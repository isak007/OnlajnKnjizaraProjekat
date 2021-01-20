package com.ftn.KnjizaraProjekat.service;

import java.util.List;

import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Zanr;

public interface KnjigaService {

	Knjiga findOne(String ISBN);
	List<Knjiga> findAll();
	Knjiga save(Knjiga knjiga);
	List<Knjiga> save(List<Knjiga> knjige);
	Knjiga update(Knjiga knjiga);
	List<Knjiga> update(List<Knjiga> knjige);
	Knjiga delete(String ISBN);
	List<Knjiga> deleteAll(Zanr zanr);
	void delete(List<String> ISBNs);
	int findBrPrimeraka(String ISBN);
	List<Knjiga> findByZanrId(Long zanrId);
	List<Knjiga> find(String isbn, String naziv, Long zanrId,
			Double cenaMin, Double cenaMax, String autor, String jezik, String sortiranje, String poredak);
	public String savePrimerke(String ISBN, int brojPrimeraka);
	public String updatePrimerke(String ISBN, int brojPrimeraka);
}
