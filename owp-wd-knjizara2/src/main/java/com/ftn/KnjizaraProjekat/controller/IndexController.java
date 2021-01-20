package com.ftn.KnjizaraProjekat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.Zanr;
import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.ZanrService;

@Controller
@RequestMapping(value="/")
public class IndexController {

	@Autowired
	private KnjigaService knjigaService;

	@Autowired
	private ZanrService zanrService;
	
	@GetMapping
	public ModelAndView index(HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		
		List<Knjiga> sveKnjige = knjigaService.findAll();
		List<Knjiga> naStanju = new ArrayList<Knjiga>();
		// provera broja primeraka
		for (Knjiga k : sveKnjige) {
			if (knjigaService.findBrPrimeraka(k.getISBN()) > 0) {
				naStanju.add(k);
			}
		}
		
		List<Zanr> zanrovi = zanrService.findAll();
		
		// prosleđivanje
		ModelAndView rezultat = new ModelAndView("index");
		if (korisnik != null && korisnik.isAdministrator()) {
			rezultat.addObject("knjige", sveKnjige);
		}
		else {
			rezultat.addObject("knjige", naStanju);
		}
		rezultat.addObject("zanrovi", zanrovi);

		return rezultat;
		
	}

}
