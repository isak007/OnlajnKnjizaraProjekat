$(document).ready(function(){
	$("#licniPodaci").click(function(){
		var akcija = $(this).attr("value")
		if (akcija == "prikazi"){
			$("#updateLabel").show()
			$("#update").show()
			$(this).attr("value","sakrij")
			$(this).text("Sakrij licne podatke")
		}
		else if (akcija == "sakrij"){
			$("#updateLabel").hide()
			$("#update").hide()
			$(this).attr("value","prikazi")
			$(this).text("Prikazi licne podatke")
		}
		return false;
	})
	
	if ($("#licniPodaci").attr("value") == "prikazi"){
		$("#updateLabel").hide()
		$("#update").hide()
	}
})