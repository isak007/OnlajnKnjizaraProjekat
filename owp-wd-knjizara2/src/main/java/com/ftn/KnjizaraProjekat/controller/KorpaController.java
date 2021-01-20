package com.ftn.KnjizaraProjekat.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.KorisnikService;
import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;

@Controller
@RequestMapping(value="/Korpa")
public class KorpaController implements ServletContextAware {
	public static final String KORPA_KEY = "korpa";
	public static final String LISTA_ZELJA_KEY = "listaZelja";
	
	@Autowired
	private KnjigaService knjigaService;
	
	@Autowired
	private KorisnikService korisnikService;
	
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
	
	
	@GetMapping
	public ModelAndView index(@RequestParam(required=false) Integer brojPoena,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (korisnik != null && korisnik.isAdministrator()) {
			response.sendRedirect(baseURL);
			return null;
		}
		
		HashMap<Knjiga, Integer> korpa = (HashMap<Knjiga, Integer>) servletContext.getAttribute(KorpaController.KORPA_KEY);
		
		double ukupnaCena = 0;
		for (Knjiga k : korpa.keySet()) {
			// na ukupnu cenu se dodaje cena knjige * broj primeraka za svaku knjigu u korpi
			ukupnaCena += k.getCena() * (korpa.get(k));
		}
		
		if (brojPoena != null && brojPoena > 0) {
			ukupnaCena -= (ukupnaCena * 5/100) * brojPoena;
		}
		
		ModelAndView rezultat = new ModelAndView("korpa");
		if (korisnik != null && korisnik.isLoyaltyKartica()) {
			LoyaltyKartica lk = korisnikService.findLoyaltyKartica(korisnik.getKorisnickoIme());
			rezultat.addObject("loyaltyKartica",lk);
		}
		rezultat.addObject("knjige", korpa);
		rezultat.addObject("ukupnaCena", ukupnaCena);
		return rezultat;
	}
	
	@GetMapping(value="/Add")
	@SuppressWarnings("unchecked")
	public void add(@RequestParam String isbn, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// provera da li ima na stanju
		if (knjigaService.findBrPrimeraka(isbn) < 1) {
			response.sendRedirect(baseURL);
			return;
		}
		
		Knjiga knjigaZaDodavanje = knjigaService.findOne(isbn);
		HashMap<Knjiga, Integer> korpa = (HashMap<Knjiga, Integer>) servletContext.getAttribute(KorpaController.KORPA_KEY);
		
		boolean postoji = false; 
		for (Knjiga k : korpa.keySet()) {
			if (knjigaZaDodavanje.getISBN().equals(k.getISBN())){
				postoji = true;
				korpa.replace(k, korpa.get(k)+1);
				break;
			}
		}
		if (!postoji) {
			korpa.put(knjigaZaDodavanje, 1);
		}
		
		System.out.println(korpa);
		servletContext.setAttribute(KorpaController.KORPA_KEY, korpa);
		response.sendRedirect(baseURL);
	}
	
	
	
	@GetMapping(value="/Remove")
	@SuppressWarnings("unchecked")
	public void remove(@RequestParam String isbn, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Knjiga knjigaZaUklanjanje = knjigaService.findOne(isbn);
		
		HashMap<Knjiga, Integer> korpa = (HashMap<Knjiga, Integer>) servletContext.getAttribute(KorpaController.KORPA_KEY);
		
		for (Knjiga k : korpa.keySet()) {
			if (knjigaZaUklanjanje.getISBN().equals(k.getISBN())){
				korpa.remove(k);
				break;
			}
		}

		System.out.println(korpa);
		servletContext.setAttribute(KorpaController.KORPA_KEY, korpa);
		response.sendRedirect(baseURL + "/Korpa");
	}
	
	
	@GetMapping(value="/Update")
	@SuppressWarnings("unchecked")
	public void update(@RequestParam(required=false) String knjigaISBN, @RequestParam(required=false) Integer brojKnjiga, @RequestParam(required=false) Integer brojPoena,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (knjigaISBN != null && brojKnjiga != null) {
			Knjiga knjigaZaIzmenu = knjigaService.findOne(knjigaISBN);
			
			HashMap<Knjiga, Integer> korpa = (HashMap<Knjiga, Integer>) servletContext.getAttribute(KorpaController.KORPA_KEY);
			
			for (Knjiga k : korpa.keySet()) {
				if (knjigaZaIzmenu.getISBN().equals(k.getISBN())){
					korpa.remove(k);
					korpa.put(k, brojKnjiga);
					break;
				}
			}
			servletContext.setAttribute(KorpaController.KORPA_KEY, korpa);
		}
		if (brojPoena != null) {
			response.sendRedirect(baseURL + "/Korpa?brojPoena="+brojPoena);
		}
		else {
			response.sendRedirect(baseURL + "/Korpa");
		}
	}
	
	
}

