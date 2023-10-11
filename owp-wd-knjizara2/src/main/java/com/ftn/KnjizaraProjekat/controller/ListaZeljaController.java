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

import com.ftn.KnjizaraProjekat.service.KnjigaService;
import com.ftn.KnjizaraProjekat.service.KorisnikService;
import com.ftn.KnjizaraProjekat.model.Knjiga;
import com.ftn.KnjizaraProjekat.model.Korisnik;
import com.ftn.KnjizaraProjekat.model.ListaZelja;

@Controller
@RequestMapping(value="/ListaZelja")
public class ListaZeljaController implements ServletContextAware {
	
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
	public ModelAndView index(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (korisnik == null || korisnik.isAdministrator()) {
			response.sendRedirect(baseURL);
			return null;
		}
		
		List<Knjiga> knjige = new ArrayList<Knjiga>();
		if (korisnik.getListaZelja() != null && korisnik.getListaZelja().getListaKnjiga() != null) {
			knjige = korisnik.getListaZelja().getListaKnjiga();
		}

		ModelAndView rezultat = new ModelAndView("listaZelja");
		rezultat.addObject("knjige", knjige);
		return rezultat;
	}
	
	@PostMapping(value="/Add")
	public void add(@RequestParam String isbn, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (korisnik == null || korisnik.isAdministrator()) {
			response.sendRedirect(baseURL);
			return;
		}
					
		korisnikService.updateListaZelja(korisnik,isbn);
		
		System.out.println(korisnik.getListaZelja());
		response.sendRedirect(baseURL);
	}
	
	
	@GetMapping(value="/Remove")
	public void remove(@RequestParam String isbn, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		
		if (korisnik == null || korisnik.isAdministrator()) {
			response.sendRedirect(baseURL);
			return;
		}
		
		ListaZelja listaZelja = korisnik.getListaZelja();

		if (listaZelja.getListaKnjiga().size() == 0) {
			response.sendRedirect(baseURL);
			System.out.println("yo wtf");

			return;
		}

		
		Knjiga knjigaZaUklanjanje = null;
		for (Knjiga knjiga : listaZelja.getListaKnjiga()) {
			if (knjiga.getISBN().equals(isbn)){
				knjigaZaUklanjanje = knjiga;
				break;
			}
		}
		
		if (knjigaZaUklanjanje != null) {
			listaZelja.getListaKnjiga().remove(knjigaZaUklanjanje);
			korisnik.setListaZelja(listaZelja);
			korisnikService.deleteFromListaZelja(korisnik, knjigaZaUklanjanje.getISBN());
		}
		response.sendRedirect(baseURL + "/");
	}
	
	
}

