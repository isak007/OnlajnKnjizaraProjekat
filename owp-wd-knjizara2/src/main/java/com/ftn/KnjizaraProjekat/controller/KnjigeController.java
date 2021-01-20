package com.ftn.KnjizaraProjekat.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.ftn.KnjizaraProjekat.model.Zanr;
import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.ZanrService;

@Controller
@RequestMapping(value="/Knjige")
public class KnjigeController implements ServletContextAware {

	
	@Autowired
	private KnjigaService knjigaService;

	@Autowired
	private ZanrService zanrService;
	
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
	public ModelAndView index(
			@RequestParam(required=false) String isbn, 
			@RequestParam(required=false) String naziv, 
			@RequestParam(required=false) Long zanrId, 
			@RequestParam(required=false) Double cenaMin, 
			@RequestParam(required=false) Double cenaMax, 
			@RequestParam(required=false) String autor,
			@RequestParam(required=false) String jezik,
			@RequestParam(required=false) String sortiranje,
			@RequestParam(required=false) String poredak,
			HttpServletResponse response, HttpSession session)  throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		//ako je input tipa text i ništa se ne unese 
		//a parametar metode Sting onda će vrednost parametra handeler metode biti "" što nije null
		if(isbn!=null && isbn.trim().equals(""))
			isbn=null;
		
		if(naziv!=null && naziv.trim().equals(""))
			naziv=null;
		
		if(cenaMin!=null && cenaMin.toString().trim().equals(""))
			cenaMin=null;
		
		if(cenaMax!=null && cenaMax.toString().trim().equals(""))
			cenaMax=null;
		
		if(autor!=null && autor.trim().equals(""))
			autor=null;
		
		if(jezik!=null && jezik.trim().equals(""))
			jezik=null;
		
		if(sortiranje!=null && sortiranje.trim().equals(""))
			sortiranje=null;
		
		if(poredak!=null && poredak.trim().equals(""))
			poredak=null;
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		
		// čitanje (pretraga)
		List<Knjiga> pronadjeneKnjige = knjigaService.find(isbn,naziv,zanrId,cenaMin,cenaMax,autor,jezik,sortiranje,poredak);
		List<Knjiga> naStanju = new ArrayList<Knjiga>();
		// provera broja primeraka
		for (Knjiga k : pronadjeneKnjige) {
			if (knjigaService.findBrPrimeraka(k.getISBN()) > 0) {
				naStanju.add(k);
			}
		}
		
		List<Zanr> zanrovi = zanrService.findAll();
		
		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("index");
		if (korisnik != null && korisnik.isAdministrator()) {
			rezultat.addObject("knjige", pronadjeneKnjige);
		}
		else {
			rezultat.addObject("knjige", naStanju);
		}
		rezultat.addObject("zanrovi", zanrovi);

		return rezultat;
	}

	@GetMapping(value="/Details")
	public ModelAndView details(@RequestParam String isbn, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		// čitanje
		Knjiga knjiga = knjigaService.findOne(isbn);
		
		int brojPrimeraka = knjigaService.findBrPrimeraka(isbn);
		// ako je brojPrimeraka -1 onda ne postoje primerci u samoj bazi, 
		// ako je 0 ili vise onda postoje
		brojPrimeraka = brojPrimeraka == -1 ? 0: brojPrimeraka;
		List<Zanr> zanrovi = zanrService.findAll();
		
		// ids zanrova za pronadjeni film jer objekti zanrova knjige nisu isti objekti svih zanrova
		List<Long> knjigaZanroviIds = new ArrayList<>();
		knjiga.getZanrovi().forEach(zanr -> knjigaZanroviIds.add(zanr.getId()));
		
		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("knjiga");
		rezultat.addObject("knjiga", knjiga);
		rezultat.addObject("brojPrimeraka", brojPrimeraka);
		rezultat.addObject("knjigaZanroviIds", knjigaZanroviIds);
		rezultat.addObject("zanrovi", zanrovi);

		return rezultat;
	}

	@GetMapping(value="/Create")
	public ModelAndView create(HttpSession session, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		// autentikacija, autorizacija
		/*Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Knjige");
			return null;
		}*/

		// čitanje
		List<Zanr> zanrovi = zanrService.findAll();

		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("dodavanjeKnjige");
		rezultat.addObject("zanrovi", zanrovi);

		return rezultat;
	}

	@PostMapping(value="/Create")
	public void create(@RequestParam String isbn,@RequestParam String naziv,@RequestParam String izdavackaKuca,@RequestParam String autor,
			@RequestParam String kratakOpis, @RequestParam String jezik, @RequestParam int godinaIzdavanja, @RequestParam int brojStranica, 
			@RequestParam double cena, @RequestParam String slika, @RequestParam String tipPoveza, @RequestParam String pismo, 
			@RequestParam(name="zanrId", required=false) Long[] zanrIds,
			HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		/*Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Knjige");
			return;
		}*/
		
		if (naziv == null || naziv.equals("") || autor == null || autor.equals("") || izdavackaKuca == null || izdavackaKuca.equals("")
				 || kratakOpis == null || kratakOpis.equals("") || jezik == null || jezik.equals("")
				 || tipPoveza == null || tipPoveza.equals("") || pismo == null || pismo.equals("")
				 || godinaIzdavanja == 0 || String.valueOf(godinaIzdavanja).equals("") || brojStranica == 0 || String.valueOf(brojStranica).equals("")
				 || brojStranica == 0 || String.valueOf(brojStranica).equals("") || cena == 0 || String.valueOf(cena).equals("")
				 || slika == null || slika.equals("")) {		
			response.sendRedirect(baseURL + "Knjige/Create");
			return;
		}

		// kreiranje
		Knjiga knjiga = new Knjiga(isbn, naziv, izdavackaKuca, autor, kratakOpis, jezik,godinaIzdavanja,brojStranica, 
				cena, 0, slika, tipPoveza, pismo);
		if(zanrIds != null) knjiga.setZanrovi(zanrService.find(zanrIds));
		knjigaService.save(knjiga);

		response.sendRedirect(baseURL + "Knjige");
	}

	@PostMapping(value="/Edit")
	public void edit(@RequestParam String isbn,@RequestParam String naziv,@RequestParam String izdavackaKuca,@RequestParam String autor,
			@RequestParam String kratakOpis, @RequestParam String jezik, @RequestParam int godinaIzdavanja, @RequestParam int brojStranica, 
			@RequestParam double cena, @RequestParam double prosecnaOcena, @RequestParam String tipPoveza, @RequestParam String pismo, @RequestParam String slika,
			@RequestParam(name="zanrId", required=false) Long[] zanrIds, 
			HttpSession session, HttpServletResponse response) throws IOException {
		
		// autentikacija, autorizacija
		/*Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Knjige");
			return;
		}*/

		// validacija
		Knjiga knjiga = knjigaService.findOne(isbn);
		if (knjiga == null) {
			response.sendRedirect(baseURL + "Knjige");
			return;
		}	
		
		if (naziv == null || naziv.equals("") || autor == null || autor.equals("") || izdavackaKuca == null || izdavackaKuca.equals("")
				 || kratakOpis == null || kratakOpis.equals("") || jezik == null || jezik.equals("")
				 || tipPoveza == null || tipPoveza.equals("") || pismo == null || pismo.equals("")
				 || godinaIzdavanja == 0 || String.valueOf(godinaIzdavanja).equals("") || brojStranica == 0 || String.valueOf(brojStranica).equals("")
				 || brojStranica == 0 || String.valueOf(brojStranica).equals("") || cena == 0 || String.valueOf(cena).equals("")) {		
			response.sendRedirect(baseURL + "Knjige/Details?isbn=" + isbn);
			return;
		}
		
		System.out.println(kratakOpis);
		// izmena
		knjiga.setNaziv(naziv);
		knjiga.setIzdavackaKuca(izdavackaKuca);
		knjiga.setAutor(autor);
		knjiga.setKratakOpis(kratakOpis);
		knjiga.setJezik(jezik);
		knjiga.setGodinaIzdavanja(godinaIzdavanja);
		knjiga.setBrojStranica(brojStranica);
		knjiga.setCena(cena);
		if (slika != null && !slika.equals("")) {
			knjiga.setSlika(slika);
		}
		knjiga.setTipPoveza(tipPoveza);
		knjiga.setPismo(pismo);
		if (zanrIds != null)
			knjiga.setZanrovi(zanrService.find(zanrIds));
		
		knjigaService.update(knjiga);

		response.sendRedirect(baseURL + "Knjige");
	}

	@PostMapping(value="/Delete")
	public void delete(@RequestParam String ISBN, 
			HttpSession session, HttpServletResponse response) throws IOException {
		// autentikacija, autorizacija
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			response.sendRedirect(baseURL + "Knjige");
			return;
		}

		// brisanje
		knjigaService.delete(ISBN);
	
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
