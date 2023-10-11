package com.ftn.KnjizaraProjekat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Autowired;

import com.ftn.KnjizaraProjekat.service.KorisnikService;

@Entity
public class LoyaltyKartica {
	
	@Id @Column(name="korisnik_id") String korisnik_id ;

    @MapsId 
    @OneToOne(mappedBy = "loyaltyKartica")
    @JoinColumn(name = "korisnik_id", nullable = false)   //same name as id @Column
	private Korisnik korisnik;
	
	@Column
	private int popust;
	
	@Column
	private int brojPoena;

	
	public LoyaltyKartica(){}

	public LoyaltyKartica(int popust, int brojPoena, Korisnik korisnik) {
		super();
		this.popust = popust;
		this.brojPoena = brojPoena;
		this.korisnik = korisnik;
	}

	public int getPopust() {
		return popust;
	}

	public void setPopust(int popust) {
		this.popust = popust;
	}

	public int getBrojPoena() {
		return brojPoena;
	}

	public void setBrojPoena(int brojPoena) {
		this.brojPoena = brojPoena;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}


	
	
}
