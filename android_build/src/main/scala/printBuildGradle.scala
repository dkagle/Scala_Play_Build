package ezbuilder {

import java.io._
import scala.util.control.Breaks._
import scala.io._
import sys.process._


class BuildGradle( basedir: String) extends scalaMVC()   {
		def print() = {
		val fileName = "build.gradle"
		val qualifiedFileName=basedir+fileName
		var dependency=false
		"mv "+qualifiedFileName+" "+qualifiedFileName+".back" !
		var writer = new PrintWriter(new File(qualifiedFileName))
		for ( line <- Source.fromFile(qualifiedFileName+".back").getLines) {
			if ( line.indexOf("dependencies {") == 0 ) {
				dependency=true

			}

			if ( dependency) {
				if ( line.indexOf("}") == 0 ) {
					printFields(writer)
				}
			}
			write(writer,line)	
		}

		writer.close

	}


	def printFields(writer: PrintWriter) = {
		val tab = "\t"

		write(writer,tab+"compile 'com.android.support:gridlayout-v7:23.3.0'")
		write(writer,tab+"compile 'com.android.support:cardview-v7:23.3.0'")
		write(writer,tab+"compile 'com.google.android.gms:play-services-appindexing:8.1.0'")
		write(writer,tab+"compile 'org.kie.modules:org-apache-commons-httpclient:6.2.0.CR2'")
		write(writer,tab+"compile 'org.apache.commons:commons-parent:40'")
		write(writer,tab+"compile 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2'")

	}
}

}
