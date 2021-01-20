package com.ftn.KnjizaraProjekat.model;

import java.time.LocalDateTime;

import com.ftn.KnjizaraProjekat.enums.StatusKomentara;

public class Komentar {
	private Long id;
	private String tekst;
	private int ocena;
	private LocalDateTime datum;
	private Korisnik autor;
	private Knjiga knjiga;
	private StatusKomentara status;
	
	
	public Komentar() {}


	public Komentar(Long id,String tekst, int ocena, LocalDateTime datum, Korisnik autor, Knjiga knjiga,
			StatusKomentara status) {
		this.id = id;
		this.tekst = tekst;
		this.ocena = ocena;
		this.datum = datum;
		this.autor = autor;
		this.knjiga = knjiga;
		this.status = status;
	}
	
	public Komentar(String tekst, int ocena, LocalDateTime datum, Korisnik autor, Knjiga knjiga,
			StatusKomentara status) {
		this.tekst = tekst;
		this.ocena = ocena;
		this.datum = datum;
		this.autor = autor;
		this.knjiga = knjiga;
		this.status = status;
	}

	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public LocalDateTime getDatum() {
		return datum;
	}

	public void setDatum(LocalDateTime datum) {
		this.datum = datum;
	}

	public Korisnik getAutor() {
		return autor;
	}

	public void setAutor(Korisnik autor) {
		this.autor = autor;
	}

	public Knjiga getKnjiga() {
		return knjiga;
	}

	public void setKnjiga(Knjiga knjiga) {
		this.knjiga = knjiga;
	}

	public StatusKomentara getStatus() {
		return status;
	}

	public void setStatus(StatusKomentara status) {
		this.status = status;
	}
	
	
}
