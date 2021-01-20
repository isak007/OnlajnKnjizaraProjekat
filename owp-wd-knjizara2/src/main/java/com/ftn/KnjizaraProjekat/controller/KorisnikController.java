package com.ftn.KnjizaraProjekat.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.LoyaltyKartica;
import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.KorisnikService;
import com.ftn.KnjizaraProjekat.service.KupovinaService;

@Controller
@RequestMapping(value="/Korisnici")
public class KorisnikController {

	public static final String KORISNIK_KEY = "prijavljeniKorisnik";
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private KnjigaService knjigaService;
	
	@Autowired
	private KupovinaService kupovinaService;
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL; 

	@PostConstruct
	public void init() {	
		baseURL = servletContext.getContextPath() + "/";			
	}

	@GetMapping
	public ModelAndView index(
			@RequestParam(required=false) String korisnickoIme,
			@RequestParam(required=false) String lozinka,
			@RequestParam(required=false) String eMail,
			@RequestParam(required=false) String ime,
			@RequestParam(required=false) String prezime,
			@RequestParam(required=false) String adresa,
			@RequestParam(required=false) String brTel,
			@RequestParam(required=false) String datRodj,
			@RequestParam(required=false) String datReg, 
			@RequestParam(required=false) Boolean administrator,
			@RequestParam(required=false) Boolean blokiran,
			HttpSession session, HttpServletResponse response) throws IOException {		
		// autentikacija, autorzacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL);
			return null;
		}

		//ako je input tipa text i ništa se ne unese 
		//a parametar metode String onda će vrednost parametra handeler metode biti "" što nije null
		
		if(korisnickoIme!=null && korisnickoIme.trim().equals(""))
			korisnickoIme=null;
		
		if(lozinka!=null && lozinka.trim().equals(""))
			lozinka=null;
		
		if(eMail!=null && eMail.trim().equals(""))
			eMail=null;
		
		if(ime!=null && ime.trim().equals(""))
			ime=null;
		
		if(prezime!=null && prezime.trim().equals(""))
			prezime=null;
		
		if(adresa!=null && adresa.trim().equals(""))
			adresa=null;
		
		if(brTel!=null && brTel.trim().equals(""))
			brTel=null;
		
		LocalDate datRodjParsed = null;
		if (datRodj!=null && !datRodj.equals("")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			datRodjParsed = LocalDate.parse(datRodj, formatter);
		}
		
		LocalDateTime datRegParsed = null;
		if (datReg!=null && !datReg.equals("")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			datRegParsed = LocalDateTime.parse(datReg, formatter);
		}
		
		
		
		
		// čitanje
		List<Korisnik> korisnici = korisnikService.find(korisnickoIme, lozinka, eMail, ime, prezime, adresa,
				brTel, datRodjParsed, datRegParsed, administrator, blokiran);

		Map<Korisnik, Boolean> korisnikZahtev = new HashMap<Korisnik, Boolean> ();
		for (Korisnik korisnik : korisnici) {
			String zahtevZaLK = korisnikService.findZahtevZaLK(korisnik.getKorisnickoIme());
			if (zahtevZaLK != null){
				korisnikZahtev.put(korisnik, true);
			}
			else {
				korisnikZahtev.put(korisnik, false);
			}
		}
		
		
		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("korisnici");
		rezultat.addObject("korisnici", korisnikZahtev);

		System.out.println(korisnici);
		
		return rezultat;
	}
	
	
	

	@GetMapping(value="/Details")
	public ModelAndView details(@RequestParam String korisnickoIme,
			HttpSession session, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da vidi druge korisnike; svaki korisnik može da vidi sebe
		if (prijavljeniKorisnik == null || (!prijavljeniKorisnik.isAdministrator() && !prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme))) {
			response.sendRedirect(baseURL);
			return null;
		}

		// validacija
		Korisnik korisnik = korisnikService.findOne(korisnickoIme);

		if (korisnik == null) {
			response.sendRedirect(baseURL);
			return null;
		}
		
		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("korisnik");
		if (korisnik != null && !korisnik.isAdministrator()) {
			if (korisnikService.findZahtevZaLK(korisnickoIme) != null) {
				rezultat.addObject("zahtevPodnesen", true);
			}
		}
		if (!kupovinaService.findAll(korisnickoIme).isEmpty()) {
			rezultat.addObject("kupovine", kupovinaService.findAll(korisnickoIme));
		}
		
		if (korisnik != null && korisnik.getListaZelja().size() > 0) {
			List<Knjiga> listaZeljaObj = new ArrayList<Knjiga>();
			korisnik.getListaZelja().forEach(isbn -> listaZeljaObj.add(knjigaService.findOne(isbn)));
			rezultat.addObject("listaZelja", listaZeljaObj);
		}
		
		rezultat.addObject("korisnik", korisnik);

		return rezultat;
	}
	
	
	

	@GetMapping(value="/Create")
	public String create(HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da kreira korisnike
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Korisnici");
			return "korisnici";
		}

		return "dodavanjeKorisnika";
	}
	
	@PostMapping(value="/Create")
	public void create(@RequestParam String korisnickoIme, @RequestParam String eMail, 
			@RequestParam String ime, @RequestParam String prezime, @RequestParam String adresa,
			@RequestParam String brTel, @RequestParam String datum, 
			@RequestParam String lozinka, @RequestParam String ponovljenaLozinka,
			HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da kreira korisnike
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Korisnici");
			return;
		}

		// validacija
		Korisnik postojeciKorisnik = korisnikService.findOne(korisnickoIme);
		if (postojeciKorisnik != null) {
			response.sendRedirect(baseURL + "Korisnici/Create");
			return;
		}
		if (korisnickoIme.equals("") || lozinka.equals("")) {
			response.sendRedirect(baseURL + "Korisnici/Create");
			return;
		}
		if (eMail.equals("")) {
			response.sendRedirect(baseURL + "Korisnici/Create");
			return;
		}

		LocalDate datumParsed = null;
		if (datum.equals("")) {
			response.sendRedirect(baseURL + "Korisnici/Create");
			return;
		}	
		else{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			datumParsed = LocalDate.parse(datum, formatter);
		}
		
		// kreiranje
		Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datumParsed);
		korisnikService.save(korisnik);

		response.sendRedirect(baseURL + "Korisnici");
	}
	
	
	
	@GetMapping(value="/Update")
	public void update(@RequestParam(required=false) String korisnickoIme, @RequestParam(required=false) String opcija,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da kreira korisnike
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL);
			return;
		}
		
		if (korisnickoIme != null && opcija != null) {
			if (opcija.equals("prihvati")) {
				// upisivanje da korisnik poseduje Loyalty Karticu
				Korisnik korisnik = korisnikService.findOne(korisnickoIme);
				korisnik.setLoyaltyKartica(true);
				// dodavanje nove Loyalty Kartice sa 20% popusta za korisnika
				LoyaltyKartica LK = new LoyaltyKartica(20, 4, korisnickoIme);
				korisnikService.saveLK(LK);
				korisnikService.update(korisnik);
			}
			korisnikService.deleteZahtevZaLK(korisnickoIme);
		}
		response.sendRedirect(baseURL + "Korisnici");
	}
	
	
	

	@PostMapping(value="/Edit")
	public void edit(@RequestParam(required=false) String korisnickoIme, @RequestParam(required=false) String uloga, @RequestParam String button,
			HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da menja druge korisnike; svaki korisnik može da menja sebe
		if (prijavljeniKorisnik == null) {
			response.sendRedirect(baseURL);
			return;
		}

		// validacija
		Korisnik korisnik = korisnikService.findOne(korisnickoIme);
		if (korisnik == null) {
			response.sendRedirect(baseURL);
			return;
		}

		// privilegije može menjati samo administrator i to drugim korisnicima
		if (prijavljeniKorisnik.isAdministrator() && !prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme)) {
			if (button.equals("izmeniUlogu")) {
				if (uloga.equals("admin")) {
					korisnik.setAdministrator(true);
				}
				else {
					korisnik.setAdministrator(false);
				}
				korisnikService.update(korisnik);
			}
			
			else if (button.equals("blokiraj")) {
				korisnik.setBlokiran(true);
				korisnikService.update(korisnik);
			}
			
			else if (button.equals("odblokiraj")) {
				korisnik.setBlokiran(false);
				korisnikService.update(korisnik);
			}
		}

		if (!prijavljeniKorisnik.isAdministrator() && prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme)) {
			if (button.equals("zahtevZaLK")) {
				korisnikService.saveZahtevZaLK(prijavljeniKorisnik.getKorisnickoIme());
			}
		}
		
		// sigurnost
		if (!prijavljeniKorisnik.equals(korisnik)) {
			// TODO odjaviti korisnika
		}

		if (prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Korisnici/Details?korisnickoIme=" + korisnik.getKorisnickoIme());
		} 
		else {
			response.sendRedirect(baseURL);
		}
	}
	

	/*
	@PostMapping(value="/Delete")
	public void delete(@RequestParam String korisnickoIme, 
			HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da briše korisnike, ali ne i sebe
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator() || prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme)) {
			response.sendRedirect(baseURL + "Korisnici");
			return;
		}

		// brisanje
		korisnikService.delete(korisnickoIme);

		// sigurnost
		// TODO odjaviti korisnika
		
		response.sendRedirect(baseURL + "Korisnici");
	}
	*/
	
	@GetMapping(value="/Register")
	public ModelAndView register(@RequestParam(required=false) String korisnickoIme, @RequestParam(required=false) String eMail, 
			@RequestParam(required=false) String ime, @RequestParam(required=false) String prezime, @RequestParam(required=false) String adresa,
			@RequestParam(required=false) String brTel, @RequestParam(required=false) String datum, 
			HttpSession session, HttpServletResponse response) throws IOException {
		
		ModelAndView rezultat = new ModelAndView("registracija");

		return rezultat;
	}

	@PostMapping(value="/Register")
	public ModelAndView register(@RequestParam String korisnickoIme, @RequestParam String eMail, 
			@RequestParam String ime, @RequestParam String prezime, @RequestParam String adresa,
			@RequestParam String brTel, @RequestParam String datum, 
			@RequestParam String lozinka, @RequestParam String ponovljenaLozinka,
			HttpSession session, HttpServletResponse response) throws IOException {
		try {
			// validacija
			Korisnik postojeciKorisnik = korisnikService.findOne(korisnickoIme);
			
			System.out.println(datum.toString());
			
			if (postojeciKorisnik != null) {
				throw new Exception("Korisničko ime već postoji!");
			}
			if (korisnickoIme.equals("") || lozinka.equals("")) {
				throw new Exception("Korisničko ime i lozinka ne smeju biti prazni!");
			}
			if (!lozinka.equals(ponovljenaLozinka)) {
				throw new Exception("Lozinke se ne podudaraju!");
			}
			if (eMail.equals("")) {
				throw new Exception("E-mail ne sme biti prazan!");
			}
			if (ime.equals("")) {
				throw new Exception("Ime ne sme biti prazan!");
			}
			if (prezime.equals("")) {
				throw new Exception("Prezime ne sme biti prazan!");
			}
			if (adresa.equals("")) {
				throw new Exception("Adresa ne sme biti prazan!");
			}
			if (brTel.equals("")) {
				throw new Exception("Broj ne sme biti prazan!");
			}
			
			LocalDate datumParsed = null;
			if (datum.equals("")) {
				throw new Exception("Datum ne sme biti prazan!");
			}	
			else{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				datumParsed = LocalDate.parse(datum, formatter);
				if (!datumParsed.isBefore(LocalDate.now())) {
					throw new Exception("Datum mora biti u proslosti!");
				}
			}
			if (lozinka.equals("") || ponovljenaLozinka.equals("")) {
				throw new Exception("Lozinka ne sme biti prazna!");
			}

			// registracija
			Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datumParsed, LocalDateTime.now());
			korisnikService.save(korisnik);

			response.sendRedirect(baseURL + "Korisnici/Login");
			return null;
		}
		
		catch (Exception ex) {
			// ispis greške
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešna registracija!";
			}

			// prosleđivanje
			ModelAndView rezultat = new ModelAndView("registracija");
			rezultat.addObject("poruka", poruka);

			return rezultat;
		}
	}
	
	@GetMapping(value="/Login")
	public String login(HttpSession session, HttpServletResponse response) throws IOException {
		return "prijava";
	}
	
	@PostMapping(value="/Login")
	public ModelAndView postLogin(@RequestParam String korisnickoIme, @RequestParam String lozinka, 
			HttpSession session, HttpServletResponse response) throws IOException {
		try {
			// validacija
			Korisnik korisnik = korisnikService.findOne(korisnickoIme, lozinka);
			if (korisnik == null || korisnik.isBlokiran()) {
				throw new Exception("Neispravno korisničko ime ili lozinka!");
			}			
			else {
				if (korisnik.isAdministrator()) {
					// praznjenje korpe ukoliko je korisnik administrator,
					// odnosno pravljene nove prazne mape
					servletContext.setAttribute(KorpaController.KORPA_KEY, new HashMap<Knjiga,Integer>());
				}
			}
			
			// prijava
			session.setAttribute(KorisnikController.KORISNIK_KEY, korisnik);
			
			response.sendRedirect(baseURL);
			return null;
		}
		
		catch (Exception ex) {
			// ispis greške
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešna prijava!";
			}
			
			// prosleđivanje
			ModelAndView rezultat = new ModelAndView("prijava");
			rezultat.addObject("poruka", poruka);

			return rezultat;
		}
	}
	
	@GetMapping(value="/Logout")
	public void logout(HttpSession session, HttpServletResponse response) throws IOException {
		// odjava	
		session.invalidate();
		
		response.sendRedirect(baseURL);
	}
	
}
