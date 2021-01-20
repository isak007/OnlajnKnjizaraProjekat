package com.ftn.KnjizaraProjekat.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.KnjizaraProjekat.dao.KorisnikDAO;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;

@Repository
public class KorisnikDAOImpl implements KorisnikDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	private class KorisnikKnjigaRowCallBackHandler implements RowCallbackHandler {

		private Map<String, Korisnik> korisnici = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int index = 1;
			String korisnickoIme = rs.getString(index++);
			String lozinka = rs.getString(index++);
			String eMail = rs.getString(index++);
			String ime = rs.getString(index++);
			String prezime = rs.getString(index++);
			String adresa = rs.getString(index++);
			String brTel = rs.getString(index++);
			LocalDate datRodjenja = rs.getTimestamp(index++).toLocalDateTime().toLocalDate();
			LocalDateTime datReg = rs.getTimestamp(index++).toLocalDateTime();
			Boolean administrator = rs.getBoolean(index++);
			Boolean blokiran = rs.getBoolean(index++);
			Boolean loyaltyKartica = rs.getBoolean(index++);
			Object knjigaISBN = rs.getObject(index++);
			Korisnik korisnik = korisnici.get(korisnickoIme);
			if (korisnik == null) {
				korisnik = new Korisnik(korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datRodjenja, datReg, 
						administrator, blokiran, loyaltyKartica);
				korisnici.put(korisnik.getKorisnickoIme(), korisnik); // dodavanje u kolekciju
			}
			
			if(knjigaISBN != null) {
				korisnik.getListaZelja().add((String)knjigaISBN);
			}
			
		}

		public List<Korisnik> getKorisnici() {
			return new ArrayList<>(korisnici.values());
		}

	}
	
	@Override
	public Korisnik findOne(String korisnickoIme) {
		String sql = 
				"SELECT ko.*, k.ISBN FROM korisnici ko " + 
				"LEFT JOIN listaZelja lz ON ko.korisnickoIme = lz.korisnickoIme " + 
				"LEFT JOIN knjige k ON lz.knjigaISBN = k.ISBN " + 
				"WHERE ko.korisnickoIme = ? " + 
				"ORDER BY ko.korisnickoIme";

		KorisnikKnjigaRowCallBackHandler rowCallbackHandler = new KorisnikKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnickoIme);

		if (rowCallbackHandler.getKorisnici().isEmpty()) {
			return null;
		}
		return rowCallbackHandler.getKorisnici().get(0);
	}
	
	@Override
	public Korisnik findOne(String korisnickoIme, String lozinka) {
		String sql = 
				"SELECT ko.*, k.ISBN FROM korisnici ko " + 
				"LEFT JOIN listaZelja lz ON ko.korisnickoIme = lz.korisnickoIme " + 
				"LEFT JOIN knjige k ON lz.knjigaISBN = k.ISBN " + 
				"WHERE ko.korisnickoIme = ? AND ko.lozinka = ?" + 
				"ORDER BY ko.korisnickoIme";

		KorisnikKnjigaRowCallBackHandler rowCallbackHandler = new KorisnikKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnickoIme, lozinka);

		if (rowCallbackHandler.getKorisnici().isEmpty()) {
			return null;
		}
		return rowCallbackHandler.getKorisnici().get(0);
	}
	
	@Override
	public List<Korisnik> findAll() {
		String sql = 
				"SELECT ko.*, k.ISBN FROM korisnici ko " + 
				"LEFT JOIN listaZelja lz ON ko.korisnickoIme = lz.korisnickoIme " + 
				"LEFT JOIN knjige k ON lz.knjigaISBN = k.ISBN " + 
				"ORDER BY ko.korisnickoIme";

		KorisnikKnjigaRowCallBackHandler rowCallbackHandler = new KorisnikKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getKorisnici();
	}
	
	
	@Override
	public List<Korisnik> find(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa, String brTel,
			LocalDate datRodjenja, LocalDateTime datReg, Boolean administrator, Boolean blokiran) {
		
		ArrayList<Object> listaArgumenata = new ArrayList<Object>();
		
		String sql = "SELECT ko.*, k.ISBN FROM korisnici ko " + 
				"LEFT JOIN listaZelja lz ON ko.korisnickoIme = lz.korisnickoIme " + 
				"LEFT JOIN knjige k ON lz.knjigaISBN = k.ISBN ";
		
		StringBuffer whereSql = new StringBuffer(" WHERE ");
		boolean imaArgumenata = false;
		
		if(korisnickoIme!=null) {
			korisnickoIme = "%" + korisnickoIme + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.korisnickoIme LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(korisnickoIme);
		}
		
		if(lozinka!=null) {
			lozinka = "%" + lozinka + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.lozinka LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(lozinka);
		}
		
		if(eMail!=null) {
			eMail = "%" + eMail + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.eMail LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(eMail);
		}
		
		if(ime!=null) {
			ime = "%" + ime + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.ime LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(ime);
		}
		
		if(prezime!=null) {
			prezime = "%" + prezime + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.prezime LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(prezime);
		}
		
		if(adresa!=null) {
			adresa = "%" + adresa + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.adresa LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(adresa);
		}
		
		if(brTel!=null) {
			brTel = "%" + brTel + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("ko.brTel LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(brTel);
		}
		
		if(administrator!=null) {	
			//vraća samo administratore ili sve korisnike sistema
			String administratorSql = (administrator)? "ko.administrator = 1": "ko.administrator >= 0";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append(administratorSql);
			imaArgumenata = true;
		}
		
		if(blokiran!=null) {	
			//vraća samo administratore ili sve korisnike sistema
			String blokiranSql = (blokiran)? "ko.blokiran = 1": "ko.blokiran >= 0";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append(blokiranSql);
			imaArgumenata = true;
		}
		
		KorisnikKnjigaRowCallBackHandler rowCallbackHandler = new KorisnikKnjigaRowCallBackHandler();
		if(imaArgumenata) {
			sql=sql + whereSql.toString()+" ORDER BY ko.korisnickoIme";
			jdbcTemplate.query(sql, rowCallbackHandler, listaArgumenata);
		}
		else {
			sql=sql + " ORDER BY ko.korisnickoIme";
			jdbcTemplate.query(sql, rowCallbackHandler);
		}
		
		System.out.println(sql);
		return rowCallbackHandler.getKorisnici();
		
		
	}
	
	
	@Transactional
	@Override
	public int save(Korisnik korisnik) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO korisnici VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, korisnik.getKorisnickoIme());
				preparedStatement.setString(index++, korisnik.getLozinka());
				preparedStatement.setString(index++, korisnik.getEMail());
				preparedStatement.setString(index++, korisnik.getIme());
				preparedStatement.setString(index++, korisnik.getPrezime());
				preparedStatement.setString(index++, korisnik.getAdresa());
				preparedStatement.setString(index++, korisnik.getBrTel());
				preparedStatement.setDate(index++, Date.valueOf(korisnik.getDatumRodjenja()));
				preparedStatement.setTimestamp(index++, Timestamp.valueOf(korisnik.getDatumReg()));
				preparedStatement.setBoolean(index++, korisnik.isAdministrator());
				preparedStatement.setBoolean(index++, korisnik.isBlokiran());
				preparedStatement.setBoolean(index++, korisnik.isLoyaltyKartica());
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh?1:0;
	}
	
	
	@Transactional
	@Override
	public int update(Korisnik korisnik) {
		
		String sql = "DELETE FROM listaZelja WHERE korisnickoIme = ?";
		jdbcTemplate.update(sql, korisnik.getKorisnickoIme());
	
		boolean uspeh = true;
		sql = "INSERT INTO listaZelja (knjigaISBN, korisnickoIme) VALUES (?, ?)";
		for (String knjigaISBN: korisnik.getListaZelja()) {	
			uspeh = uspeh &&  jdbcTemplate.update(sql, knjigaISBN, korisnik.getKorisnickoIme()) == 1;
		}
		
		sql = "UPDATE korisnici SET administrator = ?, blokiran = ?, loyaltyKartica = ? WHERE korisnickoIme = ?";	
		uspeh = uspeh &&  jdbcTemplate.update(sql, korisnik.isAdministrator(), korisnik.isBlokiran(), korisnik.isLoyaltyKartica(),
				korisnik.getKorisnickoIme()) == 1;
		
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int delete(String korisnickoIme) {
		String sql = "DELETE FROM listaZelja WHERE korisnickoIme = ?";
		jdbcTemplate.update(sql, korisnickoIme);

		sql = "DELETE FROM korisnici WHERE korisnickoIme = ?";
		return jdbcTemplate.update(sql, korisnickoIme);
	}
	
	
	/////////////////
	private class LKRowMapper implements RowMapper<LoyaltyKartica> {
	
		@Override
		public LoyaltyKartica mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			int popust = rs.getInt(index++);
			int brojPoena = rs.getInt(index++);
			String kupacId = rs.getString(index++);
			LoyaltyKartica lk = new LoyaltyKartica(popust,brojPoena,kupacId);
			return lk;
		}
	}
	
	@Override
	public LoyaltyKartica findLoyaltyKartica(String korisnickoIme) {
	String sql = 
		"SELECT lk.* FROM loyaltyKartica lk "+ 
		"WHERE lk.kupacId = ? " + 
		"ORDER BY lk.kupacId";
		
		if(!jdbcTemplate.query(sql, new LKRowMapper(), korisnickoIme).isEmpty()) {
			return jdbcTemplate.query(sql, new LKRowMapper(), korisnickoIme).get(0);
		}
		else {
			return null;
		}
	}
	
	@Override
	public int saveLK(LoyaltyKartica lk) {
		String sql = "INSERT INTO loyaltyKartica (popust,brojPoena,kupacId) VALUES (?,?,?)";
		return jdbcTemplate.update(sql, lk.getPopust(), lk.getBrojPoena(), lk.getKupacId());
	}
	
	@Override
	public int updateLK(LoyaltyKartica lk) {
		String sql = "UPDATE loyaltyKartica SET popust = ?, brojPoena = ? WHERE kupacId = ?";
		return jdbcTemplate.update(sql, lk.getPopust(), lk.getBrojPoena(), lk.getKupacId());
	}
	@Override
	public int deleteLK(LoyaltyKartica lk) {
		String sql = "DELETE FROM loyaltyKartica WHERE kupacId = ?";
		return jdbcTemplate.update(sql, lk.getKupacId());
	}
	
	
	//////////////////
	
	
	private class ZahtevZaLKRowMapper implements RowMapper<String> {
		
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			String korisnickoIme = rs.getString(index++);
			return korisnickoIme;
		}
	}

	@Override
	public String findZahtevZaLK(String korisnickoIme) {
	String sql = 
		"SELECT * FROM zahtevZaLK  "+ 
		"WHERE korisnickoIme = ? " +
		"ORDER BY korisnickoIme";
		
		if(!jdbcTemplate.query(sql, new ZahtevZaLKRowMapper(), korisnickoIme).isEmpty()) {
			return jdbcTemplate.query(sql, new ZahtevZaLKRowMapper(), korisnickoIme).get(0);
		}
		else {
			return null;
		}
	}
	
	@Override
	public int saveZahtevZaLK(String korisnickoIme) {
		String sql = "INSERT INTO zahtevZaLK (korisnickoIme) VALUES (?)";
		return jdbcTemplate.update(sql, korisnickoIme);
	}
	
	@Override
	public int deleteZahtevZaLK(String korisnickoIme) {
		String sql = "DELETE FROM zahtevZaLK WHERE korisnickoIme = ?";
		return jdbcTemplate.update(sql, korisnickoIme);
	}
	
}
