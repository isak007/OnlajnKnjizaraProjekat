package com.ftn.KnjizaraProjekat.model;

public class LoyaltyKartica {
	private int popust;
	private int brojPoena;
	private String kupacId;
	
	public LoyaltyKartica(){}

	public LoyaltyKartica(int popust, int brojPoena, String kupacId) {
		super();
		this.popust = popust;
		this.brojPoena = brojPoena;
		this.kupacId = kupacId;
	}

	
	
	public String getKupacId() {
		return kupacId;
	}

	public void setKupacId(String kupacId) {
		this.kupacId = kupacId;
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
	
	
}
