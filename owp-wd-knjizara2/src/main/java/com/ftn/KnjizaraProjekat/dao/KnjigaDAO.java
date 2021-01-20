package com.ftn.KnjizaraProjekat.dao;

import java.util.List;

import com.ftn.KnjizaraProjekat.model.Knjiga;

public interface KnjigaDAO {

	public Knjiga findOne(String ISBN);

	public List<Knjiga> findAll();

	public int save(Knjiga film);

	public int update(Knjiga film);

	public int delete(String ISBN);
	
	public List<Knjiga> find(String isbn, String naziv, Long zanrId, Double cenaMin, 
			Double cenaMax, String autor, String jezik, String sortiranje, String poredak);
	
	public int findBrPrimeraka(String ISBN);
	
	public int savePrimerke(String ISBN, int brojPrimeraka);
	
	public int updatePrimerke(String ISBN, int brojPrimeraka);
}
