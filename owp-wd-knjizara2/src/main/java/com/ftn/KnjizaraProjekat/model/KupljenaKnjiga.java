package com.ftn.KnjizaraProjekat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class KupljenaKnjiga {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "knjiga_ISBN", nullable = false)
	private Knjiga knjiga;
	
//	@Column
//	private Long kupovinaId;
	
	@Column
	private int brojPrimeraka;
	
	@Column
	private double cena;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "kupovina_id", nullable = false)
    private Kupovina kupovina;
	
	public KupljenaKnjiga() {}

	public KupljenaKnjiga(Knjiga knjiga, Kupovina kupovina, int brojPrimeraka, double cena) {
		super();
		this.knjiga = knjiga;
		this.kupovina = kupovina;
		this.brojPrimeraka = brojPrimeraka;
		this.cena = cena;
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
	
	public Kupovina getKupovina() {
		return kupovina;
	}
	
	public void setKupovina(Kupovina kupovina) {
		this.kupovina = kupovina;
	}
	
}
