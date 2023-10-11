package com.ftn.KnjizaraProjekat.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;



@Entity
public class ListaZelja {
	
	@Id @Column(name="korisnicko_ime") String korisnicko_ime ;

    @MapsId 
    @OneToOne(mappedBy = "listaZelja")
    @JoinColumn(name = "korisnicko_ime")   //same name as id @Column
    private Korisnik korisnik;
    
    @ManyToMany
    private List<Knjiga> knjiga = new ArrayList<>();
	
	@Column
	private int brojKnjiga;
	
	public ListaZelja() {}

	public ListaZelja(List<Knjiga> listaKnjiga, int brojKnjiga) {
		super();
		this.knjiga = listaKnjiga;
		this.brojKnjiga = brojKnjiga;
	}

	public List<Knjiga> getListaKnjiga() {
		return knjiga;
	}

	public void setListaKnjiga(List<Knjiga> listaKnjiga) {
		this.knjiga = listaKnjiga;
	}

	public int getBrojKnjiga() {
		return brojKnjiga;
	}

	public void setBrojKnjiga(int brojKnjiga) {
		this.brojKnjiga = brojKnjiga;
	}
	
}
