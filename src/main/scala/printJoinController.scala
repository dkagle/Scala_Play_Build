package ezbuilder {

import java.io._
import scala.io._
import scala.collection.immutable.HashMap


class JoinController(joinKey: Map[List[String],List[String]], basedir :String, schema : scala.collection.mutable.HashMap[String, List[String]], auth: Boolean, table1_fields: List[String], table2_fields: List[String], table2PrimaryKey: String ) extends scalaMVC  {




def print() = {

	var counter = 0
	var table1 = ""
	var table2 = ""
	var table1_key = ""
	var table2_key = ""
	var containsTimeStamp:Boolean = false
	var containsDouble:Boolean = false
	var containsDate = false
	var comma = ""
	var table1_detailString = ""
	var table2_detailString = ""
	var table2_createString = ""
	var table1_createString = ""
	var table1_updateString = ""
	var table2_updateString = ""
	var routesFieldString = ""
	var updateFieldString = ""
	var updateFormString = ""

	for ( ( tables, keys ) <- joinKey )  {
		table1 = tables.head
		table2 = tables.tail.head
		table2_key = keys.head.toLowerCase
		table1_key = keys.tail.head.toLowerCase

	}
	

	println("TABLE1KEY "+table1_key)
	println("TABLE2KEY "+table2_key)
	val tab = "\t"
	println(table1+"|"+table1_key)
	val DBdatatype = getDataType(table1, table1_key)

	if ( DBdatatype == "" ) {
		println("DBDATATYPE "+DBdatatype)		
	} else {
		println("DBDATATYPE "+DBdatatype)
		val PKdatatype = getLookupType(DBdatatype)
		println("PKDATATYPE "+PKdatatype)
		val objectName = table1+table2
		val lowercase = objectName.toLowerCase
		val table1lowercase = table1.toLowerCase
		val table2lowercase = table2.toLowerCase
		val table1Camelcase = table1lowercase.capitalize
		val table2Camelcase = table2lowercase.capitalize
		val camelcase = table1Camelcase+table2Camelcase
		val controllerDir = "app/controllers/"
		val modelsDir = "app/models/"
		val routesFile = "conf/routes"
		val viewDir = "app/views/"
		val fileName = camelcase+"Controller.scala"
		val writer = new PrintWriter(new File(basedir+controllerDir+fileName))
//		println("FILENAME "+basedir+controllerDir+fileName)
		comma = ","
		var comma2 = ","


		
		table2_fields.foreach((f) => {	
			println("JOIN TABLE2 FIELDS "+f)
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(getFieldName(f))
			val scalaType = getLookupControllerType(dataType.substring(0,3))
			if ( f == table2_fields.last ) 
				comma2 = ""
			
			println("SCALA TYPE "+getLookupType(dataType))
			println("TABLE2 "+fn.get+" "+table2PrimaryKey )
			if ( fn.get.toLowerCase == table2PrimaryKey.toLowerCase ) {
				
			} else {
				if ( getLookupType(dataType) == "java.sql.Date" ) {
					println("FOUND DATE")
					containsDate = true
					table2_detailString=table2_detailString + "new java.sql.Date(dateFormat.parse("+fieldName+").getTime)"+comma2	
					table2_updateString=table2_updateString + "new java.sql.Date(dateFormat.parse(update"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma2	
					routesFieldString = routesFieldString + fieldName+": String"+comma
					updateFieldString = updateFieldString + fieldName+": String"+comma
					updateFormString = updateFormString + table1lowercase+"."+fieldName+".toString"+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase )
						table2_createString=table2_createString + "new java.sql.Date(dateFormat.parse(create"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma2		
				}
			
				if ( getLookupType(dataType) == "java.sql.Timestamp" ) {
					println("FOUNDTIMESTAMP")
					containsTimeStamp = true
					table2_detailString=table2_detailString + "new java.sql.Timestamp(timeStampFormat.parse("+fieldName+").getTime)"+comma2				
					table2_updateString=table2_updateString + "new java.sql.Timestapm(timeStampFormat.parse(update"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma2
					routesFieldString = routesFieldString + fieldName+": String"+comma
					updateFieldString = updateFieldString + fieldName+": String"+comma
					updateFormString = updateFormString + table1lowercase+"."+fieldName+".toString"+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase )
						table2_createString=table2_createString + "new java.sql.Timestamp(timeStampFormat.parse(create"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma2

				}
				
				if ( getLookupType(dataType) == "Double" ) {
					println("FOUND DOUBLE")
					containsDouble = true	
					table2_detailString = table2_detailString+fieldName+comma2				
					routesFieldString = routesFieldString + fieldName+": Double"+comma
					table2_updateString = table2_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma2
					updateFieldString = updateFieldString + fieldName+": Double"+comma
					updateFormString = updateFormString + table2lowercase+"."+fieldName+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase ) {
						table2_createString = table2_createString+"create"+table1Camelcase+table2Camelcase+"."+fieldName+comma2
					}
//					table2_detailString=table2_detailString+"BigDecimal("+fieldName+").setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble"+comma			
				} else if ( getLookupType(dataType) == "Int" ) {
					println("FOUND Int")
					containsDouble = true
					table2_detailString = table2_detailString+fieldName+comma2
					table2_updateString = table2_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma2
					routesFieldString = routesFieldString + fieldName+": Int"+comma
					updateFieldString = updateFieldString + fieldName+": "+"Int"+comma
					updateFormString = updateFormString + table2lowercase+"."+fieldName+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase ) {
						table2_createString = table2_createString+"create"+table1Camelcase+table2Camelcase+"."+fieldName+comma2
					}
				} else {
					table2_detailString = table2_detailString+fieldName+comma2
					routesFieldString = routesFieldString + fieldName+": "+scalaType+comma
					table2_updateString = table2_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma2
					updateFieldString = updateFieldString + fieldName+": "+scalaType+comma
					updateFormString = updateFormString + table2lowercase+"."+fieldName+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase ) {
						
						table2_createString = table2_createString+"create"+table1Camelcase+table2Camelcase+"."+fieldName+comma2
					}
				}
			}
		})
		comma = ","
		table1_fields.foreach((f) => {	
			println("JOIN TABLE1 FIELDS "+f)
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(getFieldName(f))
			val scalaType = getLookupControllerType(dataType.substring(0,3))
			if ( f == table1_fields.last ) 
				comma = ""
			
			println("SCALA TYPE "+getLookupType(dataType))
			if ( fn.get.toLowerCase == table1_key.toLowerCase ) {
				table1_updateString = table1_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma
				updateFormString = updateFormString + table1lowercase+"."+fieldName+comma
				if ( getLookupType(dataType) == "Double" || getLookupType(dataType) == "Int" )
					updateFieldString = updateFieldString + fieldName+": Int"+comma
				else
					updateFieldString = updateFieldString + fieldName+": String"+comma
			} else {
				
				if ( getLookupType(dataType) == "java.sql.Date" ) {
					println("FOUND DATE")
					containsDate = true
					table1_detailString=table1_detailString + "new java.sql.Date(dateFormat.parse("+fieldName+").getTime)"+comma
					table1_createString=table1_createString + "new java.sql.Date(dateFormat.parse(create"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma
					table1_updateString=table1_updateString + "new java.sql.Date(dateFormat.parse(update"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma			
					updateFieldString = updateFieldString + fieldName+": String"+comma
					updateFormString = updateFormString + table1lowercase+"."+fieldName+".toString"+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase )
						routesFieldString = routesFieldString + fieldName+": String"+comma
				}
		
				if ( getLookupType(dataType) == "java.sql.Timestamp" ) {
					println("FOUNDTIMESTAMP")
					containsTimeStamp = true
					table1_detailString=table1_detailString + "new java.sql.Timestamp(timeStampFormat.parse("+fieldName+").getTime)"+comma
					table1_createString=table1_createString + "new java.sql.Timestamp(timeStampFormat.parse(create"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma
					table1_updateString=table1_updateString + "new java.sql.Timestamp(timeStampFormat.parse(update"+table1Camelcase+table2Camelcase+"."+fieldName+").getTime)"+comma	
					updateFieldString = updateFieldString + fieldName+": String"+comma
					updateFormString = updateFormString + table1lowercase+"."+fieldName+".toString"+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase )
						routesFieldString = routesFieldString + fieldName+": String"+comma
				} else if ( getLookupType(dataType) == "Double" ) {
					println("FOUND DOUBLE")
					containsDouble = true	
					//table1_detailString = table1_detailString+fieldName+comma
					table1_detailString=table1_detailString+"BigDecimal("+fieldName+").setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble"+comma	
					table1_updateString = table1_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma				
					updateFieldString = updateFieldString + fieldName+": String"+comma
					updateFormString = updateFormString + table1lowercase+"."+fieldName+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase ) {
						routesFieldString = routesFieldString + fieldName+": Double"+comma
						table1_createString = table1_createString+"create"+table1Camelcase+table2Camelcase+"."+fieldName+comma
					}
//							
				} else if ( getLookupType(dataType) == "Int" ) {
					println("FOUND Int")
					containsDouble = true
					table1_detailString = table1_detailString+fieldName+comma
					table1_updateString = table1_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma	
					updateFieldString = updateFieldString + fieldName+": "+"Int"+comma
					updateFormString = updateFormString + table1lowercase+"."+fieldName+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase ) {
						
						table1_createString = table1_createString+"create"+table1Camelcase+table2Camelcase+"."+fieldName+comma
						routesFieldString = routesFieldString + fieldName+": "+"Int"+comma
					}
				} else {
					table1_detailString = table1_detailString+fieldName+comma
					updateFieldString = updateFieldString + fieldName+": "+scalaType+comma
					table1_updateString = table1_updateString+"update"+table1Camelcase+table2Camelcase+"."+fieldName+comma	
					updateFormString = updateFormString + table1lowercase+"."+fieldName+comma
					if ( fieldName.toLowerCase != table2_key.toLowerCase ) {	
						table1_createString = table1_createString+"create"+table1Camelcase+table2Camelcase+"."+fieldName+comma
						routesFieldString = routesFieldString + fieldName+": "+scalaType+comma
					}
				}
			}
		})

		write(writer,"package controllers")
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
		write(writer,"import models."+table1Camelcase+"Model")
		write(writer,"import models."+table2Camelcase+"Model")
		if ( auth ) 
			write(writer,"import play.filters.csrf.CSRFFilter")
		write(writer,"")
		write(writer,"case class Create"+table1Camelcase+table2Camelcase+"("+routesFieldString+")")
		write(writer,"")
		write(writer, "object Create"+table1Camelcase+table2Camelcase+" {")
		write(writer, tab+"val form = Form(mapping(")
		comma = ", "
		table2_fields.foreach((f) => {	
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(getFieldName(f))
			val scalaType = getLookupControllerType(dataType.substring(0,3))
			if ( fn.get.toLowerCase == table2PrimaryKey.toLowerCase ) {

			} else if ( getLookupType(dataType) == "Double" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Double].verifying(Constraints.min(0.0, strict = true))"+comma)
			else if ( getLookupType(dataType) == "Int" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Int].verifying(Constraints.min(0, strict = true))"+comma)
			else 
				write(writer, tab+"\""+fieldName+"\" -> text.verifying()"+comma)

		})
		table1_fields.foreach((f) => {	
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(getFieldName(f))
			val scalaType = getLookupControllerType(dataType.substring(0,3))
			println("SCALA TYPE "+getLookupType(dataType))
			if ( f == table1_fields.last ) 
				comma = ""
			if ( fn.get.toLowerCase == table1_key.toLowerCase || fn.get.toLowerCase == table2_key.toLowerCase) {

			} else if ( getLookupType(dataType) == "Double" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Double].verifying(Constraints.min(0.0, strict = true))"+comma)
			else if ( getLookupType(dataType) == "Int" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Int].verifying(Constraints.min(0, strict = true))"+comma)
			else
				write(writer, tab+"\""+fieldName+"\" -> text.verifying()"+comma)
		})

		write(writer, "	)(Create"+table1Camelcase+table2Camelcase+".apply)(Create"+table1Camelcase+table2Camelcase+".unapply))")		
		write(writer, "}")
		write(writer, "")
		write(writer,"case class Update"+table1Camelcase+table2Camelcase+"("+updateFieldString+")")
		write(writer,"")
		write(writer, "object Update"+table1Camelcase+table2Camelcase+" {")
		write(writer, tab+"val form = Form(mapping(")
		comma = ", "
		
		table2_fields.foreach((f) => {	
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(fieldName)
			val scalaType = getLookupControllerType(dataType)

			if ( fn.get.toLowerCase == table2PrimaryKey.toLowerCase ) {

			} else if ( getLookupType(dataType) == "Double" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Double].verifying(Constraints.min(0.0, strict = true))"+comma)
			  else if ( getLookupType(dataType) == "Int" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Int].verifying(Constraints.min(0, strict = true))"+comma)
			  else 
				write(writer, tab+"\""+fieldName+"\" -> text.verifying()"+comma)
		})
		table1_fields.foreach((f) => {	
			val fieldName = getFieldName(f).toLowerCase
			val dataType = getType(f)
			val fn = Some(fieldName)
			val scalaType = getLookupControllerType(dataType)
			if ( f == table1_fields.last ) 
				comma = ""
			
			if ( getLookupType(dataType) == "Double" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Double].verifying(Constraints.min(0.0, strict = true))"+comma)
			  else if ( getLookupType(dataType) == "Int" ) 
				write(writer, tab+"\""+fieldName+"\" -> of[Int].verifying(Constraints.min(0, strict = true))"+comma)
			  else 
				write(writer, tab+"\""+fieldName+"\" -> text.verifying()"+comma)
		})

		write(writer, "	)(Update"+table1Camelcase+table2Camelcase+".apply)(Update"+table1Camelcase+table2Camelcase+".unapply))")	
		write(writer, "}")
		write(writer, "")	
		write(writer,"")
		write(writer,"class "+camelcase+"Controller @Inject()(components: ControllerComponents)("+table1lowercase+table2lowercase+": models."+camelcase+")("+table1Camelcase+": models."+table1Camelcase+")("+table2Camelcase+": models."+table2Camelcase+") extends AbstractController(components) with I18nSupport {")
		if ( auth ) 
			write(writer, tab+"val UserKey = \"username\"")
		if ( containsTimeStamp ) 
			write(writer, tab+"val timeStampFormat = new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\")")
		if ( containsDate )
			write(writer, tab+"val dateFormat = new java.text.SimpleDateFormat(\"yyyy-MM-dd\")")	
		write(writer,tab+"implicit val writes"+table1Camelcase+" = Json.writes["+table1Camelcase+"Model]")
		write(writer,tab+"implicit val writes"+table2Camelcase+" = Json.writes["+table2Camelcase+"Model]")
		write(writer,"")


		write(writer,tab+"val list = Action { implicit request =>")
		write(writer,tab+tab+"var "+lowercase+"Outs = "+lowercase+".list")
		write(writer,tab+tab+"render {")
		write(writer,tab+tab+tab+"case Accepts.Json() => Ok(Json.toJson("+lowercase+"Outs))")
		write(writer,tab+tab+tab+"case Accepts.Html() => ")
		if ( auth ) {
			write(writer, tab+tab+tab+tab+tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+tab+tab+tab+"case Some(user) =>")
		}		
		write(writer,tab+tab+tab+tab+"Ok(views.html."+camelcase+"ListSelect("+lowercase+"Outs))")
		
		if ( auth) {
			write(writer, tab+tab+tab+tab+tab+"case None =>")
			write(writer, tab+tab+tab+tab+tab+tab+"println(\"Redirect to login page.\")")
			write(writer, tab+tab+tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
			write(writer, tab+tab+tab+"}")
		}	
		write(writer,tab+tab+tab+"}")
		write(writer,tab+"}")

		write(writer,tab+"def details("+table1_key+": "+PKdatatype+") = Action { implicit request =>")
		write(writer,tab+tab+lowercase+".get("+table1_key+") match {")
		write(writer,tab+tab+tab+"case Some(("+table1lowercase+","+table2lowercase+")) =>") 
		write(writer,tab+tab+tab+tab+"render {")
		write(writer,tab+tab+tab+tab+tab+"case Accepts.Json() => Ok(\"[\"+Json.toJson("+table1lowercase+").toString+Json.toJson("+table2lowercase+").toString+\"]\")")
		write(writer,tab+tab+tab+tab+tab+"case Accepts.Html() => ")
		if ( auth ) {
			write(writer, tab+tab+tab+tab+tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+tab+tab+tab+tab+"case Some(user) =>")
		}
		write(writer,tab+tab+tab+tab+tab+tab+tab+"Ok(views.html."+camelcase+"Details("+table1lowercase+","+table2lowercase+"))")
		if ( auth) {
			write(writer, tab+tab+tab+tab+tab+tab+"case None =>")
			write(writer, tab+tab+tab+tab+tab+tab+tab+"println(\"Redirect to login page.\")")
			write(writer, tab+tab+tab+tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
			write(writer, tab+tab+tab+"}")
		}		

		write(writer,tab+tab+tab+tab+"}")
		write(writer,tab+tab+tab+"case None => NotFound(\"Record Not Found\")")
		write(writer,tab+tab+tab+"}")
		write(writer,tab+"}")
		write(writer,"")


		write(writer, tab+"def delete("+table1_key+": "+PKdatatype+") = Action { implicit request => ")
		write(writer, tab+tab+table1Camelcase+".get("+table1_key+") match {")
		write(writer, tab+tab+tab+"case Some("+table1lowercase+") =>")
		write(writer, tab+tab+tab+tab+"render {")
		write(writer, tab+tab+tab+tab+tab+"case Accepts.Json() => ")
		write(writer, tab+tab+tab+tab+tab+tab+"if ("+table1Camelcase+".delete("+table1_key+") >0 && "+table2Camelcase+".delete("+table1lowercase+"."+table2_key+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+tab+tab+tab+"case Accepts.Html() => ")
		if ( auth ) {
			write(writer, tab+tab+tab+tab+tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+tab+tab+tab+"case Some(user) =>")
		} 
		write(writer, tab+tab+tab+tab+tab+tab+"if ("+table1Camelcase+".delete("+table1_key+") >0 && "+table2Camelcase+".delete("+table1lowercase+"."+table2_key+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		if ( auth ) {
			write(writer, tab+tab+tab+tab+tab+"case None =>")
			write(writer, tab+tab+tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
			write(writer, tab+tab+tab+"}")
		} 
		
		write(writer, tab+tab+tab+tab+tab+"}")
		write(writer, tab+tab+tab+tab+"case None => BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+"}")
		write(writer, tab+"}")

		write(writer, tab+"def update("+updateFieldString+") = Action { ")
		write(writer, tab+tab+"if ("+table1Camelcase+".update("+table1_key.toLowerCase+","+table1_detailString+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+"if ("+table2Camelcase+".update("+table2_key.toLowerCase+","+table2_detailString+")>0) Ok(\"Success\") else BadRequest(\"Record Not Found\")")
		write(writer, tab+"}")
		write(writer, "")
		write(writer, tab+"val create = Action { implicit request =>")
		write(writer, tab+tab+"Create"+table1Camelcase+table2Camelcase+".form.bindFromRequest().fold(")
		write(writer, tab+tab+tab+"formWithErrors => render {")
		write(writer, tab+tab+tab+"case Accepts.Html() => BadRequest(views.html."+table1Camelcase+table2Camelcase+"CreateForm(formWithErrors))")
		write(writer, tab+tab+tab+"case Accepts.Json() => BadRequest(\"Failed Validation\")")
		write(writer, tab+tab+"},")
		write(writer, tab+tab+"create"+table1Camelcase+table2Camelcase+" => render {")
		write(writer, tab+tab+tab+"case Accepts.Json() =>")
		write(writer, tab+tab+tab+tab+"val returnval = "+table2Camelcase+".create(0, "+table2_createString+")")
		write(writer, tab+tab+tab+tab+"Ok(Json.toJson("+table1Camelcase+".create(0, returnval, "+table1_createString+")))") 
		write(writer, tab+tab+tab+"case Accepts.Html() =>")
		write(writer, tab+tab+tab+tab+"val returnval = "+table2Camelcase+".create(0, "+table2_createString+")")
		write(writer, tab+tab+tab+tab+table1Camelcase+".create(0, returnval, "+table1_createString+")") 
		write(writer, tab+tab+tab+tab+"Ok(views.html."+table1Camelcase+table2Camelcase+"Details("+table1Camelcase+"Model(0, returnval, "+table1_createString+"),"+table2Camelcase+"Model(0, "+ table2_createString+")))") 
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+")")
		write(writer, tab+"}")
		write(writer, "")
		write(writer, tab+"val updatePost = Action { implicit request =>")
		write(writer, tab+tab+"Update"+table1Camelcase+table2Camelcase+".form.bindFromRequest().fold(")
		write(writer, tab+tab+tab+"formWithErrors => render { case Accepts.Html() => BadRequest(\"views.html."+table1Camelcase+table2Camelcase+"UpdateForm(formWithErrors)\") },")
		write(writer, tab+tab+tab+"update"+table1Camelcase+table2Camelcase+" => render { case Accepts.Html() =>")
		write(writer, tab+tab+tab+tab+"val returnval = "+table1Camelcase+".update("+table1_updateString+")")
		write(writer, tab+tab+tab+tab+table2Camelcase+".update(update"+table1Camelcase+table2Camelcase+"."+table2_key+","+table2_updateString+")")
		write(writer, tab+tab+tab+tab+"Ok(views.html."+table1Camelcase+table2Camelcase+"Details("+table1Camelcase+"Model("+table1_updateString+"),"+table2Camelcase+"Model(update"+table1Camelcase+table2Camelcase+"."+table2_key+","+ table2_updateString+")))") 
		write(writer, tab+tab+tab+"}")
		write(writer, tab+tab+")")
		write(writer, tab+"}")

		write(writer, tab+"val createForm = Action { implicit request =>")
		if ( auth ) {
			write(writer, tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+"case Some(user) =>")
		}
		write(writer, tab+tab+tab+tab+"Ok(views.html."+table1Camelcase+table2Camelcase+"CreateForm(Create"+table1Camelcase+table2Camelcase+".form))")
		if ( auth ) {
			write(writer, tab+tab+tab+"case None =>")
			write(writer, tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
			write(writer, tab+tab+tab+"}")
		}
		
		write(writer, tab+"}")
		write(writer, "")

		write(writer, tab+"def updateForm("+table1_key+": "+PKdatatype+") = Action { implicit request =>")
		if ( auth ) {
			write(writer, tab+tab+"request.session.get(UserKey) match {")
			write(writer, tab+tab+tab+"case Some(user) =>")
		}   
		 
		write(writer, tab+tab+tab+table1lowercase+table2lowercase+".get("+table1_key+") match {")
		write(writer, tab+tab+tab+tab+tab+"case Some(("+table1lowercase+","+table2lowercase+")) =>")
		write(writer,	tab+tab+tab+tab+"Ok(views.html."+table1Camelcase+table2Camelcase+"UpdateForm( Update"+table1Camelcase+table2Camelcase+".form.fill(Update"+table1Camelcase+table2Camelcase+"("+updateFormString+"))))")
		write(writer, tab+tab+tab+tab+"case None => BadRequest(\"Record Not Found\")")
		write(writer, tab+tab+"}")
		if ( auth ) {
			write(writer, tab+tab+tab+"case None =>")
			write(writer, tab+tab+tab+tab+"Redirect(routes.AuthController.login(request.uri))")
			write(writer, tab+tab+"}")
		}    
		
		write(writer, tab+"}")
		write(writer, "")
		write(writer, "")

		write(writer,"}")

		writer.close()    

	}
}

def getDataType(tableName: String, key: String):String = {
	var dataType = ""		

	for ( (table, flds) <- schema )  {
		if ( table == tableName ) {
			flds.foreach((f) => {
				val fieldName = getFieldName(f) 
				if ( fieldName.toLowerCase == key.toLowerCase ) {
					dataType = getType(f)	
				}
			})
		}
	}

	dataType
}



}  //class
} // package ezbuilder

