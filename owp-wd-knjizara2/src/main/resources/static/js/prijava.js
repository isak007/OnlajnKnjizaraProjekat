$(document).ready(function(){
	function login(){
		var korisnickoImeInput = $("input[name=korisnickoIme]")
		var lozinkaInput = $("input[name=lozinka]")
		
		var korisnickoIme = korisnickoImeInput.val()
		var lozinka = lozinkaInput.val()
		
		var validni = true
		var poruka = ""
		
		if(korisnickoIme.length==0 && lozinka.length==0){
			validni = false
			poruka+= "Unesite korisnicko ime i lozinku"
		}
		else{
			if (korisnickoIme.length==0){
				validni = false
				poruka+= "Unesite korisnicko ime"
			}
			else if (lozinka.length==0){
				validni = false
				poruka+= "Unesite lozinku"
			}
		}
		
		if (validni){
			var parametri = {
				korisnickoIme : korisnickoIme,
				lozinka : lozinka
			}
			
			$.post("Korisnici/Login", parametri)
			
		}
		else{
			alert(poruka)
			return false;
		}
	}
	
	$("form").submit(login)
})