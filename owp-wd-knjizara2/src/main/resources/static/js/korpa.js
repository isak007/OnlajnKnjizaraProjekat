$(document).ready(function(){
	function ukloni(){
		var potvrda = confirm("Ukloni iz korpe?") 
		if (!potvrda) return false
	}
	
	function azuriraj(){
		var knjigaIsbnObj = $("input[name=knjigaISBN]")
		var knjigaISBN = knjigaIsbnObj.val()
		var brojKnjigaObj = $("select[name=brojKnjiga]")
		var brojKnjiga = brojKnjigaObj.find("input:checked")
		var brojPoenaObj = $("input[name=brojPoena]")
		var brojPoena = brojPoenaObj.val()
		
		console.log(knjigaISBN,brojKnjiga)
		if (brojPoena != null){
			var parametri = {
				knjigaISBN : knjigaISBN,
				brojKnjiga : brojKnjiga,
				brojPoena : brojPoena
			}
			$.get("Korpa/Update", parametri)
		}
		else if (brojPoena == null){
			var parametri = {
				knjigaISBN : knjigaISBN,
				brojKnjiga : brojKnjiga,
			}
			$.get("Korpa/Update", parametri)
		}
	}
	
	function potvrdaPlacanja(){
		var potvrda = confirm("Potvrda kupovine?") 
		if (potvrda) {
			var brojPoenaObj = $("input[name=brojPoena]")
			var brojPoena = brojPoenaObj.val()
			
			parametar = {
				brojPoena : brojPoena
			}
			$.post("Kupovina/Create", parametar)
		}
		else{
			return false;
		}
		
		
	}
	
	$(".ukloni").click(ukloni);
	$("#placanjeForm").click(potvrdaPlacanja);
})