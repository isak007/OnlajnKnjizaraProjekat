package com.ftn.KnjizaraProjekat.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Kupovina {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private double ukupnaCena;
	
	@Column
	private LocalDateTime datumKupovine = LocalDateTime.now();
	
	@OneToOne
	@JoinColumn(name = "kupac_id", nullable = false)
	private Korisnik kupac;
	
	@Column
	private int brojKupljenihKnjiga;
	
	@OneToMany(mappedBy = "kupovina", cascade = CascadeType.ALL)
	private List<KupljenaKnjiga> listaKupljenihKnjiga = new ArrayList<>();
	
	
	public Kupovina() {}
	
	public Kupovina(double ukupnaCena, LocalDateTime datumKupovine,
			Korisnik kupac, int brojKupljenihKnjiga) {
		this.ukupnaCena = ukupnaCena;
		this.datumKupovine = datumKupovine;
		this.kupac = kupac;
		this.brojKupljenihKnjiga = brojKupljenihKnjiga;
	}
	
	public Kupovina(Long id, double ukupnaCena, LocalDateTime datumKupovine,
			Korisnik kupac, int brojKupljenihKnjiga) {
		this.id = id;
		this.ukupnaCena = ukupnaCena;
		this.datumKupovine = datumKupovine;
		this.kupac = kupac;
		this.brojKupljenihKnjiga = brojKupljenihKnjiga;
	}
	
	public Kupovina(double ukupnaCena, LocalDateTime datumKupovine,
			Korisnik kupac, int brojKupljenihKnjiga, ArrayList<KupljenaKnjiga> listaKupljenihKnjiga) {
		this.ukupnaCena = ukupnaCena;
		this.datumKupovine = datumKupovine;
		this.kupac = kupac;
		this.brojKupljenihKnjiga = brojKupljenihKnjiga;
		this.listaKupljenihKnjiga = listaKupljenihKnjiga;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<KupljenaKnjiga> getListaKupljenihKnjiga() {
		return (ArrayList<KupljenaKnjiga>) listaKupljenihKnjiga;
	}

	public void setListaKupljenihKnjiga(ArrayList<KupljenaKnjiga> listaKupljenihKnjiga) {
		this.listaKupljenihKnjiga.clear();
		this.listaKupljenihKnjiga.addAll(listaKupljenihKnjiga);
	}

	public double getUkupnaCena() {
		return ukupnaCena;
	}

	public void setUkupnaCena(double ukupnaCena) {
		this.ukupnaCena = ukupnaCena;
	}

	public LocalDateTime getDatumKupovine() {
		return datumKupovine;
	}

	public void setDatumKupovine(LocalDateTime datumKupovine) {
		this.datumKupovine = datumKupovine;
	}

	public Korisnik getKupac() {
		return kupac;
	}

	public void setKupac(Korisnik kupac) {
		this.kupac = kupac;
	}

	public int getBrojKupljenihKnjiga() {
		return brojKupljenihKnjiga;
	}

	public void setBrojKupljenihKnjiga(int brojKupljenihKnjiga) {
		this.brojKupljenihKnjiga = brojKupljenihKnjiga;
	}
	
	
	
}
