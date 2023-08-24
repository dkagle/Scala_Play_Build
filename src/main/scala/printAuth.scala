package ezbuilder {

import java.io._



class Auth(basedir: String,schema: scala.collection.mutable.HashMap[String, List[String]], authTable: String, authUserField: String, authPWField: String) {

	def write(writer: PrintWriter, line: String) =
		writer.write(line+"\n")

	def print() {	
		var tab = "\t"
		var mvcDir = "app/controllers/"
		var fileName="AuthController.scala"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		
		write(writer,"package controllers")
		write(writer,"")
		write(writer,"import play.api.mvc.{Action, AbstractController, ControllerComponents}")
		write(writer,"import play.api.mvc.Result")
		write(writer,"import play.api.mvc.RequestHeader")
		write(writer,"import play.api.libs.json._")
		write(writer,"import play.api.data.Form")
		write(writer,"import play.api.data.Forms.{mapping, text, of}")
		write(writer,"import play.api.data.validation.Constraints")	
		write(writer,"import com.google.inject.Inject")
		write(writer,"import play.api.i18n.I18nSupport")
		write(writer,"import play.api.i18n.MessagesApi")
		write(writer,"import play.api.data.Forms.{tuple, nonEmptyText}")
		write(writer,"")
		write(writer,"case class Login("+authUserField.toLowerCase+": String, "+authPWField.toLowerCase+": String)")
		write(writer,"")
		write(writer,"object Login {")
		write(writer,"val form = Form(mapping(")
		write(writer,tab+"\""+authUserField.toLowerCase+"\" -> text.verifying(),")
		write(writer,tab+"\""+authPWField.toLowerCase+"\" -> text.verifying()")
		write(writer,tab+")(Login.apply)(Login.unapply))")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,"class AuthController @Inject()(components: ControllerComponents)(messagesApi: MessagesApi)("+authTable.toLowerCase+": models."+authTable.toLowerCase.capitalize+") extends AbstractController(components) with I18nSupport {")
		write(writer,"")
		write(writer,tab+"val UserKey = \""+authUserField.toLowerCase+"\"")
		write(writer,tab+"def login(returnTo: String) = Action { implicit request =>")
		write(writer,tab+tab+"Ok(views.html.login(Login.form, returnTo))")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"def authenticate(returnTo: String) = Action { implicit request =>")
		write(writer,tab+"val submission = Login.form.bindFromRequest()")
		write(writer,tab+"submission.fold(")
		write(writer,tab+tab+"errors => BadRequest(views.html.login(errors, returnTo)),")
		write(writer,tab+tab+"{")
		write(writer,tab+tab+tab+"case (login) =>")
		write(writer,tab+tab+tab+tab+""+authTable.toLowerCase+".getbyUser(login."+authUserField.toLowerCase+", login."+authPWField.toLowerCase+") match {")
		write(writer,tab+tab+tab+tab+tab+"case Some("+authTable.toLowerCase+") => ")
		write(writer,tab+tab+tab+tab+tab+tab+"Redirect(returnTo).addingToSession(UserKey -> "+authTable.toLowerCase+"."+authUserField.toLowerCase+")")
		write(writer,tab+tab+tab+tab+tab+"case None =>")
		write(writer,tab+tab+tab+tab+tab+tab+"BadRequest(views.html.login(submission.withGlobalError(\"Incorrect UserName or Password \" +login."+authUserField.toLowerCase+"), returnTo))")
		write(writer,tab+tab+tab+tab+tab+"}")
		write(writer,tab+tab+tab+tab+"}")
		write(writer,tab+tab+tab+")")
		write(writer,tab+tab+"}")
		write(writer,tab+"val logout = Action { implicit request =>")
		write(writer,tab+"Redirect(routes.ItemController.list()).removingFromSession(UserKey)")
		write(writer,"}")
		write(writer,"}")
		writer.close
		

	}

}

}
