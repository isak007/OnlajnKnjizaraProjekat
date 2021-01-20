package com.ftn.KnjizaraProjekat.dao;

import java.util.List;

import com.ftn.KnjizaraProjekat.model.KupljenaKnjiga;
import com.ftn.KnjizaraProjekat.model.Kupovina;

public interface KupovinaDAO {

	public Kupovina findOne(Long kupovinaId);
	
	public Kupovina findOne(String korisnickoIme);

	public List<Kupovina> findAll(String korisnickoIme);

	public int save(Kupovina kupovina);
	
	public int saveKupljenaKnjiga(KupljenaKnjiga kupljenaKnjiga);

	public int update(Kupovina kupovina);

	public int delete(Kupovina kupovina);


}
