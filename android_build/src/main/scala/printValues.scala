package ezbuilder {

import java.io._
import scala.io._
import sys.process._


class Values(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val mvcDir = "values/"
	
		var fileName="strings.xml"
		var qualifiedFileName=basedir+mvcDir+fileName
		"mv "+qualifiedFileName+" "+qualifiedFileName+".back" !
		var writer = new PrintWriter(new File(qualifiedFileName))
		for ( line <- Source.fromFile(qualifiedFileName+".back").getLines) {
			
			if ( line.indexOf("</resources>") == 0 ) {
				
				printFields(writer)
				
			}

			write(writer,line)
			
		}

		writer.close

	}



	def printFields(writer: PrintWriter) = {

		calculate()
		write(writer,tab+"<string name=\""+capitalizedObjectName+"\">"+capitalizedObjectName+"</string>")
		write(writer,tab+"<string name=\"List"+capitalizedObjectName+"\">Click "+capitalizedObjectName+" to Update</string>")
		write(writer,tab+"<string name=\"Modify"+capitalizedObjectName+"\">Modify "+capitalizedObjectName+"</string>")
		write(writer,tab+"<string name=\"Add"+capitalizedObjectName+"\">Add "+capitalizedObjectName+"s</string>")
		write(writer,tab+"<string name=\"Choose"+capitalizedObjectName+"Delete\">Choose "+capitalizedObjectName+" to Delete</string>")
		write(writer,tab+"<string name=\"Delete"+capitalizedObjectName+"\">Delete this "+capitalizedObjectName+"?</string>")
		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val fn = Some(fieldName)			
			write(writer,tab+"<string name=\""+objectName+fn.get+"\">"+fn.get.capitalize+"</string>")

}
		

		
	}
}
}
