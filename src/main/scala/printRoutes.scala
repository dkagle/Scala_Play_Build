package ezbuilder {

import java.io._
import scala.io._
import sys.process._



class Routes(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String) extends scalaMVCFile(objectName, fields, primaryKey, basedir)  {
	def print() = {
		val mvcDir = "conf/"
		val fileName = basedir+mvcDir+"routes"
		"mv "+fileName+" "+fileName+".back" !

		val writer = new PrintWriter(new File(fileName))
		for ( line <- Source.fromFile(fileName+".back").getLines) {

//			println(line)
			write(writer,line)
			
		}
		writeObject(writer)
		writer.close



	}


	def writeObject(writer: PrintWriter) {
		calculate()
		write(writer,"GET"+tab+tab+"/"+lowerCaseObjectName+tab+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.list")
		write(writer,"+ nocsrf")
		write(writer,"POST"+tab+tab+"/"+lowerCaseObjectName+tab+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.create")
		write(writer,"+ nocsrf")
		write(writer,"POST"+tab+tab+"/"+lowerCaseObjectName+"/update"+tab+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.updatePost")
		write(writer,"GET"+tab+tab+"/"+lowerCaseObjectName+"/update/$"+primaryKey.get.toLowerCase+"<\\d+>"+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.updateForm("+primaryKey.get.toLowerCase+": Int)")
		write(writer,"GET"+tab+tab+"/"+lowerCaseObjectName+"/add"+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.createForm")
		write(writer,"GET"+tab+tab+"/"+lowerCaseObjectName+"/$"+primaryKey.get.toLowerCase+"<\\d+>"+tab+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.details("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+")")

		write(writer,"PUT"+tab+tab+"/"+lowerCaseObjectName+"/:"+primaryKey.get.toLowerCase+"/"+routesString+tab+tab+"controllers."+capitalizedObjectName+"Controller.update("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+", "+routesFieldString+")")
		write(writer,"+ nocsrf")
		write(writer,"DELETE"+tab+tab+"/"+lowerCaseObjectName+"/:"+primaryKey.get.toLowerCase+tab+tab+tab+"controllers."+capitalizedObjectName+"Controller.delete("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+")")
		
	}

}
}



