DROP SCHEMA IF EXISTS knjizara;
CREATE SCHEMA knjizara DEFAULT CHARACTER SET utf8;
USE knjizara;

CREATE TABLE korisnici (
	korisnickoIme VARCHAR(20),
	lozinka VARCHAR(20) NOT NULL,
	eMail VARCHAR(20) NOT NULL,
	ime VARCHAR(20) NOT NULL,
	prezime VARCHAR(20) NOT NULL,
	adresa VARCHAR(20) NOT NULL,
	brTel VARCHAR(10) NOT NULL,
	datRodjenja DATE,
	datReg DATETIME,
	administrator BOOL DEFAULT false,
	blokiran BOOL DEFAULT false,
	loyaltyKartica BOOL DEFAULT false,
	PRIMARY KEY(korisnickoIme)
);

CREATE TABLE loyaltyKartica (
	popust INT NOT NULL,
	brojPoena INT NOT NULL,
	kupacId VARCHAR(20),
	PRIMARY KEY(kupacId),
	FOREIGN KEY(kupacId) REFERENCES korisnici(korisnickoIme)
		ON DELETE CASCADE
);

CREATE TABLE zahtevZaLK (
	korisnickoIme VARCHAR(20),
	PRIMARY KEY(korisnickoIme),
	FOREIGN KEY(korisnickoIme) REFERENCES korisnici(korisnickoIme)
		ON DELETE CASCADE
);

CREATE TABLE knjige (
	ISBN VARCHAR(13),
	naziv VARCHAR(20) NOT NULL,
	izdavackaKuca VARCHAR(20) NOT NULL,
	autor VARCHAR(20) NOT NULL,
	kratakOpis VARCHAR(500) NOT NULL,
	jezik VARCHAR(20) NOT NULL,
	godinaIzdavanja INT NOT NULL,
	brojStranica INT NOT NULL,
	cena DECIMAL(10, 2) NOT NULL,
	prosecnaOcena DECIMAL(10, 2) NOT NULL,
	slika VARCHAR(50) NOT NULL,
	tipPoveza ENUM('tvrdi', 'meki') DEFAULT 'tvrdi',
	pismo ENUM('latinica', 'cirilica') DEFAULT 'latinica',
	PRIMARY KEY(ISBN)
);

CREATE TABLE brojPrimeraka (
	knjigaISBN VARCHAR(13),
	brojKnjiga INT NOT NULL,
	PRIMARY KEY(knjigaISBN),
	FOREIGN KEY(knjigaISBN) REFERENCES knjige(ISBN)
		ON DELETE CASCADE
);

CREATE TABLE listaZelja (
	knjigaISBN VARCHAR(13),
	korisnickoIme VARCHAR(20),
	PRIMARY KEY(knjigaISBN, korisnickoIme),
	FOREIGN KEY(knjigaISBN) REFERENCES knjige(ISBN)
		ON DELETE CASCADE,
	FOREIGN KEY(korisnickoIme) REFERENCES korisnici(korisnickoIme)
		ON DELETE CASCADE
);

CREATE TABLE kupovine (
	id BIGINT AUTO_INCREMENT,
	ukupnaCena DECIMAL(10, 2) NOT NULL,
    datumKupovine DATETIME,
    kupacId VARCHAR(20) NOT NULL,
    brojKupljenihKnjiga INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(kupacId) REFERENCES korisnici(korisnickoIme)
		ON DELETE CASCADE
);

CREATE TABLE kupljeneKnjige (
	knjigaISBN VARCHAR(13),
	kupovinaId BIGINT,
	brojPrimeraka INT NOT NULL,
	cena DECIMAL(10, 2) NOT NULL,
	PRIMARY KEY(knjigaISBN, kupovinaId),
    FOREIGN KEY(knjigaISBN) REFERENCES knjige(ISBN)
		ON DELETE CASCADE,
	FOREIGN KEY(kupovinaId) REFERENCES kupovine(id)
		ON DELETE CASCADE
);

CREATE TABLE komentari (
	id BIGINT AUTO_INCREMENT,
	tekst VARCHAR(25) NOT NULL,
	ocena INT NOT NULL,
	datum DATETIME,
	autorId VARCHAR(20) NOT NULL,
	knjigaId VARCHAR(13) NOT NULL,
	statusKomentara ENUM('na_cekanju', 'odobren', 'nije_odobren') DEFAULT 'na_cekanju',
	PRIMARY KEY(id),
	FOREIGN KEY(autorId) REFERENCES korisnici(korisnickoIme)
		ON DELETE CASCADE,
    FOREIGN KEY(knjigaId) REFERENCES knjige(ISBN)
		ON DELETE CASCADE
);

CREATE TABLE zanrovi (
	id BIGINT AUTO_INCREMENT,
	naziv VARCHAR(25) NOT NULL,
	opis VARCHAR(25) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE knjigaZanr (
    knjigaISBN VARCHAR(13),
    zanrId BIGINT,
    PRIMARY KEY(knjigaISBN, zanrId),
    FOREIGN KEY(knjigaISBN) REFERENCES knjige(ISBN)
		ON DELETE CASCADE,
    FOREIGN KEY(zanrId) REFERENCES zanrovi(id)
		ON DELETE CASCADE
);

INSERT INTO knjige (ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik, godinaIzdavanja, brojStranica, cena, prosecnaOcena, slika, tipPoveza, pismo) 
VALUES ('0000000000000', 'Knjiga', 'IzdavackaKuca', 'Autor', 'kratakOpis', 'Jezik', 2020, 245, 50.0, 0.0, "knjiga1.jpg", 'meki', 'latinica');
INSERT INTO knjige (ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik, godinaIzdavanja, brojStranica, cena, prosecnaOcena, slika, tipPoveza, pismo) 
VALUES ('0000000000001', 'Knjiga2', 'IzdavackaKuca', 'Autor', 'josKraciOpis', 'Jezik', 2020, 520, 440.0, 0.0, "knjiga2.jpg", 'tvrdi', 'cirilica');
INSERT INTO knjige (ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik, godinaIzdavanja, brojStranica, cena, prosecnaOcena, slika, tipPoveza, pismo) 
VALUES ('0000000000002', 'Knjiga3', 'IzdavackaKuca', 'Autor', 'josKraciOdKraceg', 'Jezik', 2020, 124, 500.0, 0.0, "knjiga3.jpg", 'meki', 'latinica');
INSERT INTO knjige (ISBN, naziv, izdavackaKuca, autor, kratakOpis, jezik, godinaIzdavanja, brojStranica, cena, prosecnaOcena, slika, tipPoveza, pismo) 
VALUES ('0000000000003', 'Knjiga4', 'IzdavackaKuca', 'Autor', 'najkraciOpis', 'Jezik', 2020, 222, 150.0, 0.0, "knjiga4.jpg", 'tvrdi', 'latinica');

INSERT INTO brojPrimeraka(knjigaISBN, brojKnjiga)
VALUES ('0000000000000',1);
INSERT INTO brojPrimeraka(knjigaISBN, brojKnjiga)
VALUES ('0000000000001',1);
INSERT INTO brojPrimeraka(knjigaISBN, brojKnjiga)
VALUES ('0000000000002',1);
INSERT INTO brojPrimeraka(knjigaISBN, brojKnjiga)
VALUES ('0000000000003',1);

INSERT INTO zanrovi (id, naziv, opis) VALUES (1, 'naučna fantastika','opis');
INSERT INTO zanrovi (id, naziv, opis) VALUES (2, 'akcija','opis');
INSERT INTO zanrovi (id, naziv, opis) VALUES (3, 'komedija','opis');
INSERT INTO zanrovi (id, naziv, opis) VALUES (4, 'horor','opis');
INSERT INTO zanrovi (id, naziv, opis) VALUES (5, 'avantura','opis');

INSERT INTO knjigaZanr (knjigaISBN, zanrId) VALUES ('0000000000000', 1);
INSERT INTO knjigaZanr (knjigaISBN, zanrId) VALUES ('0000000000000', 2);
INSERT INTO knjigaZanr (knjigaISBN, zanrId) VALUES ('0000000000000', 5);


INSERT INTO korisnici (korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datRodjenja, datReg, administrator, blokiran, loyaltyKartica) 
VALUES ('a', 'a', 'a@a.com', 'A', 'A', 'Novi Sad', '0632313344', '2020-06-22', '2020-06-22 20:00', true, false, false);
INSERT INTO korisnici (korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datRodjenja, datReg, administrator, blokiran, loyaltyKartica)  
VALUES ('b', 'b', 'b@b.com', 'B', 'B', 'Novi Sad', '0632313444', '2020-06-22', '2020-06-22 20:00', false, false, false);
INSERT INTO korisnici (korisnickoIme, lozinka, eMail, ime, prezime, adresa, brTel, datRodjenja, datReg, administrator, blokiran, loyaltyKartica)  
VALUES ('c', 'c', 'c@c.com', 'C', 'C', 'Novi Sad', '0632313445', '2020-06-22', '2020-06-22 20:00', false, false, false);
