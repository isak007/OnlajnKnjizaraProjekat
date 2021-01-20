package com.ftn.KnjizaraProjekat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.KnjizaraProjekat.dao.ZanrDAO;
import com.ftn.KnjizaraProjekat.model.Zanr;
import com.ftn.KnjizaraProjekat.service.ZanrService;

@Service
public class DatabaseZanrService implements ZanrService {

	@Autowired
//	@Qualifier("zanrDAOOldCode")
	private ZanrDAO zanrDAO;

	@Override
	public Zanr findOne(Long id) {
		return zanrDAO.findOne(id);
	}

	@Override
	public List<Zanr> findAll() {
		return zanrDAO.findAll();
	}

	@Override
	public List<Zanr> find(Long[] ids) {
		List<Zanr> rezultat = new ArrayList<>();
		for (Long id: ids) {
			Zanr zanr = zanrDAO.findOne(id);
			rezultat.add(zanr);
		}

		return rezultat;
	}

	@Override
	public Zanr save(Zanr zanr) {
		zanrDAO.save(zanr);
		return zanr;
	}

	@Override
	public List<Zanr> save(List<Zanr> zanrovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Zanr update(Zanr zanr) {
		zanrDAO.update(zanr);
		return zanr;
	}

	@Override
	public List<Zanr> update(List<Zanr> zanrovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Zanr delete(Long id) {
		Zanr zanr = findOne(id);
		if (zanr != null) {
			zanrDAO.delete(id);
		}
		return zanr;
	}

	@Override
	public void delete(List<Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Zanr> find(String naziv) {
		// minimalne inkluzivne vrednosti parametara ako su izostavljeni
		//1. način bi bilo pozivanje ogovarajuće DAO metode u odnosu na broj parametara 
		//		gde bi trebalo implementirati više dao metoda tako da pokriju različite situacije
		//2. način reši sve u DAO sloju
		
		//odabran 1.
		if (naziv == null) {
			return zanrDAO.findAll();
		}
		return zanrDAO.find(naziv);
	}

}
