package com.ftn.KnjizaraProjekat.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;

public interface KorisnikService {

	Korisnik findOne(String korisnickoIme);
	Korisnik findOne(String korisnickoIme, String lozinka);
	Korisnik findOne(Long korisnikId);
	List<Korisnik> findAll();
	Korisnik save(Korisnik korisnik);
	List<Korisnik> save(List<Korisnik> korisnici);
	Korisnik update(Korisnik korisnik);
	List<Korisnik> update(List<Korisnik> korisnici);
	Korisnik delete(String korisnickoIme);
	void delete(List<String> korisnickaImena);
	List<Korisnik> find(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa, String brTel,
			LocalDate datRodjenja, LocalDateTime datReg, Boolean administrator, Boolean blokiran);
	List<Korisnik> findByKorisnickoIme(String korisnickoIme);
	
	
	// loyalty kartica
	LoyaltyKartica findLoyaltyKartica(String korisnickoIme);
	LoyaltyKartica saveLK(LoyaltyKartica lk);
	LoyaltyKartica updateLK(LoyaltyKartica lk);
	LoyaltyKartica deleteLK(LoyaltyKartica lk);
	
	// zahtev za LK
	String findZahtevZaLK(String korisnickoIme);
	String saveZahtevZaLK(String korisnickoIme);
	String deleteZahtevZaLK(String korisnickoIme);
	void updateListaZelja(Korisnik korisnik, String isbn);
	void deleteFromListaZelja(Korisnik korisnik, String isbn);

}
