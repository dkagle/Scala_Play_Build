package ezbuilder {

import java.io._



class Statics(basedir: String,schema: scala.collection.mutable.HashMap[String, List[String]], auth: Boolean, authTable: String) {

	
	def write(writer: PrintWriter, line: String) =
		writer.write(line+"\n")

	def print() {	
		var tab = "\t"
		var mvcDir = "app/views/"
		var fileName="layout.scala.html"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		
		write(writer, "@(content: Html)")
		write(writer, "<!DOCTYPE html>")
		write(writer, "<html>")
		write(writer, "<link rel=\"stylesheet\" href=\"@routes.Assets.versioned(\"stylesheets/application.css\")\"/>")
		write(writer, tab+"<body>")
		write(writer, tab+tab+"@content")
		write(writer, tab+"</body>")
		write(writer, "</html>")
		writer.close

		fileName="index.scala.html"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer, "<!DOCTYPE html>")
		write(writer, "@layout {")		
		
		for ( (table, flds) <- schema )  {
			val lowercaseObject = table.toLowerCase
			if ( lowercaseObject != authTable.toLowerCase )
				write(writer, tab+"<a href=\"/"+lowercaseObject+"\" class=\""+lowercaseObject+"\" >"+lowercaseObject.capitalize+"</a><br>")
		}
		write(writer,"}")
		writer.close		

		mvcDir = "app/controllers/"
		fileName="HomeController.scala"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))

		write(writer, "package controllers")
		write(writer, "")
		write(writer, "import play.api.mvc._")
		write(writer, "import play.api.routing._")
		write(writer, "import play.twirl.api.JavaScript")
		write(writer, "import com.google.inject.Inject")
		write(writer, "")
		if ( auth ) 
			write(writer, "class HomeController @Inject()(components: ControllerComponents)(authentication: AuthController) extends AbstractController(components) {")
		else
			write(writer, "class HomeController extends Controller {")
		write(writer, "")
		write(writer, tab+"def index = Action { implicit request =>")
		if ( auth ) {
			write(writer, tab+tab+"request.session.get(authentication.UserKey) match {")
			write(writer, tab+tab+"case Some(username) =>")
		}
		write(writer, tab+tab+tab+"Ok(views.html.index())")
		if ( auth ) {
			write(writer, tab+tab+"case None =>")
			write(writer, tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
			write(writer, tab+tab+"}")
		}
		write(writer, tab+"}")
		write(writer, tab+"val javascriptRouter = Action { implicit request =>")
		write(writer, tab+tab+"val router = JavaScriptReverseRouter(\"routes\")(")
		var flds = new scala.collection.mutable.ListBuffer[String]
		var table = ""
		var comma = ", "
		for ( (table, flds) <- schema )  {
			if ( (table, flds) == schema.last ) 
				comma = ""
			if ( table.toUpperCase != authTable.toUpperCase )
				write(writer, tab+tab+tab+"routes.javascript."+table.toLowerCase.capitalize+"Controller.delete"+comma)

		}

		write(writer, tab+tab+")")
		write(writer, tab+tab+tab+"Ok(JavaScript(s\"\"\"define(function () { $router; return routes })\"\"\") ")
		write(writer, tab+")")
		write(writer, tab+"}")
		write(writer, "}")
		writer.close


		mvcDir = "conf/"
		fileName = basedir+mvcDir+"routes"

		writer = new PrintWriter(new File(fileName))
		write(writer,"GET"+tab+tab+"/"+tab+tab+tab+tab+"controllers.HomeController.index")
		if ( auth ) {
			write(writer,"GET"+tab+tab+"/login"+tab+tab+tab+tab+"controllers.AuthController.login(returnTo)")
			write(writer,"+ nocsrf")
			write(writer,"POST"+tab+tab+"/authenticate"+tab+tab+tab+tab+"controllers.AuthController.authenticate(returnTo)")
			write(writer,"GET"+tab+tab+"/logout"+tab+tab+tab+tab+"controllers.AuthController.logout")
		}
		write(writer,"GET"+tab+tab+"/assets/javascripts/routes.js"+tab+tab+tab+tab+"controllers.HomeController.javascriptRouter")
		write(writer,"GET"+tab+tab+"/assets/*file"+tab+tab+tab+tab+"controllers.Assets.versioned(path=\"/public\", file: Asset)")
		write(writer,"GET"+tab+tab+"/javascriptRouter"+tab+tab+tab+tab+"controllers.HomeController.javascriptRouter")
		writer.close



	}
}
}
