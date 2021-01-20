$(document).ready(function(){
	function dodaj(){
		var nazivInput = $("input[name=naziv]")
		var isbnInput = $("input[name=isbn]")
		var izdavackaKucaInput = $("input[name=izdavackaKuca]")
		var autorInput = $("input[name=autor]")
		var kratakOpisInput = $("input[name=kratakOpis]")
		var godinaIzdavanjaInput = $("input[name=godinaIzdavanja]")
		var cenaInput = $("input[name=cena]")
		var brojStranicaInput = $("input[name=brojStranica]")
		var tipPovezaInput = $("select[name=tipPoveza]")
		var pismoInput = $("select[name=pismo]")
		var zanrInput = $("select[name=zanr]")
		var jezikInput = $("input[name=jezik]")
		var slikaInput = $("input[name=slika]")
		
		var naziv = nazivInput.val()
		var isbn = isbnInput.val()
		var izdavackaKuca = izdavackaKucaInput.val()
		var autor = autorInput.val()
		var kratakOpis = kratakOpisInput.val()
		var godinaIzdavanja = godinaIzdavanjaInput.val()
		var cena = cenaInput.val()
		var brojStranica = brojStranicaInput.val()
		var tipPoveza = tipPovezaInput.find("input:checked")
		var pismo = pismoInput.find("input:checked")
		var zanr = zanrInput.find("input:checked")
		var jezik = jezikInput.val()
		var slika = slikaInput.val()
		
		
		var validni = true
		var poruka = ""
		
		if (naziv.length==0 || isbn.length==0 || izdavackaKuca.length==0 || autor.length==0 || 
		kratakOpis.length==0 || godinaIzdavanja.length==0 || cena.length==0 ||
		 brojStranica.length == 0 || jezik.length==0){
			poruka += "Morate popuniti sva polja\n"
			validni = false
		}
		else{
			if (slika.length==0){
				poruka += "Morate uneti sliku\n"
				validni = false
			}
			
			if (isbn.match(/^[0-9]+$/) == null || isbn.length != 13){
				poruka += "ISBN mora biti broj od 13 cifri\n"
				validni = false
			}
		}
		console.log(parametri)
		if (validni){
			var parametri = {
				naziv : naziv,
				isbn : isbn,
				izdavackaKuca : izdavackaKuca,
				autor : autor,
				kratakOpis : kratakOpis,
				godinaIzdavanja : godinaIzdavanja,
				cena : cena,
				brojStranica : brojStranica,
				tipPoveza : tipPoveza,
				pismo : pismo,
				zanr : zanr,
				jezik : jezik,
				slika : slika,
			}
			$.post("Knjige/Create", parametri)
		}
		else
		{
			alert(poruka)
			return false;
		}
	}
	
	$("form").submit(dodaj)

})