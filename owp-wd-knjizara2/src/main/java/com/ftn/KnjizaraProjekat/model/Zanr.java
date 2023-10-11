package com.ftn.KnjizaraProjekat.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Zanr {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String naziv;
	
	@Column
	private String opis;
	
//	@ManyToOne
//	@JoinColumn(name = "knjiga_id", nullable = false)
//	@ManyToMany
//	private List<Knjiga> knjige = new ArrayList<>();
//	
	public Zanr(){}

	public Zanr(Long id, String naziv, String opis) {
		this.id = id;
		this.naziv = naziv;
		this.opis = opis;
	}
	
	public Zanr(String naziv, String opis) {
		this.naziv = naziv;
		this.opis = opis;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	
}
