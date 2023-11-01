package ezbuilder {

import java.io._
import scala.io._
import scala.collection.immutable.HashMap


class JoinCoffee(joinKey: Map[List[String],List[String]], basedir :String, schema : scala.collection.mutable.HashMap[String, List[String]]) extends scalaMVC {



def print() = {

	var counter = 0
	var table1 = ""
	var table2 = ""
	var table1_key = ""
	var table2_key = ""

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
		var mvcDir = "app/assets/javascripts/"

		var fileName=lowercase+".coffee"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))

		val tab = "  "

		write(writer, "require(['"+lowercase+"logic'], (Logic) ->")
		write(writer, tab+"for deleteBtn in document.querySelectorAll('button.delete-"+lowercase+"')")
		write(writer, tab+tab+"do (deleteBtn) ->  Logic(deleteBtn, deleteBtn.dataset."+table1_key.toLowerCase+")")
		write(writer, ")")
		writer.close()

		fileName=lowercase+"logic.coffee"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer, "define(['"+lowercase+"ui', 'routes'], (Ui, routes) ->")
		write(writer, tab+"(node, "+table1_key.toLowerCase+") ->")
		write(writer, tab+tab+"ui = Ui(node)")
		write(writer, tab+tab+"ui.forEachClick(() ->")
		write(writer, tab+tab+tab+"xhr = new XMLHttpRequest()")
		write(writer, tab+tab+tab+"route = routes.controllers."+camelcase+"Controller.delete("+table1_key.toLowerCase+")")
		write(writer, tab+tab+tab+"xhr.open(route.method, route.url)")
		write(writer, tab+tab+tab+"xhr.addEventListener('readystatechange', () ->")
		write(writer, tab+tab+tab+tab+"if xhr.readyState == XMLHttpRequest.DONE")
		write(writer, tab+tab+tab+tab+tab+"if xhr.status == 200")
		write(writer, tab+tab+tab+tab+tab+tab+"ui.delete()")
		write(writer, tab+tab+tab+tab+tab+"else")
		write(writer, tab+tab+tab+tab+tab+tab+"alert('Unable to delete the "+lowercase+"!')")
		write(writer, tab+tab+tab+")")
		write(writer, tab+tab+tab+"xhr.send()")
		write(writer, tab+tab+")")
		write(writer, ")")
		writer.close

		fileName=lowercase+"ui.coffee"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer, "define(() ->")
		write(writer, tab+"(node) ->")
		write(writer, tab+tab+"delete: () ->")
		write(writer, tab+tab+tab+"li = node.parentNode")
		write(writer, tab+tab+tab+"li.parentNode.removeChild(li)")
		write(writer, tab+tab+"forEachClick: (callback) ->")
		write(writer, tab+tab+tab+"node.addEventListener('click', callback)")
		write(writer, ")")
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

