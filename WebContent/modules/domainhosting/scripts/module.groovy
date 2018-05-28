import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils


class Service extends ActionSupport {
    
	def subscribe(subscription) {
	    def hosting = subscription.hosting
	    def params = [hosting.domain,hosting.extension,hosting.price,hosting.year,hosting.action,hosting.eppCode,user.structure_id]
   	    def result = connection.executeInsert 'insert into domains(name,extension,price,year,action,eppCode,structure_id) values (?,?,?,?,?,?,?)', params
   	    params = ["h&edot;bergement domaine",subscription.service,hosting.price,result[0][0],user.structure_id]
	    connection.executeInsert 'insert into bills(fee,service,amount,product_id,structure_id) values (?,?,?,?,?)', params
    }
	
	def search(){
	    response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Cache-control", "private, max-age=7200")   
	    def domain = getParameter("domain")
	    if(domain){
		    def url = "https://api.duoservers.com/?auth_username=store203583&auth_password=apipassword&section=domains&command=check&name=${domain}&tlds[0]=com&tlds[1]=net&tlds[2]=org&tlds[3]=biz&tlds[4]=info&tlds[5]=tv&tlds[6]=press&tlds[7]=news&tlds[8]=tech&return_type=json"
		    def get = new HttpGet(url)
			def client = HttpClientBuilder.create().build()
			def response = client.execute(get)
			def body = EntityUtils.toString(response.getEntity())
			write(body)
		}
	}

    def payBill(bill){
    
    }
}

new Service()