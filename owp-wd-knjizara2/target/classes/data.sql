--DROP TABLE IF EXISTS knjige;
--CREATE TABLE knjige (
--    ISBN varchar(255),
--    naziv varchar(255),
--    izdavackaKuca varchar(255),
--    autor varchar(255),
--    kratakOpis varchar(255),
--    jezik varchar(255),
--    godinaIzdavanja int,
--	brojStranica int,
--	cena double,
--	prosecnaOcena double,
--	slika varchar(255),
--	tipPoveza varchar(255),
--	pismo varchar(255)
--);

INSERT INTO knjizara.zanr VALUES (1,"Komedija","");
INSERT INTO knjizara.zanr VALUES (2,"Drama","");
INSERT INTO knjizara.zanr VALUES (3,"Akcija","");
INSERT INTO knjizara.zanr VALUES (4,"Triler","");

INSERT INTO knjizara.knjiga VALUES ("000000001","Paulo Koeljo",205,849,2013,"Laguna","Srpski",
'Roman Alhemičar, sa kojim se ovaj "najčitaniji latinoamerički pisac posle Markesa" ("The Economist") stekao svetsku slavu, prodat je u tiražu od 10 miliona primeraka.
Američku društvo knjižara proglasilo je roman Alhemičar najboljom knjigom za mlade u 1994. godini, francuski časopis "Elle" knjigom godine 1995., u Jugoslaviji je dobio nagradu "Zlatni bestseler" Televizije Beograd i "Večernjih novosti" (1995, 1996.)Alhemičar je predstavljao temu mjuzikla koji je izveden u Japanu 1997. godine; po
služio je i kao inspiracija za simfoniju koju je komponovao Valter Tajeb; a kompanija "Warner Bross" otkupila je prava za snimanje filma.',
"Alhemičar","latinica",0,"alhemicar.jpg","tvrdi");

INSERT INTO knjizara.knjiga VALUES ("000000002","Sun Tzu",273,499,2005,"Alma Books","Engleski",
'Twenty-Five Hundred years ago, Sun Tzu wrote this classic book of military strategy based on Chinese warfare and military thought. Since that time, all levels of military have used the teaching on Sun Tzu to warfare and civilization have adapted these teachings for use in politics, business and everyday life. 
The Art of War is a book which should be used to gain advantage of opponents in the boardroom and battlefield alike.',
"The Art of War","latinica",0,"artofwar.jpg","meki");

INSERT INTO knjizara.knjiga VALUES ("000000003","Temple Grandin",341,1500,2009,"Houghton Mifflin Harcourt","Engleski",
'In her groundbreaking and best-selling book Animals in Translation, Temple Grandin drew on her own experience with autism as well as her distinguished career as an animal scientist to deliver extraordinary insights into how animals think, act, and feel. 
Now she builds on those insights to show us how to give our animals the best and happiest lifeon their terms, not ours.',
"Animals Make Us Human","latinica",0,"animalsmakesushumans.jpg","tvrdi");

INSERT INTO knjizara.knjiga VALUES ("000000004","Arthur Golden",503,1249,2005,"Vintage Books USA","Engleski",
'In "Memoirs of a Geisha," we enter a world where appearances are paramount; where a girls virginity is auctioned to the highest bidder; where women are trained to beguile the most powerful men; and where love is scorned as illusion. 
It is a unique and triumphant work of fiction - at once romantic, erotic, suspenseful - and completely unforgettable.',
"Memoirs of a Geisha","latinica",0,"memoirsofgeisha.jpg","tvrdi");

INSERT INTO knjizara.knjiga VALUES ("000000005","J.K. Rowling",288,799,2022,"ČAROBNA KNJIGA D.O.O.","Srpski",
'HARI POTER još nije bio ni čuo za Hogvorts kad su sove počele da bacaju pisma na otirač pred vratima broja četiri u Šimširovoj ulici. Pisma, adresirana zelenim mastilom na Harija, ispisana na pergamentu i zapečaćena ljubičastim voskom, sakrili su nepročitana njegovi grozni tetka i teča. 
Ali na dan Harijevog jedanaestog rođendana poludžin po imenu Hagrid pojavljuje se s neverovatnim novostima: Hari Poter je čarobnjak i primljen je u Hogvorts, školu za veštičarenje i čarobnjaštvo. Neverovatna pustolovina samo što nije počela!',
"HARI POTER I KAMEN MUDROSTI","latinica",0,"haripoter.jpg","tvrdi");

INSERT INTO knjizara.knjiga VALUES ("000000006","Keri Smit",224,679,2015,"VULKAN IZDAVAŠTVO","Srpski",
'Ova ilustrovana knjiga sadrži skup ideja koje vam pomažu da iskoristite čak i svoje greške kako biste stvorili pravo umetničko delo. Keri Smit nudi različite predloge za uništavanje svake pojedinačne strane vašeg dnevnika – od crtanja pomoću kafe i ukrašavanja lišćem do cepanja strana i ponovnog vraćanja u knjigu nakon izvršenog zadatka. 
Knjiga koja pomaže da probudite maštu i predstavlja vam novu vrtsu umetnosti u kojoj ste autor baš vi i konačno imate potpunu slobodu da iskažete sve što ste oduvek hteli.',
"UNIŠTI DNEVNIK","latinica",0,"unistidnevnik.jpg","meki");

INSERT INTO knjizara.knjiga_zanr VALUES ("000000001", 3);
INSERT INTO knjizara.knjiga_zanr VALUES ("000000004", 2);
INSERT INTO knjizara.knjiga_zanr VALUES ("000000004", 4);
INSERT INTO knjizara.knjiga_zanr VALUES ("000000005", 2);
INSERT INTO knjizara.knjiga_zanr VALUES ("000000005", 3);

INSERT INTO knjizara.broj_primeraka VALUES ("000000001", 3);
INSERT INTO knjizara.broj_primeraka VALUES ("000000002", 5);
INSERT INTO knjizara.broj_primeraka VALUES ("000000003", 1);
INSERT INTO knjizara.broj_primeraka VALUES ("000000004", 1);
INSERT INTO knjizara.broj_primeraka VALUES ("000000005", 2);
INSERT INTO knjizara.broj_primeraka VALUES ("000000006", 4);


INSERT INTO knjizara.korisnik (administrator,adresa,blokiran,br_tel,datum_reg,datum_rodjenja,e_mail,ime,korisnicko_ime,lozinka,poseduje_loyalty_karticu,prezime) 
	VALUES (TRUE,"Radnicka 23",FALSE,"0612326414","2007-03-25 12:01:09","2001-03-07","markonikolic@gmail.com","Marko","marko123","marko123",FALSE,"Nikolić");
INSERT INTO knjizara.korisnik (administrator,adresa,blokiran,br_tel,datum_reg,datum_rodjenja,e_mail,ime,korisnicko_ime,lozinka,poseduje_loyalty_karticu,prezime) 
	VALUES (FALSE,"Cara Dušana 17",FALSE,"0612223435","205-06-18 10:34:54","1994-09-01","milosmilosevic@gmail.com","Miloš","milos123","milos123",FALSE,"Milošević");