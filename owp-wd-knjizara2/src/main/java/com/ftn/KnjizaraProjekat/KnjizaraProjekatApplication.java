package com.ftn.KnjizaraProjekat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KnjizaraProjekatApplication extends SpringBootServletInitializer {
  
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KnjizaraProjekatApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(KnjizaraProjekatApplication.class, args);
	}

}
