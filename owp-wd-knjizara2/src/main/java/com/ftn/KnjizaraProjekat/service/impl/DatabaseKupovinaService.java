package com.ftn.KnjizaraProjekat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.KnjizaraProjekat.dao.KupovinaDAO;
import com.ftn.KnjizaraProjekat.model.KupljenaKnjiga;
import com.ftn.KnjizaraProjekat.model.Kupovina;
import com.ftn.KnjizaraProjekat.service.KupovinaService;

@Service
public class DatabaseKupovinaService implements KupovinaService {

	@Autowired
	private KupovinaDAO kupovinaDAO;
	
	@Override
	public Kupovina findOne(Long kupovinaId) {
		return kupovinaDAO.findOne(kupovinaId);
	}
	
	@Override
	public Kupovina findOne(String korisnickoIme) {
		return kupovinaDAO.findOne(korisnickoIme);
	}

	@Override
	public List<Kupovina> findAll(String korisnickoIme) {
		return kupovinaDAO.findAll(korisnickoIme);
	}

	@Override
	public Kupovina save(Kupovina kupovina) {
		kupovinaDAO.save(kupovina);
		return kupovina;
	}
	
	@Override
	public KupljenaKnjiga saveKupljenaKnjiga(KupljenaKnjiga kupljenaKnjiga) {
		kupovinaDAO.saveKupljenaKnjiga(kupljenaKnjiga);
		return kupljenaKnjiga;
	}


	@Override
	public Kupovina update(Kupovina kupovina) {
		kupovinaDAO.update(kupovina);
		return kupovina;
	}


	@Override
	public Kupovina delete(Kupovina kupovina) {
		if (kupovina != null)
			kupovinaDAO.delete(kupovina);
		return kupovina;
	}


}
