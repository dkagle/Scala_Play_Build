package ezbuilder {

import java.io._
import scala.io._
import scala.collection.immutable.HashMap


class JoinModel(joinKey: Map[List[String],List[String]], basedir :String, schema : scala.collection.mutable.HashMap[String, List[String]] ) extends scalaMVC {



def print() = {

	var counter = 0
	var table1 = ""
	var table2 = ""
	var table1_key = ""
	var table2_key = ""

	for ( ( tables, keys ) <- joinKey )  {
		table1 = tables.head
		table2 = tables.tail.head
		table1_key = keys.head.toLowerCase
		table2_key = keys.tail.head.toLowerCase

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
		val modelsDir = "app/models/"
		val routesFile = "conf/routes"
		val viewDir = "app/views/"

		val fileName = camelcase+".scala"
		val writer = new PrintWriter(new File(basedir+modelsDir+fileName))
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
		write(writer,"")
		write(writer,tab+"def get("+table2_key+": "+PKdatatype+"): Option[("+table1Camelcase+"Model, "+table2Camelcase+"Model)] = {")	
		write(writer,tab+"val dbConfig = dbConfigProvider.get[JdbcProfile]")
		write(writer,tab+"def ds = dbConfig.db")
		write(writer,tab+tab+"val innerJoin = for {")
		write(writer,tab+tab+tab+"a <- "+table1lowercase)
		write(writer,tab+tab+tab+"b <- a."+table2lowercase+"Key"+".filter(_."+table2_key+" === "+table2_key+" )")
		write(writer,tab+tab+"} yield (a, b)")
		write(writer,tab+tab+"val action = innerJoin.result.headOption")
		write(writer,tab+tab+"println(\"SQL \"+action.statements.head)")
		write(writer,tab+tab+"val results = ds.run(action)")
		write(writer,tab+tab+"Await.result(results, 1.second)")
		write(writer,tab+"}")

		
		write(writer,tab+"def list(): Seq[("+table1Camelcase+"Model, "+table2Camelcase+"Model)] = {")
		write(writer,tab+"val dbConfig = dbConfigProvider.get[JdbcProfile]")
		write(writer,tab+"def ds = dbConfig.db")
		write(writer,tab+tab+"val innerJoin = for {")
		write(writer,tab+tab+tab+"a <- "+table1lowercase)
		write(writer,tab+tab+tab+"b <- a."+table2lowercase+"Key")
		write(writer,tab+tab+"} yield (a, b)")
		write(writer,tab+tab+"val action = innerJoin.result")
		write(writer,tab+tab+"println(\"SQL \"+action.statements.head)")
		write(writer,tab+tab+"val results = ds.run(action)")
		write(writer,tab+tab+"Await.result(results, 1.second)")		
		write(writer,tab+"}")

		write(writer,"}")
	
		writer.close()

	
	}
}

def getDataType(tableName: String, key: String):String = {
	var dataType = ""		

	for ( (table, flds) <- schema )  {
		if ( table == tableName ) {
			flds.foreach((f) => {
				val fieldName = getFieldName(f) 
				if ( fieldName.toLowerCase == key.toLowerCase ) {
					dataType = getType(f)	
				}
			})
		}
	}

	dataType
}



}  //class
} // package ezbuilder

