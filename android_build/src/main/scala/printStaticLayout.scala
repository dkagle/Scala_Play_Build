package ezbuilder {

import java.io._
import scala.io._
import sys.process._


class StaticLayout(basedir: String, schemas: scala.collection.mutable.ListBuffer[Map[String, List[String]]]) extends scalaMVC()   {

	def print() = {
		var mvcDir = "layout/"
		var previousField = ""
		val fileName="main.xml"
		var qualifiedFileName=basedir+mvcDir+fileName

		"mkdir "+basedir+mvcDir !

		var writer = new PrintWriter(new File(qualifiedFileName))
		val tab = "\t"
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<LinearLayout")
    	write(writer,tab+"xmlns:android=\"http://schemas.android.com/apk/res/android\"")
    	write(writer,tab+"android:orientation=\"vertical\"")
	    write(writer,tab+"android:layout_width=\"fill_parent\"")
    	write(writer,tab+"android:layout_height=\"fill_parent\">")
		write(writer,"")
    	write(writer,tab+"<View")
        write(writer,tab+tab+"android:layout_width=\"fill_parent\"")
        write(writer,tab+tab+"android:layout_height=\"1dp\"")
        write(writer,tab+tab+"android:background=\"@color/colorPrimaryDark\"/>")
		write(writer,"")
 

		write(writer,"</LinearLayout>")
		writer.close()
		mvcDir="menu/"
		qualifiedFileName=basedir+mvcDir+fileName

		"mkdir "+basedir+mvcDir !

		println("MENU FILENAME "+qualifiedFileName)
		writer = new PrintWriter(new File(qualifiedFileName))
		
		write(writer,"<menu xmlns:android=\"http://schemas.android.com/apk/res/android\">")


		write(writer,"</menu>")

		writer.close
	}



		
}
}
