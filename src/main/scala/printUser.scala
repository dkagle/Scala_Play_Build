package ezbuilder {

import java.io._
import scala.io._
import sys.process._


class User(basedir: String,schema: scala.collection.mutable.HashMap[String, List[String]]) {
	var tab = "\t"
	def write(writer: PrintWriter, line: String) =
		writer.write(line+"\n")

	def print() {	
		
		var mvcDir = "app/controllers/"
		var fileName="Users.scala"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		for ( line <- Source.fromFile(fileName+".back").getLines) {
			if ( line.indexOf("delete") > 0 ) {
				
				writeObject(writer)
			}
			write(writer,line)
			
		}


	}



	def writeObject(writer: PrintWriter) {
		write(writer,tab+"def getbyUser(username: String, password: String): Option[UsersModel] =  { ")
		write(writer,tab+tab+"var returnresults: Option[UsersModel] = None")
		write(writer,tab+tab+"val byUser = users.filter(p => p.username === username && p.password === password)")
		write(writer,tab+tab+"val byPassword = users.filter(_.password === password)")
		write(writer,tab+tab+"val action = byUser.result.headOption")
		write(writer,tab+tab+"println(\"SQL \"+action.statements.head)")
		write(writer,tab+tab+"val results: Future[Option[UsersModel]] = ds.run(action)")
		write(writer,tab+tab+"Await.result(results, 1.second)")
		write(writer,tab+"}")

	}

}
}
