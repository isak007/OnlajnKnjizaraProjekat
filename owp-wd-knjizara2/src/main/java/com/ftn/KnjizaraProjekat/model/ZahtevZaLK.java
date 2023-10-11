package com.ftn.KnjizaraProjekat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;



@Entity
public class ZahtevZaLK {
	
	@Id @Column(name="korisnicko_ime") String korisnicko_ime ;

    @MapsId 
    @OneToOne(mappedBy = "zahtevZaLK")
    @JoinColumn(name = "korisnicko_ime")   //same name as id @Column
    private Korisnik korisnik;
	
	public ZahtevZaLK() {}

	public ZahtevZaLK(Korisnik korisnik) {
		super();
		this.korisnik = korisnik;
	}

	public Korisnik getKorisnika() {
		return this.korisnik;
	}

	public void setKorisnika(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	
}
