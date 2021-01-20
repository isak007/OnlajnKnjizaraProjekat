$(document).ready(function(){
	function update(){
		var korisnickoImeObj = $("input[name=korisnickoIme]")
		var ulogaObj = $("select[name=uloga]")
		var buttonObj = $("button[name=button]")
		
		var korisnickoIme = korisnickoImeObj.val()
		var uloga = ulogaObj.find("input:checked")
		var button = buttonObj.val()
		
		var potvrda = confirm("Da li ste sigurni?") 
		if (potvrda) {
			var parametri = {
				korisnickoIme : korisnickoIme,
				uloga : uloga,
				button : button
			}
			$.post("Korisnici/Edit", parametri)
		}
		else{
			return false;
		}
	}
	
	
	$("#update").submit(update)
})