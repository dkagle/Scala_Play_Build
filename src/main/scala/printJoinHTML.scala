package ezbuilder {

import java.io._
import scala.io._
import scala.collection.immutable.HashMap


class JoinHTML(joinKey: Map[List[String],List[String]], basedir :String, schema : scala.collection.mutable.HashMap[String, List[String]], tableIncriment: scala.collection.mutable.HashMap[String, Boolean]) extends scalaMVC {



def print() = {

	var counter = 0
	var table1 = ""
	var table2 = ""
	var table1_key = ""
	var table2_key = ""
	var primaryKeyIncrement = Option(false)

	for ( ( tables, keys ) <- joinKey )  {
		table1 = tables.head
		table2 = tables.tail.head
		table2_key = keys.head.toLowerCase
		table1_key = keys.tail.head.toLowerCase

	}



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
		val modelsDir = "app/models/"
		val routesFile = "conf/routes"
		val viewDir = "app/views/"


		var fileName = camelcase+"Details.scala.html"
		var writer = new PrintWriter(new File(basedir+viewDir+fileName))
		write(writer,"@("+table1lowercase+": models."+table1Camelcase+"Model,"+table2lowercase+": models."+table2Camelcase+"Model)")
		write(writer,"<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, "@layout {")
		write(writer, tab+"<ul>")
		write(writer, tab+tab+"<li>")
		for ( (table, flds) <- schema )  {
			if ( table.toLowerCase == table1lowercase || table.toLowerCase == table2lowercase ) {
				flds.foreach((f) => {
					val fieldName = getFieldName(f).toLowerCase 
					write(writer, tab+tab+tab+fieldName.capitalize+": @"+table.toLowerCase+"."+fieldName+"<br>")
				})
			}
		}		
		write(writer, tab+tab+"</li>")
		write(writer, tab+"</ul>")		
		write(writer, "<a href=\"@routes."+camelcase+"Controller.list()\">Return</a>")
		write(writer, "}")
		writer.close()

		fileName = camelcase+"ListSelect.scala.html"
		writer = new PrintWriter(new File(basedir+viewDir+fileName))
		write(writer, "@("+table1lowercase+"_"+table2lowercase+"s: Seq[(models."+table1Camelcase+"Model,models."+table2Camelcase+"Model)])")
		write(writer, "")
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, tab+"@layout {")
		write(writer, tab+tab+"<h2>"+table1lowercase+" "+table2lowercase+" list</h2>")
		write(writer, tab+tab+"<ul>")
		write(writer, tab+tab+"@for(("+table1lowercase+","+table2lowercase+") <- "+table1lowercase+"_"+table2lowercase+"s) {")
		write(writer, tab+tab+tab+"<li>")
		for ( (table, flds) <- schema )  {
			if ( table.toLowerCase == table1lowercase || table.toLowerCase == table2lowercase ) {
				flds.foreach((f) => {
					val fieldName = getFieldName(f).toLowerCase 
					if ( fieldName != table1_key )
						write(writer, tab+tab+tab+tab+fieldName.capitalize+": @"+table.toLowerCase+"."+fieldName+"<br>")
				})
			}
		}		
		write(writer, tab+tab+tab+tab+"<button class=\"delete-"+table1lowercase+table2lowercase+"\" data-id=\"@"+table1lowercase+"."+table1_key+"\">Delete</button>")
		write(writer, tab+tab+tab+tab+"<a href=\"/"+table1lowercase+table2lowercase+"/update/@"+table1lowercase+"."+table2_key+"\" class=\"update-"+table1lowercase+table2lowercase+"\" role=\"Button\">Update</a><br>")
		write(writer, tab+tab+tab+"</li>")
		write(writer, tab+tab+"}")
		write(writer, tab+"</ul>")
		write(writer, tab+"<a href=\"@routes."+table1Camelcase+table2Camelcase+"Controller.createForm()\">Add a new "+table1Camelcase+table2Camelcase+"</a>")
		write(writer, tab+"<script data-main=\"@routes.Assets.versioned(\"javascripts/"+lowercase+".js\")\" type=\"text/javascript\" src=\"@routes.Assets.versioned(\"javascripts/require.js\")\"></script>")
		write(writer, "}")
		writer.close()

		fileName = camelcase+"CreateForm.scala.html"
		writer = new PrintWriter(new File(basedir+viewDir+fileName))
		
		write(writer, "@(form2: Form[Create"+table1Camelcase+table2Camelcase+"])(implicit messages: Messages)")
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, "")
		write(writer, "@import helper._")
		write(writer, "")
		write(writer, "@layout {")
		write(writer, tab+"<h2>Add "+table1Camelcase +table2Camelcase +"</h2>")
		write(writer, tab+"@helper.form(routes."+table1Camelcase +table2Camelcase +"Controller.create()) {")
		println("TABLE 1 KEY "+table1_key+" "+table2_key)
		
		for ( (table, flds) <- schema )  {
			if ( table.toLowerCase == table1lowercase || table.toLowerCase == table2lowercase ) {
				primaryKeyIncrement = tableIncriment get table
				flds.foreach((f) => {
					val fieldName = getFieldName(f).toLowerCase
					val fn = Some(getFieldName(f))
					if ( fn.get.toLowerCase != table1_key.toLowerCase ) {
						if (fn.get.toLowerCase != table2_key.toLowerCase ) {
							
							write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
						}
					}
				})
			}
		}
		write(writer, tab+tab+"<button>Save</button>")	
		write(writer, tab+"}")
		write(writer, "}")
		writer.close()

		fileName=camelcase+"UpdateForm.scala.html"
		writer = new PrintWriter(new File(basedir+viewDir+fileName))
		
		write(writer, "@(form2: Form[Update"+table1Camelcase+table2Camelcase+"])(implicit messages: Messages)")	
		write(writer, "<a href=\"@routes.HomeController.index()\">Index</a>")
		write(writer, "")
		write(writer, "@import helper._")
		write(writer, "")
		write(writer, "@layout {")
		write(writer, tab+"<h2>Update "+table1Camelcase+table2Camelcase+"</h2>")
		write(writer, tab+"@helper.form(routes."+table1Camelcase+table2Camelcase+"Controller.updatePost( )) {")
		for ( (table, flds) <- schema )  {
			if ( table.toLowerCase == table2lowercase ) {
				primaryKeyIncrement = tableIncriment get table
				flds.foreach((f) => {	
					val fieldName = getFieldName(f).toLowerCase
					val fn = Some(fieldName)
					val dataType = getType(f.replaceAll("`",""))
					var scalaType = getLookupType(dataType)
					println("FN "+fn.get.toLowerCase+" table_1_key "+table1_key.toLowerCase+"|")

					if (( scalaType == "java.sql.Timestamp") || ( scalaType == "java.sql.Date")) {
						write(writer, tab+tab+"@helper.inputDate(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
					} else {
						write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
					}

				})
			}
			if ( table.toLowerCase == table1lowercase ) {
				primaryKeyIncrement = tableIncriment get table
				flds.foreach((f) => {	
					val fieldName = getFieldName(f).toLowerCase
					val fn = Some(fieldName)
					val dataType = getType(f.replaceAll("`",""))
					var scalaType = getLookupType(dataType)
					println("FN "+fn.get.toLowerCase+" table_1_key "+table1_key.toLowerCase+"|")
					if ( fn.get.toLowerCase != table1_key.toLowerCase  ) {
					//	if ( fn.get.toLowerCase == table1_key.toLowerCase ) {
							//write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\", 'readonly -> \"readonly\")")
					//	} else {
							if (( scalaType == "java.sql.Timestamp") || ( scalaType == "java.sql.Date")) {
								write(writer, tab+tab+"@helper.inputDate(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
							} else {
								write(writer, tab+tab+"@helper.inputText(form2(\""+fieldName+"\"), '_label -> \""+fieldName.capitalize+"\")")
							}
					//	}
					}	
				})
			}
		}
		write(writer, tab+tab+"<button>Update</button>")
		write(writer, tab+"}")
		write(writer, "}")
		writer.close


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

