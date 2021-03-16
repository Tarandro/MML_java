// resize header to size of browser window

var ready = (callback) => {
	if (document.readyState != "loading") callback();
	else document.addEventListener("DOMContentLoaded", callback);
}

ready(() => {
	document.querySelector(".header").style.height = window.innerHeight + "px";
})

function sendMLRequest() {
		//alert("toto");

   var xhttp = new XMLHttpRequest();

   // Set GET method and ajax file path with parameter
   xhttp.open("GET", "ajaxfile.php?request=1", true);

   // Content-type
   xhttp.setRequestHeader('Content-type', 'application/json');

   // call on request changes state
   xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {

       // Parse this.responseText to JSON object
       var response = JSON.parse(this.responseText);

       // Loop on response object
       for (var key in response) {
          if (response.hasOwnProperty(key)) {
             var val = response[key];


          }
       } 

      }
   };

   // Send request

   xhttp.send();
document.getElementById("demo").innerHTML = xhttp.responseText;
alert(http.responseText)
}
