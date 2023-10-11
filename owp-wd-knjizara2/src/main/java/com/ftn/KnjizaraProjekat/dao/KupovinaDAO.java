package com.ftn.KnjizaraProjekat.dao;

import java.util.List;

import com.ftn.KnjizaraProjekat.model.KupljenaKnjiga;
import com.ftn.KnjizaraProjekat.model.Kupovina;

public interface KupovinaDAO {

	public Kupovina findOne(Long kupovinaId);
	
	public List<Kupovina> findAll(String korisnickoIme);
	
	public List<Kupovina> findAll();

	public int save(Kupovina kupovina);
	
	public int saveKupljenaKnjiga(KupljenaKnjiga kupljenaKnjiga);

	public int update(Kupovina kupovina);

	public int delete(Kupovina kupovina);

	public Kupovina findByKupac(Long kupacId);


}
