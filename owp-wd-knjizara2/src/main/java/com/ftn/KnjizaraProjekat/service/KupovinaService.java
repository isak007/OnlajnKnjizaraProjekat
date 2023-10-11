package com.ftn.KnjizaraProjekat.service;

import java.util.List;

import com.ftn.KnjizaraProjekat.model.KupljenaKnjiga;
import com.ftn.KnjizaraProjekat.model.Kupovina;

public interface KupovinaService {
	
	Kupovina findOne(Long kupovinaId);
	
	List<Kupovina> findAll(String korisnickoIme);
	
	List<Kupovina> findAll();

	Kupovina save(Kupovina kupovina);
	
	KupljenaKnjiga saveKupljenaKnjiga(KupljenaKnjiga kupljenaKnjiga);

	Kupovina update(Kupovina kupovina);

	Kupovina delete(Kupovina kupovina);

	Kupovina findByKupac(Long kupacId);
	
	
}
