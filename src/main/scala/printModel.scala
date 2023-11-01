package ezbuilder {

import java.io._

//trait scalaMVC {
//	def print
//}

class Model(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, authTable: String, authUserField: String, authPWField: String, primaryKeyIncrement: Option[Boolean] ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, primaryKeyIncrement)  {

	def print() = {
		calculate()
		println("PK DATA TYPE "+primaryKeyDataType)
		println("PK "+primaryKey.get.toLowerCase)
		val mvcDir = "app/models/"
		var fileName = capitalizedObjectName+".scala"
		val writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		
		val itemModel = capitalizedObjectName+"Model"
		write(writer,"package models")
		write(writer,"")
		write(writer,"import db.Schema.queryLanguage._")
		write(writer,"import db.Schema."+lowerCaseObjectName)

		write(writer,"import com.google.inject.{Inject, Provider}")
		write(writer,"import play.db.NamedDatabase")
		write(writer,"import play.api.db.slick.DatabaseConfigProvider")
		write(writer,"import slick.jdbc.JdbcProfile")
		write(writer,"import play.api._")
		write(writer,"import play.api.inject._")
		write(writer,"import play.api.libs.concurrent.Execution.Implicits.defaultContext")
		write(writer,"import akka.actor._")
		write(writer,"")
		write(writer,"case class "+itemModel+"("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+", "+fieldString+")")
		write(writer,"trait "+capitalizedObjectName+"Trait {")
		write(writer,tab+"def list: Seq["+itemModel+"]")
		write(writer,tab+"def create("+createModelFieldString+"): Int")
		write(writer,tab+"def get("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+"): Option["+itemModel+"]")
		write(writer,tab+"def update("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+", "+fieldString+"): Int")
		write(writer,tab+"def delete("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+"): Int")
		write(writer,"}")
		write(writer,"")
		write(writer,"class "+capitalizedObjectName+" @Inject()(dbConfigProvider: DatabaseConfigProvider)(lifecycle: DefaultApplicationLifecycle)(system: ActorSystem)   extends "+capitalizedObjectName+"Trait {")
		write(writer,"")
		
		write(writer,tab+"import scala.util.{Failure,Success}")
		write(writer,tab+"import scala.concurrent.ExecutionContext.Implicits.global")
		write(writer,tab+"import scala.concurrent.{Await,Future}")
		write(writer,tab+"import scala.concurrent.duration._")
		write(writer,tab+"import slick.lifted.{Tag, TableQuery}")
		write(writer,tab+"")


		write(writer,tab+"implicit val jdbc = system.dispatchers.lookup(\"jdbc-execution-context\")")
		write(writer,tab+"val dbConfig = dbConfigProvider.get[JdbcProfile]")
		write(writer,tab+"def ds = dbConfig.db")
		write(writer,tab+"def list: Seq["+itemModel+"] = {")
		write(writer,tab+tab+"val result = "+lowerCaseObjectName+".result")
		write(writer,tab+tab+"println(\"SQL \"+result.statements.head)")
		write(writer,tab+tab+"val returns = ds.run(result)")
		write(writer,tab+tab+"val results: Seq["+itemModel+"] = Seq["+itemModel+"]()")
		write(writer,tab+tab+"Await.result(returns, 1.second )")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"def create("+createModelFieldString+"): Int = {")
		if ( primaryKeyIncrement.get ) 
			write(writer,tab+tab+"val insertActions = (("+lowerCaseObjectName+" returning "+lowerCaseObjectName+".map(_."+primaryKey.get.toLowerCase+")) += "+itemModel+"("+createModelFieldString+"))")
		else
			write(writer,tab+tab+"val insertActions = ("+lowerCaseObjectName+" += "+itemModel+"("+modelFields+"))")
		write(writer,tab+tab+"println(\"SQL \"+"+lowerCaseObjectName+".insertStatement)")
		write(writer,tab+tab+"val results = ds.run(insertActions)")
		write(writer,tab+tab+"Await.result(results, 1.second).asInstanceOf[Int]")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"def get("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+"): Option["+itemModel+"] =  { ")
		write(writer,tab+tab+"var returnresults: Option["+itemModel+"] = None")
		write(writer,tab+tab+"val byid = "+lowerCaseObjectName+".filter(_."+primaryKey.get.toLowerCase+" === "+primaryKey.get.toLowerCase+")")
		write(writer,tab+tab+"val action = byid.result.headOption")
		write(writer,tab+tab+"println(\"SQL \"+action.statements.head)")
		write(writer,tab+tab+"val results: Future[Option["+itemModel+"]] = ds.run(action)")
		write(writer,tab+tab+"Await.result(results, 1.second)")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"def update("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+", "+fieldString+"): Int = { ")
//		write(writer,tab+tab+"val updated = "+lowerCaseObjectName+".insertOrUpdate("+itemModel+"("+primaryKey.get+", "+fieldString+"))")
//		write(writer,tab+tab+"println(\"SQL \"+updated.statements.head)")
		write(writer,tab+tab+"val byid = "+lowerCaseObjectName+".filter(_."+primaryKey.get.toLowerCase+" === "+primaryKey.get.toLowerCase+")")
		write(writer,tab+tab+"val "+lowerCaseObjectName+"Map = byid.map(x => ("+xFieldString+"))")
		write(writer,tab+tab+"val updated = "+lowerCaseObjectName+"Map.update("+delimitedFields+")")
		write(writer,tab+tab+"val results = ds.run(updated)")
		write(writer,tab+tab+"Await.result(results, 1.second)")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"def delete("+primaryKey.get.toLowerCase+": "+primaryKeyDataType+"): Int = {")
		write(writer,tab+tab+"val del = "+lowerCaseObjectName+".filter(_."+primaryKey.get.toLowerCase+" === "+primaryKey.get.toLowerCase+" )")
		write(writer,tab+tab+"val action = del.delete")
		write(writer,tab+tab+"println(\"SQL \"+action.statements.head)")
		write(writer,tab+tab+"val results = ds.run(action)")
		write(writer,tab+tab+"Await.result(results, 1.second)")

	
		write(writer,tab+"}")
		if ( lowerCaseObjectName == authTable.toLowerCase ) {
			write(writer, tab+"def getbyUser("+authUserField.toLowerCase+": String, "+authPWField.toLowerCase+": String): Option["+authTable.toLowerCase.capitalize+"Model] =  { ")
			write(writer, tab+tab+"var returnresults: Option["+authTable.toLowerCase.capitalize+"Model] = None")
			write(writer, tab+tab+"val byUser = "+authTable.toLowerCase+".filter(p => p."+authUserField.toLowerCase+" === "+authUserField.toLowerCase+" && p."+authPWField.toLowerCase+" === "+authPWField.toLowerCase+")")
			write(writer, tab+tab+"val byPassword = "+authTable.toLowerCase+".filter(_."+authPWField.toLowerCase+" === "+authPWField.toLowerCase+")")
			write(writer, tab+tab+"val action = byUser.result.headOption")
			write(writer, tab+tab+"println(\"SQL \"+action.statements.head)")
			write(writer, tab+tab+"val results: Future[Option["+authTable.toLowerCase.capitalize+"Model]] = ds.run(action)")
			write(writer, tab+tab+"Await.result(results, 1.second)")
			write(writer, tab+"}")
		}
		write(writer,"}")
		writer.close()
	}

}
}

