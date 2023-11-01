package ezbuilder {

import java.io._


class HTML(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, primaryKeyIncrement: Option[Boolean]  ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, primaryKeyIncrement)   {
	def print() = {
		calculate
		val mvcDir = "app/views/"
		var fileName=capitalizedObjectName+"Details.scala.html"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))



		write(writer, "@("+lowerCaseObjectName+": models."+capitalizedObjectName+"Model)")
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, "@layout {")
		write(writer, tab+"<ul>")
		write(writer, tab+tab+"<li>")

		fields.foreach((f) => {		
			val fieldName = getFieldName(f).toLowerCase
			val fn = Some(fieldName)
			if ( fn != primaryKey ) {
				write(writer, tab+tab+fieldName.capitalize+": @"+lowerCaseObjectName+"."+fieldName+"<br>")
			}
		})
		write(writer, tab+tab+"</li>")
		write(writer, tab+"</ul>")
		
		write(writer, "<a href=\"@routes."+capitalizedObjectName+"Controller.list()\">Return</a>")
		write(writer, "}")
		writer.close

		fileName=capitalizedObjectName+"CreateForm.scala.html"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		
		write(writer, "@(form2: Form[Create"+capitalizedObjectName+"])(implicit messages: Messages)")
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, "")
		write(writer, "@import helper._")
		write(writer, "")
		write(writer, "@layout {")
		write(writer, tab+"<h2>Add "+capitalizedObjectName+"</h2>")
		write(writer, tab+"@helper.form(routes."+capitalizedObjectName+"Controller.create()) {")
		fields.foreach((f) => {	
			val fieldName = getFieldName(f).toLowerCase
			val fn = Some(getFieldName(f))
			if ( fn != primaryKey || !primaryKeyIncrement.get) {
				write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
			}
		})
		write(writer, tab+tab+"<button>Save</button>")	
		write(writer, tab+"}")
		write(writer, "}")
		writer.close

		fileName=capitalizedObjectName+"ListSelect.scala.html"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))	
		write(writer, "@("+lowerCaseObjectName+"s: Seq[models."+capitalizedObjectName+"Model])")
		write(writer, "")
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, tab+"@layout {")
		write(writer, tab+tab+"<h2>"+capitalizedObjectName+"s list</h2>")
		write(writer, tab+tab+"<ul>")
		write(writer, tab+tab+"@for("+lowerCaseObjectName+" <- "+lowerCaseObjectName+"s) {")
		write(writer, tab+tab+tab+"<li>")
		for ( f <- fields ) {
			val fieldName = getFieldName(f).toLowerCase
			val fn = Some(fieldName)
			if ( fn != primaryKey || !primaryKeyIncrement.get) {
				write(writer, tab+tab+tab+tab+fieldName+": @"+lowerCaseObjectName+"."+fieldName+"<br>")
			}
		}
		write(writer, tab+tab+tab+tab+"<button class=\"delete-"+lowerCaseObjectName+"\" data-id=\"@"+lowerCaseObjectName+"."+primaryKey.get.toLowerCase+"\">Delete</button>")
		write(writer, tab+tab+tab+tab+"<a href=\"/"+lowerCaseObjectName+"/update/@"+lowerCaseObjectName+"."+primaryKey.get.toLowerCase+"\" class=\"update-"+lowerCaseObjectName+"\" role=\"Button\">Update</a><br>")
		write(writer, tab+tab+tab+"</li>")
		write(writer, tab+tab+"}")
		write(writer, tab+"</ul>")
		write(writer, tab+"<a href=\"@routes."+capitalizedObjectName+"Controller.createForm()\">Add a new "+capitalizedObjectName+"</a>")
		write(writer, tab+"<script data-main=\"@routes.Assets.versioned(\"javascripts/"+lowerCaseObjectName+".js\")\" type=\"text/javascript\" src=\"@routes.Assets.versioned(\"javascripts/require.js\")\"></script>")
		write(writer, "}")
		writer.close

		fileName=capitalizedObjectName+"UpdateForm.scala.html"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		
		write(writer, "@(form2: Form[Update"+capitalizedObjectName+"])(implicit messages: Messages)")	
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, "")
		write(writer, "@import helper._")
		write(writer, "")
		write(writer, "@layout {")
		write(writer, tab+"<h2>Update "+capitalizedObjectName+"</h2>")
		write(writer, tab+"@helper.form(routes."+capitalizedObjectName+"Controller.updatePost( )) {")
		fields.foreach((f) => {	
			val fieldName = getFieldName(f).toLowerCase
			val fn = Some(fieldName)
			val dataType = getType(f.replaceAll("`",""))
			var scalaType = getLookupType(dataType)
			if ( fn == primaryKey ) {
				write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\", 'readonly -> \"readonly\")")
			} else {
				if (( scalaType == "java.sql.Timestamp") || ( scalaType == "java.sql.Date")) {
					write(writer, tab+tab+"@helper.inputDate(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
				} else {
					write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
				}
			}
		})

		write(writer, tab+tab+"<button>Update</button>")
		write(writer, tab+"}")
		write(writer, "}")
		writer.close


	

	}

}

}
