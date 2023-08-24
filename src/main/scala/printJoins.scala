package ezbuilder {

import java.io._
import scala.io._
import scala.collection.immutable.HashMap


class Joins(joinKey: Map[List[String],List[String]], basedir :String, schema : scala.collection.mutable.HashMap[String, List[String]] ) extends scalaMVC {



def print() = {

	var counter = 0
	var table1 = ""
	var table2 = ""
	var table1_key = ""
	var table2_key = ""

	for ( ( tables, keys ) <- joinKey )  {
		table1 = tables.head
		table2 = tables.tail.head
		table1_key = keys.head
		table2_key = keys.tail.head

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
		val controllerDir = "app/controllers/"
		val modelsDir = "app/models/"
		val routesFile = "conf/routes"
		var fileName = camelcase+"Controller.scala"
		var writer = new PrintWriter(new File(basedir+controllerDir+fileName))
//		println("FILENAME "+basedir+controllerDir+fileName)

		write(writer,"package controllers")
		write(writer,"")
		write(writer,"import com.google.inject.Inject")
		write(writer,"import play.api.mvc.{Action, AbstractController, ControllerComponents}") 
		write(writer,"import play.api.mvc.Result")
		write(writer,"import play.api.libs.json._")
		write(writer,"import models."+table1Camelcase+"Model")
		write(writer,"import models."+table2Camelcase+"Model")
			
		write(writer,"class "+camelcase+"Controller @Inject()(components: ControllerComponents)("+table1lowercase+table2lowercase+": models."+camelcase+")("+table1lowercase+": models."+table1Camelcase+")("+table2lowercase+": models."+table2Camelcase+") extends AbstractController(components) {")
		write(writer,tab+"implicit val writes"+table1Camelcase+" = Json.writes["+table1Camelcase+"Model]")
		write(writer,tab+"implicit val writes"+table2Camelcase+" = Json.writes["+table2Camelcase+"Model]")


		write(writer,tab+"def details("+table1_key+": "+PKdatatype+") = Action {")
		write(writer,tab+tab+lowercase+".get("+table1_key+") match {")
		write(writer,tab+tab+tab+"case Some(("+table1lowercase+","+table2lowercase+")) => Ok(\"[\"+Json.toJson("+table1lowercase+").toString+Json.toJson("+table2lowercase+").toString+\"]\")")
		write(writer,tab+tab+tab+"case None => NotFound(\"Record Not Found\")")
		write(writer,tab+tab+tab+"}")
		write(writer,tab+"}")
		write(writer,"}")

		writer.close()

		fileName = camelcase+".scala"
		writer = new PrintWriter(new File(basedir+modelsDir+fileName))
		write(writer,"package models")
		write(writer,"import db.Schema.queryLanguage._")
		write(writer,"import db.Schema."+table1lowercase)
		write(writer,"import db.Schema."+table2lowercase)
		write(writer,"import com.google.inject.Inject")
		write(writer,"import play.api.db.slick.DatabaseConfigProvider")
		write(writer,"import slick.jdbc.JdbcProfile")
		write(writer,"import play.api._")
		write(writer,"import play.api.inject._")
		write(writer,"import play.api.libs.concurrent.Execution.Implicits.defaultContext")

		write(writer,"class "+camelcase+" @Inject()(dbConfigProvider: DatabaseConfigProvider)(lifecycle: DefaultApplicationLifecycle) {")
		
		write(writer,tab+"import scala.util.{Failure,Success}")
		write(writer,tab+"import scala.concurrent.ExecutionContext.Implicits.global")
		write(writer,tab+"import scala.concurrent.{Await,Future}")
		write(writer,tab+"import scala.concurrent.duration._")

		write(writer,tab+"def get("+table1_key+": "+PKdatatype+"): Option[("+table1Camelcase+"Model, "+table2Camelcase+"Model)] = {")	
		write(writer,tab+"val dbConfig = dbConfigProvider.get[JdbcProfile]")
		write(writer,tab+"def ds = dbConfig.db")
		write(writer,tab+tab+"val innerJoin = for {")
		write(writer,tab+tab+tab+"a <- "+table1lowercase)
		write(writer,tab+tab+tab+"b <- a."+table2lowercase+"Key")
		write(writer,tab+tab+"} yield (a, b)")
		write(writer,tab+tab+"val action = innerJoin.result.headOption")
		write(writer,tab+tab+"println(\"SQL \"+action.statements.head)")
		write(writer,tab+tab+"val results = ds.run(action)")
		write(writer,tab+tab+"Await.result(results, 1.second)")
		write(writer,tab+tab+"}")
		write(writer,"}")
	
		writer.close()

		val mvcDir = "conf/"
		fileName = basedir+mvcDir+"routes"

		val fw = new FileWriter(fileName,true)

		fw.write("GET"+tab+tab+"/"+lowercase+"/$"+table2_key+"<\\d+>"+tab+tab+tab+tab+"controllers."+camelcase+"Controller.details("+table2_key+": "+PKdatatype+")\n")
		fw.close
	}
}

def getDataType(tableName: String, key: String):String = {
	var dataType = ""		

	for ( (table, flds) <- schema )  {
		if ( table == tableName ) {
			for ( f <- flds ) {
				val fieldName = getFieldName(f) 
				if ( fieldName == key ) {
					dataType = getType(f)	
				}
			}
		}
	}

	dataType
}



}  //class
} // package ezbuilder

