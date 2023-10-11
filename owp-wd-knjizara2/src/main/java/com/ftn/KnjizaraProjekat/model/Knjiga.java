package com.ftn.KnjizaraProjekat.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Knjiga {
	
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ISBN")
	private String  ISBN;
	
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BrojPrimeraka brojPrimeraka;  
	
	@Column
	private String  naziv;
	
	@Column
	private String  izdavackaKuca;
	
	@Column
	private String  autor;
	
	@Column(length =1000)
	private String  kratakOpis;
	
	@Column
	private String  jezik;
	
	@Column
	private int godinaIzdavanja;
	
	@Column
	private int brojStranica;
	
	@Column
	private double cena;
	
	@Column
	private double prosecnaOcena;
	
	@Column
	private String slika;
	
	@Column
	private String tipPoveza;
	
	@Column
	private String pismo;
	
//	@OneToMany(mappedBy = "knjiga", cascade = CascadeType.ALL)
	@ManyToMany
	private List<Zanr> zanr = new ArrayList<>();
	
	
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
		return zanr;
	}

	public void setZanrovi(List<Zanr> zanrovi) {
		this.zanr.clear();
		this.zanr.addAll(zanrovi);
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


