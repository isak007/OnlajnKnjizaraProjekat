package com.ftn.KnjizaraProjekat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.KnjizaraProjekat.dao.KnjigaDAO;
import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Zanr;
import com.ftn.KnjizaraProjekat.service.KnjigaService;

@Service
public class DatabaseKnjigaService implements KnjigaService {

	@Autowired
	private KnjigaDAO knjigaDAO;
	
	@Override
	public Knjiga findOne(String ISBN) {
		return knjigaDAO.findOne(ISBN);
	}

	@Override
	public List<Knjiga> findAll() {
		return knjigaDAO.findAll();
	}

	@Override
	public Knjiga save(Knjiga knjiga) {
		knjigaDAO.save(knjiga);
		return knjiga;
	}

	@Override
	public List<Knjiga> save(List<Knjiga> knjige) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Knjiga update(Knjiga knjiga) {
		knjigaDAO.update(knjiga);
		return knjiga;
	}

	@Override
	public List<Knjiga> update(List<Knjiga> knjige) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Knjiga delete(String ISBN) {
		Knjiga knjiga = findOne(ISBN);
		if (knjiga != null)
			knjigaDAO.delete(ISBN);
		
		return knjiga;
	}

	@Override
	public List<Knjiga> deleteAll(Zanr zanr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(List<String> ISBNs) {
		// TODO Auto-generated method stub
	}
	//NACIN 1
	//U ovom metodi pretragu radimo manuelno tako što programski filtriramo kompletnu listu rezultata dobijenu sa findAll()
	//očitaju se svi filmovi iz baze pa se filtrira po uslovu
	//nije praktično imajući u vidu da u bazi može biti i 500.000 fimova
	//NACIN 2
	//ideja je koristiti select sa where delom sa se smanji ResultSet


	@Override
	public List<Knjiga> findByZanrId(Long zanrId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Knjiga> find(String isbn, String naziv, Long zanrId, Double cenaMin, Double cenaMax, 
			String autor, String jezik, String sortiranje, String poredak) {
		return knjigaDAO.find(isbn, naziv, zanrId, cenaMin, cenaMax, autor, jezik, sortiranje, poredak);
	}
	
	public int findBrPrimeraka(String ISBN) {
		return knjigaDAO.findBrPrimeraka(ISBN);
	}
	
	public String savePrimerke(String ISBN, int brojPrimeraka) {
		knjigaDAO.savePrimerke(ISBN, brojPrimeraka);
		return ISBN;
	}
	
	public String updatePrimerke(String ISBN, int brojPrimeraka) {
		knjigaDAO.updatePrimerke(ISBN, brojPrimeraka);
		return ISBN;
	}
}
