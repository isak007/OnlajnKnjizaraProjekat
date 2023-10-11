package com.ftn.KnjizaraProjekat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;



@Entity
public class BrojPrimeraka {
	
	@Id @Column(name="knjiga_ISBN") String knjiga_ISBN ;

    @MapsId 
    @OneToOne(mappedBy = "brojPrimeraka")
    @JoinColumn(name = "knjiga_ISBN")   //same name as id @Column
    private Knjiga knjiga;
	
	@Column
	private int brojKnjiga;
	
	public BrojPrimeraka() {}

	public BrojPrimeraka(Knjiga knjiga, int brojKnjiga) {
		super();
		this.knjiga = knjiga;
		this.brojKnjiga = brojKnjiga;
	}


	public Knjiga getKnjiga() {
		return knjiga;
	}

	public void setKnjiga(Knjiga knjiga) {
		this.knjiga = knjiga;
	}

	public int getBrojKnjiga() {
		return brojKnjiga;
	}

	public void setBrojKnjiga(int brojKnjiga) {
		this.brojKnjiga = brojKnjiga;
	}
	
}
