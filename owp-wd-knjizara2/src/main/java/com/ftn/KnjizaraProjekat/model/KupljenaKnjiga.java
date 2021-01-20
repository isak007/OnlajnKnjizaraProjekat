package com.ftn.KnjizaraProjekat.model;

public class KupljenaKnjiga {
	private Knjiga knjiga;
	private Long kupovinaId;
	private int brojPrimeraka;
	private double cena;
	
	public KupljenaKnjiga() {}

	public KupljenaKnjiga(Knjiga knjiga, Long kupovinaId, int brojPrimeraka, double cena) {
		super();
		this.knjiga = knjiga;
		this.kupovinaId = kupovinaId;
		this.brojPrimeraka = brojPrimeraka;
		this.cena = cena;
	}
	

	
	
	public Long getKupovinaId() {
		return kupovinaId;
	}

	public void setKupovinaId(Long kupovinaId) {
		this.kupovinaId = kupovinaId;
	}

	public Knjiga getKnjiga() {
		return knjiga;
	}

	public void setKnjiga(Knjiga knjiga) {
		this.knjiga = knjiga;
	}

	public int getBrojPrimeraka() {
		return brojPrimeraka;
	}

	public void setBrojPrimeraka(int brojPrimeraka) {
		this.brojPrimeraka = brojPrimeraka;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}
	
	
}
