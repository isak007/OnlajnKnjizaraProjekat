package com.ftn.KnjizaraProjekat.listeners;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;



@Component
public class InitHttpSessionListener implements HttpSessionListener {

	
	/** kod koji se izvrsava po kreiranju sesije */
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("Inicijalizacija sesisje HttpSessionListener...");

		// pri kreiranju sesije inicijalizujemo je ili radimo neke dodatne aktivnosti	
		HttpSession session  = event.getSession();
		System.out.println("Session id korisnika je "+ session.getId());
		
		System.out.println("Uspeh HttpSessionListener!");
	}
	
	/** kod koji se izvrsava po brisanju sesije */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Brisanje sesisje HttpSessionListener...");
		
		System.out.println("Uspeh HttpSessionListener!");
	}

}
