package com.ftn.KnjizaraProjekat.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.KupljenaKnjiga;
import com.ftn.KnjizaraProjekat.model.Kupovina;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;
import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.KorisnikService;
import com.ftn.KnjizaraProjekat.service.KupovinaService;

@Controller
@RequestMapping(value="/Kupovina")
public class KupovinaController implements ServletContextAware {

	
	@Autowired
	private KnjigaService knjigaService;
	
	@Autowired
	private KorisnikService korisnikService;

	@Autowired
	private KupovinaService kupovinaService;
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 

	@PostConstruct
	public void init() {
		baseURL = servletContext.getContextPath() + "/";
	}
	

	@GetMapping(value="/Details")
	public ModelAndView details(@RequestParam Long id, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		// čitanje
		Kupovina kupovina = kupovinaService.findOne(id);
		if (kupovina == null) {
			response.sendRedirect(baseURL);
		}
		
		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("kupovina");
		rezultat.addObject("kupovina", kupovina);
		rezultat.addObject("kupljeneKnjige", kupovina.getListaKupljenihKnjiga());

		return rezultat;
	}


	@PostMapping(value="/Create")
	public void create(@RequestParam(required=false) Integer brojPoena,
			HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null) {
			response.sendRedirect(baseURL + "Korisnici/Login");
			return;
		}
		else if (prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Knjige");
			return;
		}
		
		// kreiranje
		HashMap<Knjiga, Integer> korpa = (HashMap<Knjiga, Integer>) servletContext.getAttribute(KorpaController.KORPA_KEY);
		double ukupnaCena = 0;
		for (Knjiga k : korpa.keySet()) {
			// na ukupnu cenu se dodaje cena knjige * broj primeraka za svaku knjigu u korpi
			ukupnaCena += k.getCena() * (korpa.get(k));
		}
		
		LoyaltyKartica LK = korisnikService.findLoyaltyKartica(prijavljeniKorisnik.getKorisnickoIme());
		
		if (LK != null) {
			// korigovanje ukupne cene i uklanjanje bodova sa loyalty kartice
			if (brojPoena != null && brojPoena > 0) {
				ukupnaCena -= (ukupnaCena * 5/100) * brojPoena;
				LK.setBrojPoena(LK.getBrojPoena()-brojPoena);
				LK.setPopust(LK.getPopust()-(brojPoena*5));
			}
			// dodavanje 1 boda za svakih 1000din
			int bodoviZaDodavanje = (int)ukupnaCena / 1000;
			if (bodoviZaDodavanje > 0)
				LK.setBrojPoena(LK.getBrojPoena()+bodoviZaDodavanje);
				LK.setPopust(LK.getPopust()+(bodoviZaDodavanje*5));	
			korisnikService.updateLK(LK);
		}
		
		
		
		if (korpa.size()>0) {
			Kupovina kupovina = new Kupovina(ukupnaCena, LocalDateTime.now(), prijavljeniKorisnik, 0);
			kupovinaService.save(kupovina);
			Kupovina kupovinaSaId = kupovinaService.findOne(prijavljeniKorisnik.getKorisnickoIme());
			int brojKupljenihKnjiga = 0;
			for (Knjiga k : korpa.keySet()) {
				brojKupljenihKnjiga += korpa.get(k);
				KupljenaKnjiga kk = new KupljenaKnjiga(k, kupovinaSaId.getId(), korpa.get(k), k.getCena()*korpa.get(k));
				kupovinaService.saveKupljenaKnjiga(kk);
				kupovinaSaId.getListaKupljenihKnjiga().add(kk);
			}
			kupovinaSaId.setBrojKupljenihKnjiga(brojKupljenihKnjiga);
			kupovinaService.update(kupovinaSaId);
			korpa.clear();
		}
		
		response.sendRedirect(baseURL + "Knjige");
	}


	
	@PostMapping(value="/Porucivanje")
	public void add(@RequestParam String isbn, @RequestParam int brPrimerakaZaPorucivanje,
			HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Knjige");
			return;
		}

		int brojPrimeraka = knjigaService.findBrPrimeraka(isbn);
		// ako postoji broj primeraka za knjigu u bazi dodaje se broj porucenih na postojeci
		if (brojPrimeraka != -1) {
			knjigaService.updatePrimerke(isbn, brojPrimeraka + brPrimerakaZaPorucivanje);
		}
		
		// u suprotnom se kreira u bazi 
		else {
			knjigaService.savePrimerke(isbn, brPrimerakaZaPorucivanje);
		}
	
		response.sendRedirect(baseURL + "Knjige");
	}

}
