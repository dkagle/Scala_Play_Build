package ezbuilder {

import java.io._
import scala.io._
import scala.collection.immutable.HashMap
import sys.process._


class Joins(joinKey: Map[List[String],List[String]], basedir :String, schema : scala.collection.mutable.HashMap[String, List[String]], table1_fields: List[String], table2_fields: List[String]) extends scalaMVC {



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

		var mvcDir = "app/controllers/"
		var fileName=basedir+mvcDir+"HomeController.scala"
		
		"mv "+fileName+" "+fileName+".back" !

		val writer = new PrintWriter(new File(fileName))
		for ( line <- Source.fromFile(fileName+".back").getLines) {
			write(writer,line)
			if ( line.indexOf("val router = JavaScriptReverseRouter(\"routes\")(") > 0 ) {
				
				write(writer,tab+tab+tab+"routes.javascript."+table1Camelcase+table2Camelcase+"Controller.delete,")
			}			
		}


		writer.close

		mvcDir = "conf/"
		fileName = basedir+mvcDir+"routes"

		val fw = new FileWriter(fileName,true)

		fw.write("GET"+tab+tab+"/"+lowercase+"/$"+table1_key.toLowerCase+"<\\d+>"+tab+tab+tab+"controllers."+camelcase+"Controller.details("+table1_key+": "+PKdatatype+")\n")
		fw.write("GET"+tab+tab+"/"+lowercase+tab+tab+tab+tab+"controllers."+camelcase+"Controller.list\n")
		fw.write("+ nocsrf\n")
		fw.write("DELETE"+tab+tab+"/"+lowercase+"/:"+table1_key.toLowerCase+tab+tab+tab+tab+"controllers."+camelcase+"Controller.delete("+table1_key.toLowerCase+": "+PKdatatype+")\n")
		fw.write("+ nocsrf\n")
		fw.write("POST"+tab+tab+"/"+lowercase+tab+tab+tab+tab+tab+"controllers."+camelcase+"Controller.create\n")
		fw.write("+ nocsrf\n")
		fw.write("POST"+tab+tab+"/"+lowercase+"/update"+tab+tab+tab+tab+"controllers."+camelcase+"Controller.updatePost\n")
		fw.write("GET"+tab+tab+"/"+lowercase+"/add"+tab+tab+tab+tab+"controllers."+camelcase+"Controller.createForm\n")
		fw.write("GET"+tab+tab+"/"+lowercase+"/update/$"+table1_key.toLowerCase+"<\\d+>"+tab+tab+tab+"controllers."+camelcase+"Controller.updateForm("+table1_key.toLowerCase+": "+PKdatatype+")\n")
		fw.close
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

