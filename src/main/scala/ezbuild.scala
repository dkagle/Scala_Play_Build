
package ezbuilder
import ezbuilder._


class Ezbuild( ) {
var schema = new scala.collection.mutable.HashMap[String, List[String]]
var filterSchema = new scala.collection.mutable.HashMap[String, List[String]]
var schemas = new scala.collection.mutable.ListBuffer[Map[String, List[String]]]
var tableKey = new scala.collection.mutable.HashMap[String, String]
var tableKeys = new scala.collection.mutable.ListBuffer[Map[String, String]]
var joinKeys = new scala.collection.mutable.ListBuffer[Map[List[String], List[String]]]
var fields = new scala.collection.mutable.ListBuffer[String]


var flds = new scala.collection.mutable.ListBuffer[String]
var table = ""

val evolutionDir = "conf/evolutions/default"
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
def getTables(): List[String] = {
	var tempTables = new scala.collection.mutable.ListBuffer[String]
	for ( (table, flds) <- schema )  {
		tempTables += table
	}

	tempTables.toList
} 


def getFields(selectedTable: String): List[String] = {
	var fields = new scala.collection.mutable.ListBuffer[String]
	for ( (table, flds) <- schema )  {
		if ( table == selectedTable ) {
			for ( f <- flds ) {
				println(getFieldName(f))
				fields += getFieldName(f)
			}
		}
	}
	fields.toList
}

def getFilterFields(selectedTable: String): List[String] = {
	var fields = new scala.collection.mutable.ListBuffer[String]
	for ( (table, flds) <- filterSchema )  {
		if ( table == selectedTable ) {
			for ( f <- flds ) {
				println(getFieldName(f))
				fields += getFieldName(f)
			}
		}
	}
	fields.toList
}

def getNoPKFields(selectedTable: String): List[String] = {
	var fields = new scala.collection.mutable.ListBuffer[String]
	for ( (table, flds) <- schema )  {
		if ( table == selectedTable ) {
			for ( f <- flds ) {
				println(getFieldName(f))
				if ( !f.contains("PRIMARY KEY") )
					fields += getFieldName(f)
			}
		}
	}
	fields.toList
}

def getPrimaryKeyField(selectedTable: String): String = {
	var pk = ""
	for ( (table, flds) <- schema )  {
		if ( selectedTable == table ) {
			for ( f <- flds ) {
				if ( f.contains("PRIMARY KEY") )
					pk = f
			}
		}
	}
	pk
}

def rebuildSchema(filterTable: String, filterFields: List[String]) {
	var tempSchema = new scala.collection.mutable.HashMap[String, List[String]]
	var tempFields = new scala.collection.mutable.ListBuffer[String]
	println("REBUILDSCHEMA "+filterTable)
	tempFields += getPrimaryKeyField(filterTable)
	for ( filterField <- filterFields ) {
		for ( (table,fields) <- filterSchema )  {
			if ( table == filterTable ) {
				for ( f <- fields ) {
					if ( getFieldName(f) == filterField ) 
						tempFields += f
				}
			}
		}
	}
	for ( (table,fields) <- filterSchema )  {
		println("table "+table)
		if ( table == filterTable ) {
			("REBUILDING "+table)
			tempSchema += (table -> tempFields.toList)
		} else {
			("COPYING "+table)
			tempSchema += (table -> fields)
		}
	}	
	filterSchema=tempSchema
}

def updateSchema(selectTables: List[String]) = {
	filterSchema.clear					
	println("UPDATE SCHEMA")
	for ( selectTable <- selectTables ) {
		println("SELECT TABLE "+selectTable)
		for ( (table, flds) <- schema )  {
			if ( selectTable == table )
				filterSchema += ( table -> flds )
		}
	}				 
}


def printFields(fields: List[String], tableName: String) = for ( field <- fields)
				println("TABLENAME "+ tableName +" FIELD "+field)
def duplicateSchema() = {

	for ( (table, flds) <- schema )  {
		filterSchema += ( table -> flds )
	}
}

def dummyPrintSchemas( ) = {
		println("DP PRINT SCHEMAS")
		for ( (table, flds) <- filterSchema )  {
			println("DP TABLE "+ table)
			for ( f <- flds ) {
				println("DP TABLE "+table+" FIELD "+f)	
				primaryKey = tableKey get table
			}
			


		}
			
}

def dummyPrintSchemas2( ) = {
		println("DP2 PRINT SCHEMAS")
		for ( (table, flds) <- schema )  {
			println("DP2 TABLE "+ table)
			for ( f <- flds ) {
				println("DP2 TABLE "+table+" FIELD "+f)	
				primaryKey = tableKey get table
			}
			


		}
			
}

def printSchemas(basedir: String, auth: Boolean, authTable: String, authUserField: String, authPWField: String ) = {
		println("PRINT SCHEMAS")
		val statics = new Statics(basedir,filterSchema,auth, authTable)
		statics.print
		if ( auth ) {
			val authentication = new Auth( basedir, filterSchema, authTable, authUserField, authPWField )
			authentication.print
			val login = new Login( basedir, filterSchema, authUserField, authPWField )
			login.print
		}
		for ( (table, flds) <- filterSchema )  {
			println("TABLE "+ table)
			for ( f <- flds ) {
				println("TABLE "+table+" FIELD "+f)	
				primaryKey = tableKey get table
			}
			if ( table.toLowerCase != authTable.toLowerCase ) {
				if (auth) {
					val controller = new ControllerAuth(table, flds, primaryKey, basedir)
					controller.print
				} else {
					val controller = new Controller(table, flds, primaryKey, basedir)
					controller.print
				}
				val routes = new Routes(table, flds, primaryKey, basedir)
				routes.print
				val html = new HTML(table, flds, primaryKey, basedir)
					html.print
				val test = new Test(table, flds, primaryKey, basedir)
					test.print
				val coffee = new Coffee(table, flds, primaryKey, basedir)
					coffee.print
			}
			
			val model = new Model(table, flds, primaryKey, basedir, authTable, authUserField, authPWField)
			model.print 

			val schema = new Schema(table, flds, primaryKey, basedir, joinKeys.toList)
			schema.print


		} 
			
}

def printJoins(basedir: String) = {

	var table1Exists: Boolean = false
	var table2Exists: Boolean = false
	var table1 = ""
	var table2 = ""
	for ( joinKey <- joinKeys ) {
		for ( ( tables, keys ) <- joinKey )  {
			table1 = tables.head
			table2 = tables.tail.head
			println("printJoins table1 "+table1)
			println("printJoins table2 "+table2)
			val table1_key = keys.head
			val table2_key = keys.tail.head
			for ( (table, flds) <- filterSchema ) {
				println("printJOins filter "+table)
				if ( table1 == table ) {
					table1Exists = true
				}
				if ( table2 == table ) {
					table2Exists = true
				}
			}	
		}
		if ( table1Exists && table2Exists ) {
			println("Calling joins "+table1+" "+table2)
			val joins = new Joins(joinKey, basedir, schema)
			joins.print()
		}
		table1Exists = false
		table2Exists = false
		
	}	

}

def foundCreateTable(line: String) = {
	val objectStart = line.indexOf(createTableTag)+createTableTag.length()+spaceLength
	val objectEnd = line.indexOf(space,objectStart)
	val objectName = line.substring(objectStart,objectEnd)
	fields = new scala.collection.mutable.ListBuffer[String]

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


	def getFieldName(line: String):String = {
		var start = 0
		var end = line.indexOf(' ', start) 
		val fieldName = line.substring(start,end).trim
		fieldName
	}

}	



