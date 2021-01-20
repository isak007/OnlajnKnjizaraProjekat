package com.ftn.KnjizaraProjekat.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SecondConfiguration implements WebMvcConfigurer{

	@Bean
    public ConnectionManager connectionManager() {
		System.out.println("Konekcija za bazu pozvana!");
		return new ConnectionManager();
	}
	
	public final class ConnectionManager {
		
		@Value("${spring.datasource.driverClassName}")
		private String driverClassName;
		
		@Value("${spring.datasource.url}")
		private String url; 
		
		@Value("${spring.datasource.username}")
		private String username; 
		
		@Value("${spring.datasource.password}")
		private String password; 
		
		private ConnectionManager() {
			super();
		}
		
		private Connection conn = null;	
	    
		public Connection getConnection() {
			if (conn == null) {
				// ucitavanje MySQL drajvera
				try {
					Class.forName(driverClassName);
					// konekcija
					conn = DriverManager.getConnection(
							url, username, password);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return conn;
		}
		
		public void closeConnection(){
			try {
				if(conn != null)
					conn.close();
					conn=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
