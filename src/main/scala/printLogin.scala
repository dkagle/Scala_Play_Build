package ezbuilder {

import java.io._



class Login(basedir: String,schema: scala.collection.mutable.HashMap[String, List[String]], authUserField: String, authPWField: String) {

	def write(writer: PrintWriter, line: String) =
		writer.write(line+"\n")

	def print() {	
		var tab = "\t"
		var mvcDir = "app/views/"
		var fileName="login.scala.html"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer,"@(form: Form[Login], returnTo: String)(implicit header: RequestHeader, messages: Messages)")
		write(writer,"")
		write(writer,"@import play.api.i18n.Messages.Implicits._")
		write(writer,"")
		write(writer,"@layout {")
		write(writer,tab+"<h1>Please sign in</h1>")
		write(writer,tab+"@helper.form(routes.AuthController.authenticate(returnTo)) {")
		write(writer,tab+tab+tab+"<ul>")
		write(writer,tab+tab+tab+tab+"@for(error <- form.globalErrors) {")
		write(writer,tab+tab+tab+tab+tab+"<li>@error.message</li>")
		write(writer,tab+tab+tab+tab+"}")
		write(writer,tab+tab+tab+"</ul>")
		write(writer,tab+tab+tab+"")
		write(writer,tab+tab+tab+"@helper.inputText(form(\""+authUserField.toLowerCase+"\"), '_label -> \"Name\")")
		write(writer,tab+tab+tab+"@helper.inputPassword(form(\""+authPWField.toLowerCase+"\"), '_label -> \"Password\")")
		write(writer,tab+tab+tab+"<button>Sign in</button>")
		write(writer,tab+"}")
		write(writer,"}")
		writer.close

	}

}
}
