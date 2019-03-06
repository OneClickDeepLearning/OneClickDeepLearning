		document.getElementById("flapBtn").addEventListener("click",function(e){
			flipPanel();
		});

		function flipPanel(){
		  if(document.getElementById("card").className.search("flip") === -1){
		        document.getElementById("card").className = document.getElementById("card").className + " flip";
				document.getElementById("flaptext").innerHTML="Jupyter";
		  }
		    else{
		        document.getElementById("card").classList.remove("flip");
		        document.getElementById("flaptext").innerHTML="Template";
		    }
		}
		