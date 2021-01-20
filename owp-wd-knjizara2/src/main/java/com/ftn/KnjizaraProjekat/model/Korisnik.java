package com.ftn.KnjizaraProjekat.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Korisnik {

	private String korisnickoIme="", lozinka="", eMail="", ime="", prezime="", adresa="", brTel="";
	private LocalDate datumRodjenja;
	private LocalDateTime datumReg = LocalDateTime.now();
	private boolean blokiran = false;
	private boolean administrator = false;
	private boolean loyaltyKartica = false;
	private List<String> listaZelja = new ArrayList<>();
	
	public Korisnik() {}
	
	public Korisnik(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa,
			String brTel, LocalDate datumRodjenja) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.eMail = eMail;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brTel = brTel;
		this.datumRodjenja = datumRodjenja;
	}
	
	public Korisnik(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa,
			String brTel, LocalDate datumRodjenja, LocalDateTime datumReg) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.eMail = eMail;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brTel = brTel;
		this.datumRodjenja = datumRodjenja;
		this.datumReg = datumReg;
	}
	
	public Korisnik(String korisnickoIme, String lozinka, String eMail, String ime, String prezime, String adresa,
			String brTel, LocalDate datumRodjenja, LocalDateTime datumReg, boolean administrator, boolean blokiran, boolean loyaltyKartica) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.eMail = eMail;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brTel = brTel;
		this.datumRodjenja = datumRodjenja;
		this.datumReg = datumReg;
		this.administrator = administrator;
		this.blokiran = blokiran;
		this.loyaltyKartica = loyaltyKartica;
	}
	
	
	
	
	
	public boolean isLoyaltyKartica() {
		return loyaltyKartica;
	}

	public void setLoyaltyKartica(boolean loyaltyKartica) {
		this.loyaltyKartica = loyaltyKartica;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public boolean isBlokiran() {
		return blokiran;
	}

	public void setBlokiran(boolean blokiran) {
		this.blokiran = blokiran;
	}

	public List<String> getListaZelja() {
		return listaZelja;
	}

	public void setListaZelja(List<String> listaZelja) {
		this.listaZelja.clear();
		this.listaZelja.addAll(listaZelja);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result + ((korisnickoIme == null) ? 0 : korisnickoIme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Korisnik other = (Korisnik) obj;
		if (korisnickoIme == null) {
			if (other.korisnickoIme != null)
				return false;
		} else if (!korisnickoIme.equals(other.korisnickoIme))
			return false;
		return true;
	}
	
	
	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	
	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getEMail() {
		return eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBrTel() {
		return brTel;
	}

	public void setBrTel(String brTel) {
		this.brTel = brTel;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public LocalDateTime getDatumReg() {
		return datumReg;
	}

	public void setDatumReg(LocalDateTime datumReg) {
		this.datumReg = datumReg;
	}

	
	
	@Override
	public String toString() {
		return "Korisnik [korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + "]";
	}

}
