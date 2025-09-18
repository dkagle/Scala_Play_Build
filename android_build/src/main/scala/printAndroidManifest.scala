package ezbuilder {

import java.io._
import scala.io._
import sys.process._



class AndroidManifest(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val fileName = "AndroidManifest.xml"
		val qualifiedFileName=basedir+fileName
		"mv "+qualifiedFileName+" "+qualifiedFileName+".back" !
		var writer = new PrintWriter(new File(qualifiedFileName))
		for ( line <- Source.fromFile(qualifiedFileName+".back").getLines) {
	
			if ( line.indexOf("	 </application>") == 0 ) {
				
				printFields(writer)
				
			} 

			write(writer,line)
			
		}

		writer.close

	}


	def printFields(writer: PrintWriter) = {

		calculate()

		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"MainActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/"+capitalizedObjectName+"\"")
		write(writer,tab+tab+tab+"android:parentActivityName=\"com.scalaitoinfinity.androidbuild.MainActivity\" >")
		write(writer,tab+tab+tab+"<meta-data")
		write(writer,tab+tab+tab+tab+"android:name=\"android.support.PARENT_ACTIVITY\"")
		write(writer,tab+tab+tab+tab+"android:value=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"MainActivity\" />")
		write(writer,tab+tab+"</activity>")
		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"AddActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/Add"+capitalizedObjectName+"\"")
		write(writer,tab+tab+tab+"android:parentActivityName=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"MainActivity\" >")
		write(writer,tab+tab+tab+"<meta-data")
		write(writer,tab+tab+tab+tab+"android:name=\"android.support.PARENT_ACTIVITY\"")
		write(writer,tab+tab+tab+tab+"android:value=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"AddActivity\" />")
		write(writer,tab+tab+"</activity>")
		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"ListActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/List"+capitalizedObjectName+"\"")
		write(writer,tab+tab+tab+"android:parentActivityName=\"com.scalaitoinfinity.androidbuild.MainActivity\" >")
		write(writer,tab+tab+tab+"<meta-data")
		write(writer,tab+tab+tab+tab+"android:name=\"android.support.PARENT_ACTIVITY\"")
		write(writer,tab+tab+tab+tab+"android:value=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"ListActivity\" />")
		write(writer,tab+tab+"</activity>")
		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"DeleteListActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/Choose"+capitalizedObjectName+"Delete\"")
		write(writer,tab+tab+tab+"android:parentActivityName=\"com.scalaitoinfinity.androidbuild.MainActivity\" >")
		write(writer,tab+tab+tab+"<meta-data")
		write(writer,tab+tab+tab+tab+"android:name=\"android.support.PARENT_ACTIVITY\"")
		write(writer,tab+tab+tab+tab+"android:value=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"DeleteListActivity\" />")
		write(writer,tab+tab+"</activity>")
		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"FormActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/Modify"+capitalizedObjectName+"\"")
		write(writer,tab+tab+tab+"android:parentActivityName=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"MainActivity\" >")
		write(writer,tab+tab+tab+"<meta-data")
		write(writer,tab+tab+tab+tab+"android:name=\"android.support.PARENT_ACTIVITY\"")
		write(writer,tab+tab+tab+tab+"android:value=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"FormActivity\" />")
		write(writer,tab+tab+"</activity>")
		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"DeleteFormActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/Delete"+capitalizedObjectName+"\"")
		write(writer,tab+tab+tab+"android:parentActivityName=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"MainActivity\" >")
		write(writer,tab+tab+tab+"<meta-data")
		write(writer,tab+tab+tab+tab+"android:name=\"android.support.PARENT_ACTIVITY\"")
		write(writer,tab+tab+tab+tab+"android:value=\"com.scalaitoinfinity.androidbuild."+capitalizedObjectName+"DeleteFormActivity\" />")
		write(writer,tab+tab+"</activity>")
	}
}

}
