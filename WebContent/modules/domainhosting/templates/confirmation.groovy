div(style : "font-family:Tahoma;background:#fafafa;padding-bottom:16px;padding-top: 25px"){
		 div(style : "padding-bottom:12px;margin-left:auto;margin-right:auto;width:80%;background:#fff") {
		    img(src : "https://www.thinktech.sn/images/logo.png", style : "display:block;margin : 0 auto")
		    div(style : "margin-top:10px;padding-bottom:2%;padding-top:2%;text-align:center;background:#3abfdd") {
		      h4(style : "font-size: 120%;color: #fff;margin: 3px") {
		        span("Enregistrement domaine web en cours")
		      }
		    }
		    div(style : "width:90%;margin:auto;margin-top : 20px;margin-bottom:30px") {
		     h5(style : "font-size: 90%;color: rgb(0, 0, 0);margin-bottom: 0px") {
		         span("Domaine : $order.name")
		     }
		     h5(style : "font-size: 90%;color: rgb(0, 0, 0);margin-top:5px;margin-bottom: 0px") {
		         span("Dur&eacute;e : $order.year an")
		     }
		     if(order.action == "transfer"){
		        h5(style : "font-size: 90%;color: rgb(0, 0, 0);margin-top:5px;margin-bottom: 0px") {
		         span("Action : transfert")
		     	}                                
		     }
		     p("le paiement a &eacute;t&eacute; bien effectu&eacute; et le domaine est maintenant en cours d\'enregistrement. la dur&eacute;e maximale d\'enregistrement est de deux heures")

		    }
		    div(style : "text-align:center;margin-top:30px;margin-bottom:10px") {
			    a(href : "$url/dashboard/domains",style : "font-size:130%;width:140px;margin:auto;text-decoration:none;background: #3abfdd;display:block;padding:10px;border-radius:2px;border:1px solid #eee;color:#fff;") {
			        span("Voir")
			    }
			}
		  }
		  
		  div(style :"margin: 10px;margin-top:10px;font-size : 80%;text-align:center") {
		      p("vous recevez cet email parce que vous (ou quelqu\'un utilisant cet email)")
		      p("a souscrit au service domainhosting en utilisant cette adresse")
		  }
		  
}