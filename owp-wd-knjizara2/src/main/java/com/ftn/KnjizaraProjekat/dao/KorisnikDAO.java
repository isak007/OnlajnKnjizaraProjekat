package com.ftn.KnjizaraProjekat.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;

public interface KorisnikDAO {

	public Korisnik findOne(String korisnickoIme);

	public Korisnik findOne(String korisnickoIme, String lozinka);

	public List<Korisnik> findAll();

	public List<Korisnik> find(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa, String brTel,
			LocalDate datRodjenja, LocalDateTime datReg, Boolean administrator, Boolean blokiran);
	
	public int save(Korisnik korisnik);

	public int update(Korisnik korisnik);

	public int delete(String korisnickoIme);
	
	// loyalty kartica
	public LoyaltyKartica findLoyaltyKartica(String korisnickoIme);
	
	public int saveLK(LoyaltyKartica lk);

	public int updateLK(LoyaltyKartica lk);

	public int deleteLK(LoyaltyKartica lk);
	
	// zahtev za LK
	public String findZahtevZaLK(String korisnickoIme);
	
	public int saveZahtevZaLK(String korisnickoIme);
	
	public int deleteZahtevZaLK(String korisnickoIme);
}
