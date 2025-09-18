
import ezbuilder._
import scala.io._
import sys.process._


object androidbuild extends App {
var schema = new scala.collection.mutable.HashMap[String, List[String]]
var schemas = new scala.collection.mutable.ListBuffer[Map[String, List[String]]]
var tableKey = new scala.collection.mutable.HashMap[String, String]
var tableKeys = new scala.collection.mutable.ListBuffer[Map[String, String]]
var joinKeys = new scala.collection.mutable.ListBuffer[Map[List[String], List[String]]]
var fields = new scala.collection.mutable.ListBuffer[String]
val basedir = sys.env("ANDROIDBUILD")+"/src/main/"
val gradledir= sys.env("ANDROIDBUILD")+"/"
val javadir = basedir+"java/com/scalaitoinfinity/androidbuild/"
val resdir = basedir+"res/"
//val url = sys.env("URL")
val url="http://192.168.1.75:9000/";
val filesHere = ( new java.io.File(basedir)).listFiles
val createTableTag = "CREATE TABLE"
val primaryKeyTag = "PRIMARY KEY"

val space = ' '
val spaceLength = 1
var tableName = ""
var key = ""
var primaryKey = Option("")
var start = 0
var end = 0




// main  

for ( file <- filesHere; if file.getName.endsWith(".sql") )
	for ( line <- fileLines(file) ) 
		parseLine(line)
	printSchemas()




def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines().toList
def printFields(fields: List[String], tableName: String) = for ( field <- fields)
				println("TABLENAME "+ tableName +" FIELD "+field)
def printSchemas() = {
	val staticValues = new StaticValues(resdir)
	staticValues.print 
	val staticMain = new StaticMainActivity( javadir, schemas )
	staticMain.print 
	val staticLayout = new StaticLayout(resdir, schemas )
	staticLayout.print
	val staticAndroidManifest = new StaticAndroidManifest(basedir)
	staticAndroidManifest.print 
	val buildGradle = new BuildGradle(gradledir)
	buildGradle.print 
	for ( schema <- schemas ) {
		for ( (table, flds) <- schema )  {
			for ( f <- flds ) {
				println("TABLE "+table+" FIELD "+f)	
				primaryKey = tableKey get table
			}
			val values = new Values(table.toLowerCase, flds, primaryKey, resdir, url)
			values.print			
			val androidManifest = new AndroidManifest(table.toLowerCase, flds, primaryKey, basedir, url)
			androidManifest.print
			val menu = new Menu(table.toLowerCase, flds, primaryKey, resdir, url)
			menu.print 

			val layout = new Layout(table.toLowerCase, flds, primaryKey, resdir, url)
			layout.print
			val listActivity = new ListActivity(table.toLowerCase, flds, primaryKey, javadir, url)
			listActivity.print
			val deleteListActivity = new DeleteListActivity(table.toLowerCase, flds, primaryKey, javadir, url)
			deleteListActivity.print
			val add = new AddActivity(table.toLowerCase, flds, primaryKey, javadir, url)
			add.print
			val form = new FormActivity(table.toLowerCase, flds, primaryKey, javadir, url)
			form.print
			val deleteForm = new DeleteFormActivity(table.toLowerCase, flds, primaryKey, javadir, url)
			deleteForm.print
			val mainForm = new MainActivity(table.toLowerCase, flds, primaryKey, javadir, url)
			mainForm.print  
			
			
		}
	}
	var fileName=resdir+"menu/main.xml.back"

	"rm "+fileName !

	fileName=resdir+"values/strings.xml.back"

	"rm "+fileName !			

}



def foundCreateTable(line: String) = {
	val objectStart = line.indexOf(createTableTag)+createTableTag.length()+spaceLength
	val objectEnd = line.indexOf(space,objectStart)
	val objectName = line.substring(objectStart,objectEnd)
	fields = new scala.collection.mutable.ListBuffer[String]
	schema = new scala.collection.mutable.HashMap[String, List[String]]
	tableName = objectName
    println("PARSER CREATE TABLE "+objectName) 
}

def foundPrimaryKey(line: String) = {
	start = 0
	end = line.indexOf(' ', start)
	primaryKey = Option(line.substring(start,end).trim)
	println("PARSER PRIMARY KEY "+primaryKey)

}

def foundForiegnKey(line: String) = {
	var tables = new scala.collection.mutable.ListBuffer[String]
	var keys = new scala.collection.mutable.ListBuffer[String]
	var joinKey = new scala.collection.mutable.HashMap[List[String], List[String]]
	start = line.indexOf('(')+1
	end = line.indexOf(')')
	val foreignKey = line.substring(start,end).trim
	tables += tableName
	keys += foreignKey
	println("PARSER FORIEGN KEY "+tableName+"|"+foreignKey)
	start = line.indexOf("REFERENCES")+11
	end = line.indexOf('(', start)
	val foreignTable = line.substring(start,end).trim

	start = end + 1
	end = line.indexOf(')',start)
	val foreignTableKey = line.substring(start,end).trim

	tables += foreignTable
	keys += foreignTableKey
	println("PARSER FORIEGN KEY "+foreignTable+"|"+foreignTableKey)

	joinKey += ( tables.toList -> keys.toList )
	joinKeys += joinKey.toMap



}


def foundFieldName(line: String) = {
	println("PARSER FIELD "+line)
	fields += line
	
}

def foundEndTable() = {
	println("PARSER End Table "+tableName)
	schema += ( tableName -> fields.toList )
	schemas += schema.toMap
	tableKey += ( tableName-> primaryKey.get )
	tableKeys += tableKey.toMap
}



var parseTable=false
def parseLine(line: String) = {
		if (line.trim.startsWith(createTableTag)) {
			foundCreateTable(line)
			parseTable = true
		} else if (line.trim.startsWith(")")) {
			foundEndTable()
			parseTable=false
		} else if (line.trim.startsWith("PRIMARY KEY")) {
			foundPrimaryKey(line)
		} else if (line.trim.startsWith("FOREIGN KEY")) {
			
			foundForiegnKey(line)
		} else {
			if ( parseTable ) {
				foundFieldName(line)
				if (line.indexOf(primaryKeyTag) > 0 ) 
					foundPrimaryKey(line)

			}
}		}




}	



