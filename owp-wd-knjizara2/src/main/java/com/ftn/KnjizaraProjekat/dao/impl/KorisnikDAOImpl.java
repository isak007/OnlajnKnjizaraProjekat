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
import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.ListaZelja;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;

@Repository
public class KorisnikDAOImpl implements KorisnikDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private KnjigaDAOImpl knjigaDAOImpl; 

	
	private class KorisnikKnjigaRowCallBackHandler implements RowCallbackHandler {

		private Map<String, Korisnik> korisnici = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int index = 1;
			Long id = rs.getLong(index++);
			Boolean administrator = rs.getBoolean(index++);
			String adresa = rs.getString(index++);
			Boolean blokiran = rs.getBoolean(index++);
			String brTel = rs.getString(index++);
			LocalDateTime datReg = rs.getTimestamp(index++).toLocalDateTime();
			LocalDate datRodjenja = rs.getTimestamp(index++).toLocalDateTime().toLocalDate();
			String eMail = rs.getString(index++);
			String ime = rs.getString(index++);
			String korisnickoIme = rs.getString(index++);
			String lozinka = rs.getString(index++);
			Boolean posedujeLoyaltyKarticu = rs.getBoolean(index++);
			String prezime = rs.getString(index++);
//			Object knjigaISBN = rs.getObject(index++);
			Korisnik korisnik = korisnici.get(korisnickoIme);
			if (korisnik == null) {
				korisnik = new Korisnik(id, korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datRodjenja, datReg, 
						administrator, blokiran, posedujeLoyaltyKarticu);
				korisnici.put(korisnik.getKorisnickoIme(), korisnik); // dodavanje u kolekciju
			}
			
//			if(knjigaISBN != null) {
//				korisnik.getListaZelja().add((String)knjigaISBN);
//			}
			
		}

		public List<Korisnik> getKorisnici() {
			return new ArrayList<>(korisnici.values());
		}

	}
	
	@Override
	public Korisnik findOne(Long korisnikId) {
		String sql = 
				"SELECT ko.* FROM korisnik ko " + 
				"WHERE ko.id = ? " + 
				"ORDER BY ko.id";

		KorisnikKnjigaRowCallBackHandler rowCallbackHandler = new KorisnikKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnikId);

		if (rowCallbackHandler.getKorisnici().isEmpty()) {
			return null;
		}
		return rowCallbackHandler.getKorisnici().get(0);
	}
	
	@Override
	public Korisnik findOne(String korisnickoIme) {
		String sql = 
				"SELECT ko.*, k.ISBN FROM korisnik ko " + 
				"LEFT JOIN lista_zelja lz ON ko.korisnicko_ime = lz.korisnicko_ime " + 
				"LEFT JOIN lista_zelja_knjiga lzk ON ko.korisnicko_ime = lzk.lista_zelja_korisnicko_ime " + 
				"LEFT JOIN knjiga k ON lzk.knjiga_ISBN = k.ISBN " + 
				"WHERE ko.korisnicko_ime = ? " + 
				"ORDER BY ko.korisnicko_ime";

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
				"SELECT ko.*, k.ISBN FROM korisnik ko " + 
				"LEFT JOIN lista_zelja lz ON ko.korisnicko_ime = lz.korisnicko_ime " + 
				"LEFT JOIN lista_zelja_knjiga lzk ON ko.korisnicko_ime = lzk.lista_zelja_korisnicko_ime " + 
				"LEFT JOIN knjiga k ON lzk.knjiga_ISBN = k.ISBN " + 
				"WHERE ko.korisnicko_ime = ? AND ko.lozinka = ? " + 
				"ORDER BY ko.korisnicko_ime";

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
				"SELECT ko.*, k.ISBN FROM korisnik ko " + 
				"LEFT JOIN lista_zelja lz ON ko.korisnicko_ime = lz.korisnicko_ime " + 
				"LEFT JOIN lista_zelja_knjiga lzk ON ko.korisnicko_ime = lzk.lista_zelja_korisnicko_ime " + 
				"LEFT JOIN knjiga k ON lzk.knjiga_ISBN = k.ISBN " + 
				"ORDER BY ko.korisnicko_ime";

		KorisnikKnjigaRowCallBackHandler rowCallbackHandler = new KorisnikKnjigaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getKorisnici();
	}
	
	
	@Override
	public List<Korisnik> find(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa, String brTel,
			LocalDate datRodjenja, LocalDateTime datReg, Boolean administrator, Boolean blokiran) {
		
		ArrayList<Object> listaArgumenata = new ArrayList<Object>();
		
		String sql = "SELECT ko.*, k.ISBN FROM korisnik ko " + 
				"LEFT JOIN lista_zelja lz ON ko.korisnicko_ime = lz.korisnicko_ime " + 
				"LEFT JOIN lista_zelja_knjiga lzk ON ko.korisnicko_ime = lzk.lista_zelja_korisnicko_ime " + 
				"LEFT JOIN knjiga k ON lzk.knjiga_ISBN = k.ISBN ";
		
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
			whereSql.append("ko.e_mail LIKE ?");
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
			whereSql.append("ko.br_tel LIKE ?");
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
			sql=sql + whereSql.toString()+" ORDER BY ko.korisnicko_ime";
			jdbcTemplate.query(sql, rowCallbackHandler, listaArgumenata);
		}
		else {
			sql=sql + " ORDER BY ko.korisnicko_ime";
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
				String sql = "INSERT INTO korisnik"
						+ " (administrator,adresa,blokiran,br_tel,datum_reg,datum_rodjenja,e_mail,ime,korisnicko_ime,lozinka,poseduje_loyalty_karticu,prezime)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setBoolean(index++, korisnik.isAdministrator());
				preparedStatement.setString(index++, korisnik.getAdresa());
				preparedStatement.setBoolean(index++, korisnik.isBlokiran());
				preparedStatement.setString(index++, korisnik.getBrTel());
				preparedStatement.setTimestamp(index++, Timestamp.valueOf(korisnik.getDatumReg()));
				preparedStatement.setDate(index++, Date.valueOf(korisnik.getDatumRodjenja()));
				preparedStatement.setString(index++, korisnik.getEMail());
				preparedStatement.setString(index++, korisnik.getIme());
				preparedStatement.setString(index++, korisnik.getKorisnickoIme());
				preparedStatement.setString(index++, korisnik.getLozinka());
				preparedStatement.setBoolean(index++, korisnik.isPosedujeLoyaltyKarticu());
				preparedStatement.setString(index++, korisnik.getPrezime());
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
			
		boolean uspeh = true;
		String sql = "UPDATE korisnik SET administrator = ?, blokiran = ?, poseduje_loyalty_karticu = ? WHERE korisnicko_ime = ?";	
		uspeh = uspeh &&  jdbcTemplate.update(sql, korisnik.isAdministrator(), korisnik.isBlokiran(), korisnik.isPosedujeLoyaltyKarticu(),
				korisnik.getKorisnickoIme()) == 1;
		
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int updateListaZelja(Korisnik korisnik, String knjigaISBN) {
		
//		String sql = "DELETE FROM lista_zelja WHERE korisnicko_ime = ?";
//		jdbcTemplate.update(sql, korisnik.getKorisnickoIme());
	
//		boolean uspeh = true;
//		sql = "INSERT INTO lista_zelja (broj_knjiga, korisnicko_ime) VALUES (?, ?)";
//		if (korisnik.getListaZelja() != null && korisnik.getListaZelja().getListaKnjiga() != null) {
//			
//			for (Knjiga knjiga: korisnik.getListaZelja().getListaKnjiga()) {	
//				uspeh = uspeh &&  jdbcTemplate.update(sql, knjiga.getISBN(), korisnik.getKorisnickoIme()) == 1;
//			}
//		}
		
		boolean uspeh = true;
		
		if (korisnik.getListaZelja() != null && korisnik.getListaZelja().getListaKnjiga() != null) {
			
			boolean postoji = false; 
			for (Knjiga knjiga : korisnik.getListaZelja().getListaKnjiga()) {
				if (knjiga.getISBN().equals(knjigaISBN)){
					postoji = true;
					break;
				}
			}
			
			if (!postoji) {
				korisnik.getListaZelja().getListaKnjiga().add(knjigaDAOImpl.findOne(knjigaISBN));
				String sql = "INSERT INTO lista_zelja_knjiga (lista_zelja_korisnicko_ime, knjiga_isbn) VALUES (?, ?)";
				uspeh = uspeh &&  jdbcTemplate.update(sql, korisnik.getKorisnickoIme(), knjigaISBN) == 1;
			}
			
			else {
				uspeh = false;
			}

		}

		else {
			ListaZelja lz = new ListaZelja();
			List<Knjiga> listaKnjiga = new ArrayList<Knjiga>();
			listaKnjiga.add(knjigaDAOImpl.findOne(knjigaISBN));
			lz.setListaKnjiga(listaKnjiga);
			korisnik.setListaZelja(lz);	
			String sql = "INSERT INTO lista_zelja (broj_knjiga, korisnicko_ime) VALUES (?, ?)";
			uspeh = uspeh &&  jdbcTemplate.update(sql, 1, korisnik.getKorisnickoIme()) == 1;
			sql = "INSERT INTO lista_zelja_knjiga (lista_zelja_korisnicko_ime, knjiga_isbn) VALUES (?, ?)";
			uspeh = uspeh &&  jdbcTemplate.update(sql, korisnik.getKorisnickoIme(), knjigaISBN) == 1;
		}
		
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int deleteFromListaZelja(Korisnik korisnik, String knjigaISBN) {
		
		boolean uspeh = true;
		
		if (korisnik.getListaZelja() != null && korisnik.getListaZelja().getListaKnjiga() != null) {
			
			boolean postoji = false; 
			for (Knjiga knjiga : korisnik.getListaZelja().getListaKnjiga()) {
				if (knjiga.getISBN().equals(knjigaISBN)){
					postoji = true;
					break;
				}
			}
			
			if (postoji) {
				korisnik.getListaZelja().getListaKnjiga().remove(knjigaDAOImpl.findOne(knjigaISBN));
				String sql = "DELETE FROM lista_zelja_knjiga WHERE lista_zelja_korisnicko_ime = ? && knjiga_isbn = ?";
				uspeh = uspeh &&  jdbcTemplate.update(sql, korisnik.getKorisnickoIme(), knjigaISBN) == 1;
			}
			
		}
		
		return uspeh?1:0;
	}
	
	@Transactional
	@Override
	public int delete(String korisnickoIme) {
		String sql = "DELETE FROM lista_zelja WHERE korisnicko_ime = ?";
		jdbcTemplate.update(sql, korisnickoIme);

		sql = "DELETE FROM korisnik WHERE korisnicko_ime = ?";
		return jdbcTemplate.update(sql, korisnickoIme);
	}
	
	
	/////////////////
	private class LKRowMapper implements RowMapper<LoyaltyKartica> {
	
		@Autowired
		KorisnikDAOImpl korisnikDAO;
		
		@Override
		public LoyaltyKartica mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			int popust = rs.getInt(index++);
			int brojPoena = rs.getInt(index++);
			Long korisnikId = rs.getLong(index++);
			LoyaltyKartica lk = new LoyaltyKartica(popust,brojPoena,korisnikDAO.findOne(korisnikId));
			return lk;
		}
	}
	
	@Override
	public LoyaltyKartica findLoyaltyKartica(String korisnickoIme) {
	String sql = 
		"SELECT lk.* FROM loyalty_kartica lk "+ 
		"WHERE lk.korisnik_id = ? " + 
		"ORDER BY lk.korisnik_id";
		
		if(!jdbcTemplate.query(sql, new LKRowMapper(), korisnickoIme).isEmpty()) {
			return jdbcTemplate.query(sql, new LKRowMapper(), korisnickoIme).get(0);
		}
		else {
			return null;
		}
	}
	
	@Override
	public int saveLK(LoyaltyKartica lk) {
		String sql = "INSERT INTO loyalty_kartica (korisnik_id,broj_poena,popust) VALUES (?,?,?)";
		return jdbcTemplate.update(sql,  lk.getKorisnik().getId(), lk.getBrojPoena(), lk.getPopust());
	}
	
	@Override
	public int updateLK(LoyaltyKartica lk) {
		String sql = "UPDATE loyalty_kartica SET popust = ?, broj_poena = ? WHERE korisnik_id = ?";
		return jdbcTemplate.update(sql, lk.getPopust(), lk.getBrojPoena(), lk.getKorisnik().getId());
	}
	@Override
	public int deleteLK(LoyaltyKartica lk) {
		String sql = "DELETE FROM loyalty_kartica WHERE korisnik_id = ?";
		return jdbcTemplate.update(sql, lk.getKorisnik().getId());
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
		"SELECT * FROM zahtev_zalk  "+ 
		"WHERE korisnicko_ime = ? " +
		"ORDER BY korisnicko_ime";
		
		if(!jdbcTemplate.query(sql, new ZahtevZaLKRowMapper(), korisnickoIme).isEmpty()) {
			return jdbcTemplate.query(sql, new ZahtevZaLKRowMapper(), korisnickoIme).get(0);
		}
		else {
			return null;
		}
	}
	
	@Override
	public int saveZahtevZaLK(String korisnickoIme) {
		String sql = "INSERT INTO zahtev_zalk (korisnicko_ime) VALUES (?)";
		return jdbcTemplate.update(sql, korisnickoIme);
	}
	
	@Override
	public int deleteZahtevZaLK(String korisnickoIme) {
		String sql = "DELETE FROM zahtev_zalk WHERE korisnicko_ime = ?";
		return jdbcTemplate.update(sql, korisnickoIme);
	}

	
}
