package ezbuilder {

import java.io._


class ControllerAuth(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir)   {


	def print() = {
	val mvcDir = "app/controllers/"
	calculate
	var containsDate:Boolean = false
	var listString = ""
	var detailString = ""
	var containsTimeStamp:Boolean = false
	var containsDouble:Boolean = false
	val itemModel = capitalizedObjectName+"Model"


		
		val fileName=capitalizedObjectName+"Controller.scala"
		val writer = new PrintWriter(new File(basedir+mvcDir+fileName))

		write(writer,"package controllers")
		write(writer,"")
		write(writer,"")
		write(writer,"import play.api.mvc.{Action, AbstractController, ControllerComponents}")
		write(writer,"import play.api.mvc.Result")
		write(writer,"import play.api.libs.json._")
		write(writer,"import play.api.data.Form")
		write(writer,"import play.api.data.Forms.{mapping, text, of}")
		write(writer,"import play.api.data.format.Formats.doubleFormat")
		write(writer,"import play.api.data.format.Formats.intFormat")
		write(writer,"import play.api.data.validation.Constraints")
		write(writer,"import com.google.inject.Inject")
		write(writer,"import play.api.i18n.I18nSupport")
		write(writer,"import play.api.i18n.MessagesApi")
		write(writer,"import play.filters.csrf.CSRFFilter")
		write(writer,"import models."+itemModel)

		write(writer,"")
		write(writer,"case class Create"+capitalizedObjectName+"("+controllerFieldString+")")
		write(writer,"")
		write(writer, "object Create"+capitalizedObjectName+" {")
		write(writer, tab+"val form = Form(mapping(")
		comma = ", "
		for ( f <- fields ) {
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(getFieldName(f))
			val scalaType = getLookupControllerType(dataType.substring(0,3))
			if ( f == fields.last ) 
				comma = ""
			
			println("SCALA TYPE "+getLookupType(dataType))
			if ( getLookupType(dataType) == "java.sql.Date" ) {
				println("FOUND DATE")
				containsDate = true
			}
			if ( getLookupType(dataType) == "java.sql.Timestamp" ) {
				println("FOUNDTIMESTAMP")
				containsTimeStamp = true
			}
			
			if ( getLookupType(dataType) == "Double" ) {
				println("FOUND DOUBLE")
				containsDouble = true				
				listString=listString+"BigDecimal("+lowerCaseObjectName+"Out."+fieldName+").setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble"+comma	
				detailString=detailString+"BigDecimal("+lowerCaseObjectName+"."+fieldName+").setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble"+comma			
				if ( fn != primaryKey ) {
					
					write(writer, tab+"\""+fieldName+"\" -> of[Double].verifying(Constraints.min(0.0, strict = true))"+comma)
				}
			} else if ( getLookupType(dataType) == "Int" ) {
				println("FOUND Int")
				containsDouble = true
				listString = listString+lowerCaseObjectName+"Out."+fieldName+comma
				detailString = detailString+lowerCaseObjectName+"."+fieldName+comma
				if ( fn != primaryKey ) {
					
					write(writer, tab+"\""+fieldName+"\" -> of[Int].verifying(Constraints.min(0, strict = true))"+comma)
				}
			} else {
				listString = listString+lowerCaseObjectName+"Out."+fieldName+comma
				detailString = detailString+lowerCaseObjectName+"."+fieldName+comma
				if ( fn != primaryKey ) {
					
					write(writer, tab+"\""+fieldName+"\" -> text.verifying()"+comma)
				}
			}
		}
		write(writer, "	)(Create"+capitalizedObjectName+".apply)(Create"+capitalizedObjectName+".unapply))")		
		write(writer, "}")
		write(writer, "")

		write(writer,"case class Update"+capitalizedObjectName+"("+updateControllerFieldString+")")
		write(writer,"")
		write(writer, "object Update"+capitalizedObjectName+" {")
		write(writer, tab+"val form = Form(mapping(")
		comma = ", "
		for ( f <- fields ) {
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(fieldName)
			val scalaType = getLookupControllerType(dataType)
			if ( f == fields.last ) 
				comma = ""
			
			println("SCALA TYPE "+getLookupType(dataType))

				
				
			if ( getLookupType(dataType) == "Double" ) {
				println("FOUND DOUBLE")
				containsDouble = true
				write(writer, tab+"\""+fieldName+"\" -> of[Double].verifying(Constraints.min(0.0, strict = true))"+comma)
			} else if ( getLookupType(dataType) == "Int" ) {
				println("FOUND Int")
				containsDouble = true
				write(writer, tab+"\""+fieldName+"\" -> of[Int].verifying(Constraints.min(0, strict = true))"+comma)
			} else {
				write(writer, tab+"\""+fieldName+"\" -> text.verifying()"+comma)
			}
		}
		write(writer, "	)(Update"+capitalizedObjectName+".apply)(Update"+capitalizedObjectName+".unapply))")	
		write(writer, "}")
		write(writer, "")		


		write(writer, "class "+capitalizedObjectName+"Controller @Inject()(components: ControllerComponents)("+lowerCaseObjectName+": models."+capitalizedObjectName+") extends AbstractController(components) with I18nSupport {")
		write(writer, tab+"val UserKey = \"username\"")
		if ( containsTimeStamp ) 
			write(writer, tab+"val timeStampFormat = new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\")")
		if ( containsDate )
			write(writer, tab+"val dateFormat = new java.text.SimpleDateFormat(\"yyyy-MM-dd\")")	

		if ( containsDate || containsTimeStamp) {
			write(writer, tab+"implicit val writes"+capitalizedObjectName+" = Writes["+itemModel+"] {")
			write(writer, tab+tab+"case "+capitalizedObjectName+"Model("+primaryKey.get.toLowerCase+" ,"+delimitedFields+") =>")
			write(writer, tab+tab+tab+"Json.obj(")
			var comma = ","
			for ( f <- fields ) {
				val fieldName = getFieldName(f).toLowerCase
				val dataType = getType(f)
				val fn = Some(fieldName)

				if ( f == fields.last ) 
					comma = ""

				write(writer, tab+tab+tab+tab+"\""+fieldName+"\" -> "+fieldName+comma)

				
			}
			write(writer, tab+tab+tab+")")
			write(writer, tab+"}")
		} else {
			write(writer, tab+"implicit val writes"+capitalizedObjectName+" = Json.writes["+itemModel+"]")
		}
		if ( containsDouble ) {
			write(writer, tab+"val "+capitalizedObjectName+"Outs = new scala.collection.mutable.ListBuffer["+capitalizedObjectName+"Model]")
			write(writer, tab+"val list = Action { implicit request => ")
			write(writer, tab+tab+"var "+lowerCaseObjectName+"Outs = "+lowerCaseObjectName+".list")
			write(writer, tab+tab+capitalizedObjectName+"Outs.clear")
			write(writer, tab+tab+"for ( "+lowerCaseObjectName+"Out <- "+lowerCaseObjectName+"Outs ) {")
			write(writer, tab+tab+tab+"var "+capitalizedObjectName+"Out = new "+capitalizedObjectName+"Model("+listString+")")
			write(writer, tab+tab+tab+capitalizedObjectName+"Outs+="+capitalizedObjectName+"Out")
			write(writer, tab+tab+"}")
			write(writer, tab+tab+"render {")
			write(writer, tab+tab+tab+"case Accepts.Json() => Ok(Json.toJson("+capitalizedObjectName+"Outs))")
			write(writer, tab+tab+tab+"case Accepts.Html() => ")
			write(writer, tab+tab+tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+tab+tab+"case Some(user) =>")
			write(writer, tab+tab+tab+tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"ListSelect("+capitalizedObjectName+"Outs))")		

		} else {
			write(writer, tab+"val list = Action { implicit request =>")
			write(writer, tab+tab+"render {")
			write(writer, tab+tab+tab+"case Accepts.Json() => Ok(Json.toJson("+capitalizedObjectName+"Outs))")
			write(writer, tab+tab+tab+"case Accepts.Html() =>")
			write(writer, tab+tab+tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+tab+tab+"case Some(user) =>")
			write(writer, tab+tab+tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"ListSelect("+capitalizedObjectName+".list))")	

		}
		write(writer, tab+tab+tab+tab+tab+"case None =>")
		write(writer, tab+tab+tab+tab+tab+tab+"println(\"Redirect to login page.\")")
		write(writer, tab+tab+tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+"}")
		write(writer, tab+"}")
		write(writer, tab+"val create = Action { implicit request =>")
		write(writer, tab+tab+"Create"+capitalizedObjectName+".form.bindFromRequest().fold(")
		write(writer, tab+tab+tab+"formWithErrors => render {")
		write(writer, tab+tab+tab+"case Accepts.Html() => BadRequest(views.html."+capitalizedObjectName+"CreateForm(formWithErrors))")
		write(writer, tab+tab+tab+"case Accepts.Json() => BadRequest(\"Failed Validation\")")
		write(writer, tab+tab+"},")
		write(writer, tab+tab+"create"+capitalizedObjectName+" => render {")
		write(writer, tab+tab+tab+"case Accepts.Json() =>")
		write(writer, tab+tab+tab+tab+"Ok(Json.toJson("+lowerCaseObjectName+".create("+createFieldString+")))") 
		write(writer, tab+tab+tab+"case Accepts.Html() =>")
		write(writer, tab+tab+tab+tab+lowerCaseObjectName+".create("+createFieldString+")")
		write(writer, tab+tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"Details("+capitalizedObjectName+"Model(0, "+createFieldString+")))") 
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+")")
		write(writer, tab+"}")

		write(writer, "")

		write(writer, tab+"def details("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+") = Action { implicit request =>")
		write(writer, tab+tab+lowerCaseObjectName+".get("+primaryKey.get.toLowerCase+") match {")
		write(writer, tab+tab+"case Some("+lowerCaseObjectName+") =>") 
		if ( containsDouble ) {
			write(writer, tab+tab+tab+"var "+lowerCaseObjectName+"Out = new "+capitalizedObjectName+"Model("+detailString+")")
			write(writer, tab+tab+tab+"render {")
			write(writer, tab+tab+tab+"case Accepts.Json() => Ok(Json.toJson("+lowerCaseObjectName+"Out))")
		} else {
			write(writer, tab+tab+tab+"render {")
			write(writer, tab+tab+tab+tab+"case Accepts.Json() => Ok(Json.toJson("+lowerCaseObjectName+"))")
		}
		write(writer, tab+tab+tab+"case Accepts.Html() => ")
		write(writer, tab+tab+tab+tab+"request.session.get(UserKey) match {")
		write(writer, tab+tab+tab+tab+tab+"case Some(user) =>")
		write(writer, tab+tab+tab+tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"Details("+lowerCaseObjectName+"))")
		write(writer, tab+tab+tab+tab+tab+"case None =>")
		write(writer, tab+tab+tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
		write(writer, tab+tab+tab+tab+"}")
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+"case None => BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+"}")
		write(writer, tab+"}")
		write(writer, "")
		write(writer, "")
	
		write(writer, tab+"def update("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+", "+routesFieldString+") = Action { ")
		write(writer, tab+tab+"if ("+lowerCaseObjectName+".update("+primaryKey.get.toLowerCase+","+controllerDelimitedFields+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		write(writer, tab+"}")
		write(writer, "")
		
		write(writer, tab+"def delete("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+") = Action { implicit request => ")
		write(writer, tab+tab+"render {")
		write(writer, tab+tab+tab+"case Accepts.Json() => ")
		write(writer, tab+tab+tab+tab+"if ("+lowerCaseObjectName+".delete("+primaryKey.get.toLowerCase+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+tab+"case Accepts.Html() => ")
		write(writer, tab+tab+tab+tab+"request.session.get(UserKey) match {")
		write(writer, tab+tab+tab+tab+tab+"case Some(user) =>")
		write(writer, tab+tab+tab+tab+tab+tab+"if ("+lowerCaseObjectName+".delete("+primaryKey.get.toLowerCase+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+tab+tab+tab+"case None =>")
		write(writer, tab+tab+tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+"}")
		write(writer, tab+"}")

		write(writer, tab+"val updatePost = Action { implicit request =>")
		write(writer, tab+tab+"Update"+capitalizedObjectName+".form.bindFromRequest().fold(")
		write(writer, tab+tab+tab+"formWithErrors => render { case Accepts.Html() => BadRequest(\"views.html."+capitalizedObjectName+"UpdateForm(formWithErrors)\") },")
		write(writer, tab+tab+tab+"update"+capitalizedObjectName+" => render { case Accepts.Html() =>")
		write(writer, tab+tab+tab+lowerCaseObjectName+".update("+updateFieldString+")")
		write(writer, tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"Details("+capitalizedObjectName+"Model("+updateFieldString+")))")
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+")")
		write(writer, tab+"}")
		write(writer, "")

		write(writer, tab+"val createForm = Action { implicit request =>")
		write(writer, tab+tab+"request.session.get(UserKey) match {")
		write(writer, tab+tab+tab+"case Some(user) =>")
		write(writer, tab+tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"CreateForm(Create"+capitalizedObjectName+".form))")
		write(writer, tab+tab+tab+"case None =>")
		write(writer, tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
		write(writer, tab+tab+tab+"}")
		write(writer, tab+"}")
		write(writer, "")

		write(writer, tab+"def updateForm(id: Int) = Action { implicit request =>")
		write(writer, tab+tab+"request.session.get(UserKey) match {")
		write(writer, tab+tab+tab+"case Some(user) =>")
		write(writer, tab+tab+tab+tab+"val update"+capitalizedObjectName+" = "+lowerCaseObjectName+".get(id).get")
		write(writer, tab+tab+tab+tab+"Ok(views.html."+capitalizedObjectName+"UpdateForm( Update"+capitalizedObjectName+".form.fill(Update"+capitalizedObjectName+"("+updateFormString+"))))")
		write(writer, tab+tab+tab+"case None =>")
		write(writer, tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
		write(writer, tab+tab+"}")

		write(writer, tab+"}")
				
		write(writer, "}") 
		writer.close() 
	
	
	}
}

}


