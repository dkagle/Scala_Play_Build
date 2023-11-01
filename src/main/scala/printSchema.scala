package ezbuilder {

import java.io._
import scala.io._
import sys.process._



class Schema(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, joinKeys: List[Map[List[String], List[String]]], primaryKeyIncrement: Option[Boolean] ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, primaryKeyIncrement) {

	def print() = {
		val mvcDir = "app/db/"
		val fileName = basedir+mvcDir+"Schema.scala"
//		println("FileName "+fileName)

		"mv "+fileName+" "+fileName+".back" !
		
		val writer = new PrintWriter(new File(fileName))
		for ( line <- Source.fromFile(fileName+".back").getLines) {
			if ( line.indexOf("AUTOMATION TAG") > 0 ) {
				
				writeObject(writer)
			}
			write(writer,line)
			
		}

		writer.close

	}


	def writeObject(writer: PrintWriter) {
		calculate()
		val itemModel = capitalizedObjectName+"Model"

		write(writer,tab+"import models."+itemModel)
		write(writer,tab+"class "+capitalizedObjectName+"(tag: Tag) extends Table["+itemModel+"](tag, \""+objectName+"\") {")

		fields.foreach((f) => {
			val fieldName = getFieldName(f)
			println("FIELDNAME "+fieldName)
			val dataType = getType(f)
//			println("FIELDNAME "+fieldName)
//			println("DATATYPE "+dataType)
			val fn = Some(fieldName)
			var length = dataType.length
			var scalaType = getLookupType(dataType)
			var andKeyWord = " and"
			var autotag = ""
			if ( f == fields.last ) 
				andKeyWord = ""
			if ( fn == primaryKey && primaryKeyIncrement.get ) {
				autotag=", O.AutoInc"
			} else
				autotag=""
			write(writer, tab+tab+"val "+fieldName.toLowerCase+" = column["+scalaType+"](\""+fieldName+"\""+autotag+")")		
		})

		write(writer,tab+tab+"override def * = ("+primaryKey.get.toLowerCase+", "+delimitedFields+") <> ("+itemModel+".tupled, "+itemModel+".unapply)")

		joinKeys.foreach((joinKey) => {
			for ( ( tables, keys ) <- joinKey )  {
				val table1 = tables.head
				val table2 = tables.tail.head
				val table1_key = keys.head
				val table2_key = keys.tail.head

				if ( table1 == objectName ) write(writer,tab+tab+"val "+table2.toLowerCase+"Key = foreignKey(\"SUP_FK\", "+table1_key.toLowerCase+", "+table2.toLowerCase+")(_."+table2_key.toLowerCase+")")
				

			}
		})		

		write(writer,tab+"}")
		write(writer, tab+"val "+lowerCaseObjectName+" = TableQuery["+capitalizedObjectName+"]")
		write(writer,tab+"")

	}

}
}



/*

  class Items(tag: Tag) extends Table[Item](tag, "ITEMS") {
    val id = column[Long]("ID", O.AutoInc)
    val name = column[String]("NAME")
    val price = column[Double]("PRICE")
    override def * = (id, name, price) <> (Item.tupled, Item.unapply)
  }
  val items = TableQuery[Items]

  object Items {
    implicit class ItemsExtensions[A](val q: Query[Items, A]) {
      val byId = Compiled { (id: Column[Long]) =>
        q.filter(_.id === id)
      }
    }

*/
