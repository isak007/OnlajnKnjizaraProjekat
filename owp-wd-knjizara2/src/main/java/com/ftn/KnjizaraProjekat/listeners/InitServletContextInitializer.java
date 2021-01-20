package com.ftn.KnjizaraProjekat.listeners;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import com.ftn.KnjizaraProjekat.controller.KorpaController;
import com.ftn.KnjizaraProjekat.model.Knjiga;


@Component
public final class InitServletContextInitializer implements ServletContextInitializer {
 
	/** kod koji se izvrsava po pokretanju aplikacije kada je ServletContext kreiran */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	System.out.println("Inicijalizacija konteksta pri ServletContextInitializer...");
    	
    	HashMap<Knjiga, Integer>  praznaKorpa = new HashMap<Knjiga,Integer>();
    	servletContext.setAttribute(KorpaController.KORPA_KEY, praznaKorpa);
    	
    	System.out.println("Uspeh ServletContextInitializer!");
    }

}