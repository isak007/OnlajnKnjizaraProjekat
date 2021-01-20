package com.ftn.KnjizaraProjekat.dao;

import java.util.ArrayList;
import java.util.List;

import com.ftn.KnjizaraProjekat.model.Zanr;

public interface ZanrDAO {

	public Zanr findOne(Long id);

	public List<Zanr> findAll();

	public List<Zanr> find(String naziv);
	
	public int save(Zanr zanr);

	public int [] save(ArrayList<Zanr> zanrovi);
	
	public int update(Zanr zanr);

	public int delete(Long id);
}
