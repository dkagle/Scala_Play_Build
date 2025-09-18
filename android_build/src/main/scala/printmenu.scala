package ezbuilder {

import java.io._
import scala.io._
import sys.process._


class Menu(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val mvcDir = "menu/"
	
		var fileName=objectName+"main.xml"
		var qualifiedFileName=basedir+mvcDir+fileName
		var writer = new PrintWriter(new File(qualifiedFileName))
		write(writer,"<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">")
    	write(writer,tab+"<item android:id=\"@+id/update_action\"")
	    write(writer,tab+tab+"android:showAsAction=\"ifRoom|withText\"")
        write(writer,tab+tab+"android:title=\"@string/update\" />")
    	write(writer,tab+"<item android:id=\"@+id/add_action\"")
        write(writer,tab+tab+"android:showAsAction=\"ifRoom|withText\"")
        write(writer,tab+tab+"android:title=\"@string/add\" />")
    	write(writer,tab+"<item android:id=\"@+id/delete_action\"")
        write(writer,tab+tab+"android:showAsAction=\"ifRoom|withText\"")
        write(writer,tab+tab+"android:title=\"@string/delete\" />")
		write(writer,"</menu>")
		writer.close()
	
		fileName="main.xml"
		qualifiedFileName=basedir+mvcDir+fileName

		"mv "+qualifiedFileName+" "+qualifiedFileName+".back" !

		writer = new PrintWriter(new File(qualifiedFileName))


		


		writer = new PrintWriter(new File(qualifiedFileName))
		for ( line <- Source.fromFile(qualifiedFileName+".back").getLines) {
			if ( line.indexOf("</menu>") == 0 ) {
				write(writer,tab+"<item android:id=\"@+id/"+capitalizedObjectName+"\"")
				write(writer,tab+tab+"android:showAsAction=\"ifRoom|withText\"")
				write(writer,tab+tab+"android:title=\"@string/"+capitalizedObjectName+"\" />")
			} 

			write(writer,line)
			
		}

		writer.close

		
	}
	
}
}

