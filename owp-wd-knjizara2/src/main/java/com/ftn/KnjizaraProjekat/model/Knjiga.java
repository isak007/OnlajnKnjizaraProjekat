package com.ftn.KnjizaraProjekat.model;

import java.util.ArrayList;
import java.util.List;

public class Knjiga {
	private String  ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik;
	private int godinaIzdavanja, brojStranica;
	private double cena, prosecnaOcena;
	private String slika;
	private String tipPoveza;
	private String pismo;
	private List<Zanr> zanrovi = new ArrayList<>();
	
	public Knjiga(){}
	
	public Knjiga(String ISBN, String naziv, String izdavackaKuca, String autor, String kratakOpis, String jezik,
			int godinaIzdavanja, int brojStranica, double cena, double prosecnaOcena, String slika,
			String tipPoveza, String pismo) {
		super();
		this.ISBN = ISBN;
		this.naziv = naziv;
		this.izdavackaKuca = izdavackaKuca;
		this.autor = autor;
		this.kratakOpis = kratakOpis;
		this.jezik = jezik;
		this.godinaIzdavanja = godinaIzdavanja;
		this.brojStranica = brojStranica;
		this.cena = cena;
		this.prosecnaOcena = prosecnaOcena;
		this.slika = slika;
		this.tipPoveza = tipPoveza;
		this.pismo = pismo;
	}

	public List<Zanr> getZanrovi() {
		return zanrovi;
	}

	public void setZanrovi(List<Zanr> zanrovi) {
		this.zanrovi.clear();
		this.zanrovi.addAll(zanrovi);
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getIzdavackaKuca() {
		return izdavackaKuca;
	}

	public void setIzdavackaKuca(String izdavackaKuca) {
		this.izdavackaKuca = izdavackaKuca;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getKratakOpis() {
		return kratakOpis;
	}

	public void setKratakOpis(String kratakOpis) {
		this.kratakOpis = kratakOpis;
	}

	public String getJezik() {
		return jezik;
	}

	public void setJezik(String jezik) {
		this.jezik = jezik;
	}

	public int getGodinaIzdavanja() {
		return godinaIzdavanja;
	}

	public void setGodinaIzdavanja(int godinaIzdavanja) {
		this.godinaIzdavanja = godinaIzdavanja;
	}

	public int getBrojStranica() {
		return brojStranica;
	}

	public void setBrojStranica(int brojStranica) {
		this.brojStranica = brojStranica;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public double getProsecnaOcena() {
		return prosecnaOcena;
	}

	public void setProsecnaOcena(double prosecnaOcena) {
		this.prosecnaOcena = prosecnaOcena;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getTipPoveza() {
		return tipPoveza;
	}

	public void setTipPoveza(String tipPoveza) {
		this.tipPoveza = tipPoveza;
	}

	public String getPismo() {
		return pismo;
	}

	public void setPismo(String pismo) {
		this.pismo = pismo;
	}
	
	
}


