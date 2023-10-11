package com.ftn.KnjizaraProjekat.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.ftn.KnjizaraProjekat.dao.KnjigaDAO;
import com.ftn.KnjizaraProjekat.dao.ZanrDAO;
//import com.ftn.KnjizaraProjekat.dao.ZanrDAO;
import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Zanr;

@Repository
public class KnjigaDAOImpl implements KnjigaDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ZanrDAO zanrDAO; 

	private class KnjigaZanrRowCallBackHandler implements RowCallbackHandler {

		private Map<String, Knjiga> knjige = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			String ISBN = resultSet.getString(index++);
			String autor = resultSet.getString(index++);
			Integer brojStranica = resultSet.getInt(index++);
			Double cena = resultSet.getDouble(index++);
			Integer godinaIzdavanja = resultSet.getInt(index++);
			String izdavackaKuca = resultSet.getString(index++);
			String jezik = resultSet.getString(index++);
			String kratakOpis = resultSet.getString(index++);
			String naziv = resultSet.getString(index++);
			String pismo = resultSet.getString(index++);
			Double prosecnaOcena = resultSet.getDouble(index++);
			String slika = resultSet.getString(index++);
			String tipPoveza = resultSet.getString(index++);
			
			Knjiga knjiga = knjige.get(ISBN);
			if (knjiga == null) {
				knjiga = new Knjiga(ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik, godinaIzdavanja, 
						brojStranica, cena, prosecnaOcena, slika, tipPoveza, pismo);
				knjige.put(knjiga.getISBN(), knjiga); // dodavanje u kolekciju
			}
			
			// provjera da li uopste postoji zanr za ovu knjigu
			Object zanrId = resultSet.getObject(index++);
			if (zanrId != null) {
				String zanrNaziv = resultSet.getString(index++);
				String zanrOpis = resultSet.getString(index++);
				Zanr zanr = new Zanr((Long)zanrId, zanrNaziv, zanrOpis);
				knjiga.getZanrovi().add(zanr);
			}
		}

		public List<Knjiga> getKnjige() {
			return new ArrayList<>(knjige.values());
		}

	}
	
	@Override
	public Knjiga findOne(String ISBN) {
		String sql = 
				"SELECT k.*, z.id, z.naziv, z.opis FROM knjiga k " + 
				"LEFT JOIN knjiga_zanr kz ON kz.knjiga_ISBN = k.ISBN " + 
				"LEFT JOIN zanr z ON kz.zanr_id = z.id " + 
				"WHERE k.ISBN = ? " + 
				"ORDER BY k.ISBN";

		KnjigaZanrRowCallBackHandler rowCallbackHandler = new KnjigaZanrRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, ISBN);

		return rowCallbackHandler.getKnjige().get(0);
	}
	
	@Override
	public List<Knjiga> findAll() {
		String sql = 
				"SELECT k.*, z.id, z.naziv, z.opis FROM knjiga k " + 
				"LEFT JOIN knjiga_zanr kz ON kz.knjiga_ISBN = k.ISBN " + 
				"LEFT JOIN zanr z ON kz.zanr_id = z.id " + 
				"ORDER BY k.ISBN";

		KnjigaZanrRowCallBackHandler rowCallbackHandler = new KnjigaZanrRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getKnjige();
	}

	@Transactional
	@Override
	public int save(Knjiga knjiga) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO knjiga "
						+ "(isbn,naziv,izdavacka_kuca,autor,kratak_opis,jezik,godina_izdavanja,broj_stranica,cena,prosecna_ocena,slika,tip_poveza,pismo)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, knjiga.getISBN());
				preparedStatement.setString(index++, knjiga.getNaziv());
				preparedStatement.setString(index++, knjiga.getIzdavackaKuca());
				preparedStatement.setString(index++, knjiga.getAutor());
				preparedStatement.setString(index++, knjiga.getKratakOpis());
				preparedStatement.setString(index++, knjiga.getJezik());
				preparedStatement.setInt(index++, knjiga.getGodinaIzdavanja());
				preparedStatement.setInt(index++, knjiga.getBrojStranica());
				preparedStatement.setDouble(index++, knjiga.getCena());
				preparedStatement.setDouble(index++, knjiga.getProsecnaOcena());
				preparedStatement.setString(index++, knjiga.getSlika());
				preparedStatement.setString(index++, knjiga.getTipPoveza());
				preparedStatement.setString(index++, knjiga.getPismo());
				
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		if (uspeh) {
			String sql = "INSERT INTO knjiga_zanr (knjiga_ISBN, zanr_id) VALUES (?, ?)";
			for (Zanr itZanr: knjiga.getZanrovi()) {	
				uspeh = uspeh && jdbcTemplate.update(sql, knjiga.getISBN(), itZanr.getId()) == 1;
			}	
			sql = "INSERT INTO broj_primeraka (knjiga_ISBN, broj_knjiga) VALUES (?, 0)";
			uspeh = uspeh && jdbcTemplate.update(sql, knjiga.getISBN()) == 1;
		}
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int update(Knjiga knjiga) {
		String sql ="";
		boolean uspeh = true;
		
		if (knjiga.getZanrovi() != null) {
			sql = "DELETE FROM knjiga_zanr WHERE knjiga_ISBN = ?";
			jdbcTemplate.update(sql, knjiga.getISBN());
			
			sql = "INSERT INTO knjiga_zanr (knjiga_ISBN, zanr_id) VALUES (?, ?)";
			for (Zanr itZanr: knjiga.getZanrovi()) {	
				uspeh = uspeh &&  jdbcTemplate.update(sql, knjiga.getISBN(), itZanr.getId()) == 1;
			}
		}
		
		sql = "UPDATE knjiga SET naziv = ?, izdavacka_kuca = ?, autor = ?, kratak_opis = ?, jezik = ?, godina_izdavanja = ?, broj_stranica = ?,"
				+ "  cena = ?, prosecna_ocena = ?, slika = ?, tip_poveza = ?, pismo = ? WHERE ISBN = ?";	
		uspeh = uspeh &&  jdbcTemplate.update(sql, knjiga.getNaziv(), knjiga.getIzdavackaKuca(), knjiga.getAutor(), knjiga.getKratakOpis(),
				knjiga.getJezik(), knjiga.getGodinaIzdavanja(), knjiga.getBrojStranica(), knjiga.getCena(), knjiga.getProsecnaOcena(), knjiga.getSlika(),
				knjiga.getTipPoveza(), knjiga.getPismo(), knjiga.getISBN()) == 1;
		
		return uspeh?1:0;
	}
	
	
	@Transactional
	@Override
	public int delete(String ISBN) {
		String sql = "DELETE FROM knjiga_zanr WHERE knjiga_ISBN = ?";
		jdbcTemplate.update(sql, ISBN);

		sql = "DELETE FROM knjiga WHERE id = ?";
		return jdbcTemplate.update(sql, ISBN);
	}

	
	private class KnjigaRowMapper implements RowMapper<Knjiga> {

		@Override
		public Knjiga mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			int index = 1;
			String ISBN = resultSet.getString(index++);
			String autor = resultSet.getString(index++);
			Integer brojStranica = resultSet.getInt(index++);
			Double cena = resultSet.getDouble(index++);
			Integer godinaIzdavanja = resultSet.getInt(index++);
			String izdavackaKuca = resultSet.getString(index++);
			String jezik = resultSet.getString(index++);
			String kratakOpis = resultSet.getString(index++);
			String naziv = resultSet.getString(index++);
			String pismo = resultSet.getString(index++);
			Double prosecnaOcena = resultSet.getDouble(index++);
			String slika = resultSet.getString(index++);
			String tipPoveza = resultSet.getString(index++);

			Knjiga knjiga = new Knjiga(ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik, godinaIzdavanja, 
					brojStranica, cena, prosecnaOcena, slika, tipPoveza, pismo);
			return knjiga;
		}

	}
	
	@Override
	public List<Knjiga> find(String isbn, String naziv, Long zanrId, Double cenaMin, Double cenaMax, String autor, 
			String jezik, String sortiranje, String poredak) {
		
		ArrayList<Object> listaArgumenata = new ArrayList<Object>();
		
		String sql = "SELECT * FROM knjiga k "; 
		
		StringBuffer whereSql = new StringBuffer(" WHERE ");
		boolean imaArgumenata = false;
		
		if(isbn!=null) {
			isbn = "%" + isbn + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("k.ISBN LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(isbn);
		}
		
		if(naziv!=null) {
			naziv = "%" + naziv + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("k.naziv LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(naziv);
		}

		if(cenaMin!=null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("k.cena >= ?");
			imaArgumenata = true;
			listaArgumenata.add(cenaMin);
		}
		
		if(cenaMax!=null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("k.cena <= ?");
			imaArgumenata = true;
			listaArgumenata.add(cenaMax);
		}
		
		if(autor!=null) {
			autor = "%" + autor + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("k.autor LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(autor);
		}
		
		if(jezik!=null) {
			jezik = jezik + "%";
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("k.jezik LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(jezik);
		}
		
		if(imaArgumenata) {
			if (sortiranje != null) {
				if (sortiranje.equals("poNazivu")) {
					sql = sql + whereSql.toString()+" ORDER BY k.naziv";
				}
				else if (sortiranje.equals("poZanru")) {
					if (zanrId == null) {
						sql = sql + whereSql.toString()+"ORDER BY (SELECT count(knjiga_ISBN) FROM knjiga_zanr WHERE knjiga_ISBN = k.ISBN)";
					}
					else {
						sql=sql + whereSql.toString()+" ORDER BY k.ISBN";
					}
				}
				else if (sortiranje.equals("poCeni")) {
					sql = sql + whereSql.toString()+" ORDER BY k.cena";		
				}
				else if (sortiranje.equals("poAutoru")) {
					sql = sql + whereSql.toString()+" ORDER BY k.autor";
				}
				else if (sortiranje.equals("poJeziku")) {
					sql = sql + whereSql.toString()+" ORDER BY k.jezik";
				}
				else if (sortiranje.equals("poOceni")) {
					sql = sql + whereSql.toString()+" ORDER BY k.prosecna_ocena";
				}
				
			}
			else {
				sql=sql + whereSql.toString()+" ORDER BY k.ISBN";
			}
		}
		else {
			if (sortiranje != null) {
				if (sortiranje.equals("poNazivu")) {
					sql = sql + " ORDER BY k.naziv";
				}
				else if (sortiranje.equals("poZanru")) {
					if (zanrId == null) {
						sql = sql + "ORDER BY (SELECT count(knjiga_ISBN) FROM knjiga_zanr WHERE knjiga_isbn = k.ISBN)";
					}
					else {
						sql=sql + " ORDER BY k.ISBN";
					}
				}
				else if (sortiranje.equals("poCeni")) {
					sql = sql + " ORDER BY k.cena";		
				}
				else if (sortiranje.equals("poAutoru")) {
					sql = sql + " ORDER BY k.autor";
				}
				else if (sortiranje.equals("poJeziku")) {
					sql = sql + " ORDER BY k.jezik";
				}
				else if (sortiranje.equals("poOceni")) {
					sql = sql + " ORDER BY k.prosecna_ocena";
				}
			}
			else {
				sql=sql + " ORDER BY k.ISBN";
			}
		}
		
		if (poredak != null && sortiranje != null) {
			if (poredak.equals("opadajuci")) {
				sql = sql + " DESC";
			}
		}
		
		System.out.println(sql);
		
		List<Knjiga> knjige = jdbcTemplate.query(sql, listaArgumenata.toArray(), new KnjigaRowMapper());
		
		for (Knjiga knjiga : knjige) {
			knjiga.setZanrovi(findKnjigaZanr(knjiga.getISBN(), null));
		}
		//ako se traži knjiga sa određenim žanrom  
		// tada se taj žanr mora nalaziti u listi žanrova od knjige
		if(zanrId!=null)
			for (Iterator<Knjiga> iterator = knjige.iterator(); iterator.hasNext();) {
				Knjiga knjiga = (Knjiga) iterator.next();
				boolean zaBrisanje = true;
				for (Zanr zanr : knjiga.getZanrovi()) {
					if(zanr.getId() == zanrId) {
						zaBrisanje = false;
						break;
					}
				}
				if(zaBrisanje)
					iterator.remove();
			}
		
		
		return knjige;
	}
	
	
	
	private class KnjigaZanrRowMapper implements RowMapper<Object []> {

		@Override
		public Object [] mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			String knjigaISBN = rs.getString(index++);
			Long zanrId = rs.getLong(index++);

			Object [] knjigaZanr = {knjigaISBN, zanrId};
			return knjigaZanr;
		}
	}
	
	
	private List<Zanr> findKnjigaZanr(String knjigaISBN, Long zanrId) {
		
		List<Zanr> zanroviFilma = new ArrayList<Zanr>();
		
		ArrayList<Object> listaArgumenata = new ArrayList<Object>();
		
		String sql = 
				"SELECT kz.knjiga_ISBN, kz.zanr_id FROM knjiga_zanr kz ";
		
		StringBuffer whereSql = new StringBuffer(" WHERE ");
		boolean imaArgumenata = false;
		
		if(knjigaISBN!=null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("kz.knjiga_ISBN LIKE ?");
			imaArgumenata = true;
			listaArgumenata.add(knjigaISBN);
		}
		
		if(zanrId!=null) {
			if(imaArgumenata)
				whereSql.append(" AND ");
			whereSql.append("kz.zanr_id = ?");
			imaArgumenata = true;
			listaArgumenata.add(zanrId);
		}

		if(imaArgumenata)
			sql=sql + whereSql.toString()+" ORDER BY kz.knjiga_ISBN";
		else
			sql=sql + " ORDER BY kz.knjiga_ISBN";
		System.out.println(sql);
		
		List<Object[]> knjigaZanrovi = jdbcTemplate.query(sql, listaArgumenata.toArray(), new KnjigaZanrRowMapper()); 
				
		for (Object[] kz : knjigaZanrovi) {
			zanroviFilma.add(zanrDAO.findOne((Long)kz[1]));
		}
		return zanroviFilma;
	}
	
	private class brojPrimerakaRowMapper implements RowMapper<Object []> {

		@Override
		public Object [] mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			String knjigaISBN = rs.getString(index++);
			int brojKnjiga = rs.getInt(index++);

			Object [] primerci = {knjigaISBN, brojKnjiga};
			return primerci;
		}
	}
	
	
	@Transactional
	@Override
	public int savePrimerke(String ISBN, int brojPrimeraka) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO broj_primeraka VALUES (?,?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, ISBN);
				preparedStatement.setInt(index++, brojPrimeraka);
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int updatePrimerke(String ISBN, int brojPrimeraka) {
		
		boolean uspeh = true;
		String sql = "UPDATE broj_primeraka SET broj_knjiga = ? WHERE knjiga_ISBN = ?";	
		uspeh = uspeh &&  jdbcTemplate.update(sql, brojPrimeraka, ISBN) == 1;
		
		return uspeh?1:0;
	}
	
	
	@Override
	public int findBrPrimeraka(String ISBN) {
		ArrayList<Object> listaArgumenata = new ArrayList<Object>();
		listaArgumenata.add(ISBN);
		
		String sql = 
				"SELECT knjiga_ISBN, broj_knjiga FROM broj_primeraka"
				+ " WHERE knjiga_ISBN LIKE ? ORDER BY knjiga_ISBN";
		
		List<Object[]> primerci = jdbcTemplate.query(sql, listaArgumenata.toArray(), new brojPrimerakaRowMapper());
		// ako nikad nije postojao objekat za primerke, tj. knjiga se ne nalazi u sistem
		int brojPrimeraka = -1;
		// ako postoji objekat za primerke 
		if (!primerci.isEmpty()) brojPrimeraka = 0;
		
		for (Object[] knjiga : primerci) {
			if (knjiga[0].equals(ISBN)) {
				brojPrimeraka = (int)knjiga[1];
			}
		}
		
		return brojPrimeraka;
	}

}
