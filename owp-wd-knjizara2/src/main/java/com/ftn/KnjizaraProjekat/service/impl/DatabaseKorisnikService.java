package com.ftn.KnjizaraProjekat.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.KnjizaraProjekat.dao.KorisnikDAO;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;
import com.ftn.KnjizaraProjekat.service.KorisnikService;

@Service
public class DatabaseKorisnikService implements KorisnikService {

	@Autowired
	private KorisnikDAO korisnikDAO;
	
	@Override
	public Korisnik findOne(String korisnickoIme) {
		return korisnikDAO.findOne(korisnickoIme);
	}

	@Override
	public Korisnik findOne(String korisnickoIme, String lozinka) {
		return korisnikDAO.findOne(korisnickoIme, lozinka);
	}
	
	@Override
	public Korisnik findOne(Long korisnikId) {
		return korisnikDAO.findOne(korisnikId);
	}

	@Override
	public List<Korisnik> findAll() {
		return korisnikDAO.findAll();
	}

	@Override
	public Korisnik save(Korisnik korisnik) {
		korisnikDAO.save(korisnik);
		return korisnik;
	}

	@Override
	public List<Korisnik> save(List<Korisnik> korisnici) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Korisnik update(Korisnik korisnik) {
		korisnikDAO.update(korisnik);
		return korisnik;
	}

	@Override
	public List<Korisnik> update(List<Korisnik> korisnici) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Korisnik delete(String korisnickoIme) {
		Korisnik korisnik = findOne(korisnickoIme);
		if (korisnik != null) {
			korisnikDAO.delete(korisnickoIme);
		}
		return korisnik;
	}

	@Override
	public void delete(List<String> korisnickaImena) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Korisnik> find(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa, String brTel,
			LocalDate datRodjenja, LocalDateTime datReg, Boolean administrator, Boolean blokiran) {
		// minimalne inkluzivne vrednosti parametara ako su izostavljeni
		//1. način bi bilo pozivanje ogovarajuće DAO metode u odnosu na broj parametara 
		//		gde bi trebalo implementirati više dao metoda tako da pokriju različite situacije
		//2. način reši sve u DAO sloju
		
		//odabran 2.
		return korisnikDAO.find(korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel,
				datRodjenja, datReg, administrator, blokiran);
	}

	@Override
	public List<Korisnik> findByKorisnickoIme(String korisnickoIme) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// loyalty kartica
	@Override
	public LoyaltyKartica findLoyaltyKartica(String korisnickoIme) {
		return korisnikDAO.findLoyaltyKartica(korisnickoIme);
	}
	
	@Override
	public LoyaltyKartica saveLK(LoyaltyKartica lk) {
		korisnikDAO.saveLK(lk);
		return lk;
	}
	
	@Override
	public LoyaltyKartica updateLK(LoyaltyKartica lk) {
		korisnikDAO.updateLK(lk);
		return lk;
	}

	@Override
	public LoyaltyKartica deleteLK(LoyaltyKartica lk) {
		if (lk != null) {
			korisnikDAO.deleteLK(lk);
		}
		return lk;
	}
	
	
	// zahtev za LK
	@Override
	public String findZahtevZaLK(String korisnickoIme) {
		return korisnikDAO.findZahtevZaLK(korisnickoIme);
	}
	
	@Override
	public String saveZahtevZaLK(String korisnickoIme) {
		korisnikDAO.saveZahtevZaLK(korisnickoIme);
		return korisnickoIme;
	}
	
	@Override
	public String deleteZahtevZaLK(String korisnickoIme) {
		korisnikDAO.deleteZahtevZaLK(korisnickoIme);
		return korisnickoIme;
	}

	@Override
	public void updateListaZelja(Korisnik korisnik, String isbn) {
		korisnikDAO.updateListaZelja(korisnik, isbn);
	}
	
	public void deleteFromListaZelja(Korisnik korisnik, String isbn) { 
		korisnikDAO.deleteFromListaZelja(korisnik, isbn);
	}

	
}
