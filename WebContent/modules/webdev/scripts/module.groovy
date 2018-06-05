import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import app.FileManager

class Service extends ActionSupport {
    
	def subscribe(subscription) {	    
   	  order(subscription.hosting)
    }
    
    def order(order) {
         def params = [order.subject,order.priority,"webdev",order.plan,order.description,user.id,user.structure_id]
	     def result = connection.executeInsert 'insert into projects(subject,priority,service,plan,description,user_id,structure_id) values (?, ?, ?,?,?,?,?)', params
	     project.id = result[0][0]
		 def tasks
	     def bill = createBill(order)
	     if(bill.amount){
		      def params = [bill.fee,bill.amount,project.id,project.structure]
		      connection.executeInsert 'insert into bills(fee,amount,project_id,structure_id) values (?,?,?,?)', params
	       	  tasks = getTasks(true)
		  }else{
	          tasks = getTasks(false)
		  }
		  def query = 'insert into projects_tasks(name,description,project_id) values (?, ?, ?)'
	      connection.withBatch(query){ ps ->
	         tasks.each{
	            ps.addBatch(it.name,it.description,project.id)
	         } 
	      }
	}
    
    def createBill(order){
	   def bill = new Expando()
	   bill.fee = "caution"
	   if(order.plan == "business") {
	      bill.amount = 20000 * 3
	   }else if(order.plan == "corporate") {
	      bill.amount = 15000 * 3
	   }else if(order.plan == "personal") {
	      bill.amount = 10000 * 3
	   }
	   bill
	}
	
	def getTasks(caution){
	   def tasks = []
	   def task = new Expando(name : caution ? "Contrat et Caution" : "Contrat",description :"cette phase intiale &eacute;tablit la relation l&eacute;gale qui vous lie &agrave; ThinkTech")
	   tasks << task
	   task = new Expando(name :"Traitement",description : "cette phase d'approbation est celle o&ugrave; notre &eacute;quipe technique prend en charge votre projet")
       tasks << task
       task = new Expando(name :"Analyse du projet",description : "cette phase est celle de l'analyse de votre projet pour une meilleure compr&eacute;hension des objectifs")
	   tasks << task
	   task = new Expando(name :"D&eacute;finition des fonctionnalit&eacute;s",description : "cette phase est celle de la d&eacute;finition des fonctionnalit&eacute;s du produit")
	   tasks << task
	   task = new Expando(name :"Conception de l'interface",description : "cette phase est celle de la conception de l'interface utilisateur")
       tasks << task
       task = new Expando(name :"D&eacute;veloppement des fonctionnalit&eacute;s",description : "cette phase est celle du d&eacute;veloppement des fonctionnalit&eacute;s du produit")
       tasks << task
       task = new Expando(name :"Tests",description : "cette phase permet de tester les fonctionnalit&eacute;s du produit")
       tasks << task
       task = new Expando(name :"Validation",description : "cette phase est celle de la validation des fonctionnalit&eacute;s du produit")
       tasks << task
       task = new Expando(name :"Livraison du produit",description : "cette phase est celle du deploiement du produit final")
       tasks << task
       task = new Expando(name :"Formation",description : "cette phase finale est celle de la formation pour une prise en main du produit")
	   tasks << task
	   tasks
	}
	
	def pay(bill){
	  if(bill.fee == "caution"){
	  	connection.executeUpdate "update projects set status = 'in progress', startedOn = NOW(), progression = 10 where id = ?", [bill.project_id]
	  	def project = connection.firstRow("select * from projects  where id = ?", [bill.project_id])
	  	def info = "le paiement de la caution a &eacute;t&eacute; &eacute;ffectu&eacute; et le contrat vous liant &aacute; ThinkTech a &eacute;t&eacute; g&eacute;n&eacute;r&eacute; et ajout&eacute; aux documents du projet"
	  	connection.executeUpdate "update projects_tasks set startedOn = NOW(), status = 'finished', info = ? , progression = 100 where name = ? and project_id = ?", [info,"Contrat et Caution",bill.project_id]
	  	connection.executeUpdate "update projects_tasks set startedOn = NOW(), status = 'in progress' where name = ? and project_id = ?", ["Traitement",bill.project_id]
	  	def params = ["contrat.doc",50000,bill.project_id,bill.user.id]
	    connection.executeInsert 'insert into documents(name,size,project_id,createdBy) values (?,?,?,?)',params
	  	generateContract(bill.user,project)
	  }
    }
   
    def generateContract(user,project) {
      def folder =  module.folder.absolutePath + "/contracts/"
      Thread.start{
          def file = project.plan.replace(' ','-')+".doc"
	      def document = new HWPFDocument(new POIFSFileSystem(new File(folder+file)))
	      document.range.replaceText("structure_name",user.structure.name)
	      document.range.replaceText("date_contract",new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date()))
	      def out = new ByteArrayOutputStream()
	      document.write(out)
	      def dir = "structure_"+user.structure.id+"/"+"project_"+project.id
	      def manager = new FileManager()
	      manager.upload(dir+"/contrat.doc",new ByteArrayInputStream(out.toByteArray()))
      }
    }
		
}