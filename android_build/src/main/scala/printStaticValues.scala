package ezbuilder {

import java.io._
import scala.io._
import sys.process._


class StaticValues(basedir: String ) extends scalaMVC {

	def print() = {
		val mvcDir = "values/"
		val tab = "\t"
		var fileName="strings.xml"	
		var qualifiedFileName=basedir+mvcDir+fileName
		"mv "+qualifiedFileName+" "+qualifiedFileName+".back" !
		var writer = new PrintWriter(new File(qualifiedFileName))
		for ( line <- Source.fromFile(qualifiedFileName+".back").getLines) {
			
			if ( line.indexOf("</resources>") == 0 ) {
				
				printObjectStrings(writer)
				
			}

			write(writer,line)
			
		}

		writer.close


		fileName="colors.xml"
		qualifiedFileName=basedir+mvcDir+fileName
		writer = new PrintWriter(new File(qualifiedFileName))
		
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<resources>")
		write(writer,tab+"<color name=\"colorPrimary\">#1f1f81</color>")
		write(writer,tab+"<color name=\"colorPrimaryDark\">#141415</color>")
		write(writer,tab+"<color name=\"colorPrimaryLight\">#505050</color>")
		write(writer,tab+"<color name=\"colorAccent\">#505050</color>")
		write(writer,"</resources>")

		writer.close

	}

	def printObjectStrings(writer: PrintWriter) = {
		val tab = "\t"
//		write(writer,tab+"<string name=\"app_name\">AndroidBuild</string>")
		write(writer,tab+"<string name=\"update\">Update</string>")
		write(writer,tab+"<string name=\"add\">Add</string>")
		write(writer,tab+"<string name=\"delete\">Delete</string>")
		write(writer,tab+"<string name=\"connection_error\">Connection error.</string>")

	}



}
}
