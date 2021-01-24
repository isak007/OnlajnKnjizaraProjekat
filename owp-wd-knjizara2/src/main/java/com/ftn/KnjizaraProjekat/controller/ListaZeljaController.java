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
		
		List<Knjiga> listaZelja = new ArrayList<Knjiga>();
		for (String knjigaISBN : korisnik.getListaZelja()) {
			listaZelja.add(knjigaService.findOne(knjigaISBN));
		}
				
		if (listaZelja.isEmpty()) {
			response.sendRedirect(baseURL);
			return null;
		}
		
		ModelAndView rezultat = new ModelAndView("listaZelja");
		rezultat.addObject("listaZelja", listaZelja);
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
		
		List<String> listaZelja = korisnik.getListaZelja();
			
		boolean postoji = false; 
		if (listaZelja != null) {
			for (String knjigaISBN : listaZelja) {
				if (knjigaISBN.equals(isbn)){
					postoji = true;
					break;
				}
			}
		}

		if (!postoji) {
			korisnik.getListaZelja().add(isbn);	
			korisnikService.update(korisnik);
		}
		
		
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
		
		List<String> listaZelja = new ArrayList<String>();
		for (String knjigaISBN : korisnik.getListaZelja()) {
			listaZelja.add(knjigaISBN);
		}
		
		if (listaZelja.isEmpty()) {
			response.sendRedirect(baseURL);
			return;
		}
		
		for (String knjigaISBN : listaZelja) {
			if (knjigaISBN.equals(isbn)){
				listaZelja.remove(knjigaISBN);
				break;
			}
		}
		
		System.out.println(listaZelja);
		korisnik.setListaZelja(listaZelja);
		korisnikService.update(korisnik);
		response.sendRedirect(baseURL + "Korisnici/Details?korisnickoIme=" + korisnik.getKorisnickoIme());
	}
	
	
}

