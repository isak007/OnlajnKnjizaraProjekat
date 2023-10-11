package com.ftn.KnjizaraProjekat.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.KnjizaraProjekat.dao.KupovinaDAO;
import com.ftn.KnjizaraProjekat.model.KupljenaKnjiga;
import com.ftn.KnjizaraProjekat.model.Kupovina;
import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.KorisnikService;

@Repository
public class KupovinaDAOImpl implements KupovinaDAO{

	@Autowired
	KorisnikService korisnikService;
	
	@Autowired
	KnjigaService knjigaService;

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	private class KupovinaKnjigaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Kupovina> kupovine = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int index = 1;
			Long id = rs.getLong(index++);
			int brojKupljenihKnjiga = rs.getInt(index++);
			LocalDateTime datKupovine = rs.getTimestamp(index++).toLocalDateTime();
			double ukupnaCena = rs.getDouble(index++);
			Long kupacId = rs.getLong(index++);

			Object knjigaISBN = rs.getObject(index++);
			Kupovina kupovina = kupovine.get(id);

			if (kupovina == null) {
				kupovina = new Kupovina(id,ukupnaCena,datKupovine,korisnikService.findOne(kupacId),brojKupljenihKnjiga);
				kupovine.put(kupovina.getId(), kupovina); // dodavanje u kolekciju
			}
			
			if(knjigaISBN != null) {
				int brojPrimeraka = rs.getInt(index++);
				double cena = rs.getDouble(index++);
				KupljenaKnjiga kupljenaKnjiga = new KupljenaKnjiga(knjigaService.findOne((String)knjigaISBN), kupovina, brojPrimeraka, cena);
				kupovina.getListaKupljenihKnjiga().add(kupljenaKnjiga);
			}
		}

		public List<Kupovina> getKupovine() {
			return new ArrayList<>(kupovine.values());
		}

	}
	
	@Override
	public Kupovina findOne(Long kupovinaId) {
		String sql = 
				"SELECT kup.*, kk.knjiga_ISBN, kk.broj_primeraka, kk.cena FROM kupovina kup " + 
				"LEFT JOIN kupljena_knjiga kk ON kup.id = kk.kupovina_id " + 
				"WHERE kup.id = ? " + 
				"ORDER BY kup.id DESC";

		KupovinaKnjigaRowCallBackHandler rowCallbackHandler = new KupovinaKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, kupovinaId);

		if (rowCallbackHandler.getKupovine().isEmpty()) {
			return null;
		}
		return rowCallbackHandler.getKupovine().get(0);
	}
	
	@Override
	public Kupovina findByKupac(Long kupacId) {
		String sql = 
				"SELECT kup.*, kk.knjiga_ISBN, kk.broj_primeraka, kk.cena FROM kupovina kup " + 
				"LEFT JOIN kupljena_knjiga kk ON kup.id = kk.kupovina_id " + 
				"WHERE kup.kupac_id = ? " + 
				"ORDER BY kup.id DESC";

		KupovinaKnjigaRowCallBackHandler rowCallbackHandler = new KupovinaKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, kupacId);

		if (rowCallbackHandler.getKupovine().isEmpty()) {
			return null;
		}
		return rowCallbackHandler.getKupovine().get(0);
	}
	
	@Override
	public List<Kupovina> findAll(String korisnickoIme) {
		
		String sql = 
				"SELECT kup.*, kk.knjiga_ISBN, kk.broj_primeraka, kk.cena FROM kupovina kup " + 
				"LEFT JOIN kupljena_knjiga kk ON kup.id = kk.kupovina_id " + 
				"WHERE kup.kupac_id = ? " + 
				"ORDER BY kup.datum_kupovine DESC";

		KupovinaKnjigaRowCallBackHandler rowCallbackHandler = new KupovinaKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnikService.findOne(korisnickoIme).getId());

		return rowCallbackHandler.getKupovine();
	}
	
	@Override
	public List<Kupovina> findAll() {
		String sql = 
				"SELECT kup.*, kk.knjiga_ISBN, kk.broj_primeraka, kk.cena FROM kupovina kup " + 
				"LEFT JOIN kupljena_knjiga kk ON kup.id = kk.kupovina_id " + 
				"ORDER BY kup.datum_kupovine DESC";

		KupovinaKnjigaRowCallBackHandler rowCallbackHandler = new KupovinaKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getKupovine();
	}
	
	@Transactional
	@Override
	public int save(Kupovina kupovina) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO kupovina (ukupna_cena,datum_kupovine,kupac_id,broj_kupljenih_knjiga) VALUES (?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setDouble(index++, kupovina.getUkupnaCena());
				preparedStatement.setTimestamp(index++, Timestamp.valueOf(kupovina.getDatumKupovine()));
				preparedStatement.setLong(index++, kupovina.getKupac().getId());
				preparedStatement.setInt(index++, kupovina.getBrojKupljenihKnjiga());
				
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int saveKupljenaKnjiga(KupljenaKnjiga kupljenaKnjiga) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO kupljena_knjiga (knjiga_isbn,kupovina_id,broj_primeraka,cena) VALUES (?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, kupljenaKnjiga.getKnjiga().getISBN());
				preparedStatement.setLong(index++, kupljenaKnjiga.getKupovina().getId());
				preparedStatement.setInt(index++, kupljenaKnjiga.getBrojPrimeraka());
				preparedStatement.setDouble(index++, kupljenaKnjiga.getCena());
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh?1:0;
	}
	
	
	@Transactional
	@Override
	public int update(Kupovina kupovina) {
		
		
		String sql = "DELETE FROM kupljena_knjiga WHERE kupovina_id = ?";
		jdbcTemplate.update(sql, kupovina.getId());
		
		boolean uspeh = true;
		sql = "INSERT INTO kupljena_knjiga (knjiga_ISBN, kupovina_id, broj_primeraka, cena) VALUES (?,?,?,?)";
		for (KupljenaKnjiga kk: kupovina.getListaKupljenihKnjiga()) {	
			uspeh = uspeh &&  jdbcTemplate.update(sql, kk.getKnjiga().getISBN(), kupovina.getId(),
					kk.getBrojPrimeraka(), kk.getCena()) == 1;
		}
		
		System.out.println(kupovina.getUkupnaCena());
		
		sql = "UPDATE kupovina SET ukupna_cena = ?, broj_kupljenih_knjiga = ? WHERE id = ?";	
		uspeh = uspeh &&  jdbcTemplate.update(sql, kupovina.getUkupnaCena(), kupovina.getBrojKupljenihKnjiga(),
				kupovina.getId()) == 1;
		
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int delete(Kupovina kupovina) {
		String sql = "DELETE FROM kupljena_knjiga WHERE kupovina_id = ?";
		jdbcTemplate.update(sql, kupovina.getId());

		sql = "DELETE FROM kupovina WHERE id = ?";
		return jdbcTemplate.update(sql, kupovina.getId());
	}
	
	
}
