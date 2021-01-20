$(document).ready(function(){
	function register(){
		var korisnickoImeInput = $("input[name=korisnickoIme]")
		var emailInput = $("input[name=eMail]")
		var imeInput = $("input[name=ime]")
		var prezimeInput = $("input[name=prezime]")
		var adresaInput = $("input[name=adresa]")
		var brTelInput = $("input[name=brTel]")
		var datumInput = $("input[name=datum]")
		var lozinkaInput = $("input[name=lozinka]")
		var ponovljenaLozinkaInput = $("input[name=ponovljenaLozinka]")
		
		var korisnickoIme = korisnickoImeInput.val()
		var email = emailInput.val()
		var ime = imeInput.val()
		var prezime = prezimeInput.val()
		var adresa = adresaInput.val()
		var brTel = brTelInput.val()
		var datum = datumInput.val()
		var lozinka = lozinkaInput.val()
		var ponovljenaLozinka = ponovljenaLozinkaInput.val()
	
		var validni = true
		var poruka = ""
		
		if(korisnickoIme.length==0 || email.length==0 || ime.length==0 || prezime.length==0
		|| adresa.length==0 || brTel.length==0 || lozinka.length==0
		|| ponovljenaLozinka.length==0){
			validni = false
			poruka+= "Morate popuniti sva polja\n"
		}
		
		else{
			if (brTel.match(/^[0-9]+$/) == null || brTel.length != 9 &&
			brTel.length != 10){
				poruka += "Broj telefona mora biti broj od 9 ili 10 cifri\n"
				validni = false
			}
			
			if (datum.length==0){
				validni = false
				poruka+= "Unesite datum\n"
			}
			if (lozinka != ponovljenaLozinka){
				validni = false
				poruka+="Lozinke se ne poklapaju\n"
			}
			
		}
		
		
		if (validni){
			var parametri = {
				korisnickoIme : korisnickoIme,
				eMail : email,
				ime : ime,
				prezime : prezime,
				adresa : adresa,
				brTel : brTel,
				datum : datum,
				lozinka : lozinka
			}
			
			console.log(parametri)
			$.post("Korisnici/Register", parametri)
			
		}
		
		else{
			alert(poruka)
			return false;
		}
	}
	
	$("form").submit(register)
})