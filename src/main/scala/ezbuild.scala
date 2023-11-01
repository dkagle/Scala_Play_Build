
package ezbuilder
import ezbuilder._


class Ezbuild( ) {
var schema = new scala.collection.mutable.HashMap[String, List[String]]
var filterSchema = new scala.collection.mutable.HashMap[String, List[String]]
var schemas = new scala.collection.mutable.ListBuffer[Map[String, List[String]]]
var tableKey = new scala.collection.mutable.HashMap[String, String]
var tableKeys = new scala.collection.mutable.ListBuffer[Map[String, String]]
var tableIncriment = new scala.collection.mutable.HashMap[String, Boolean]
var joinKeys = new scala.collection.mutable.ListBuffer[Map[List[String], List[String]]]
var fields = new scala.collection.mutable.ListBuffer[String]


var flds = new scala.collection.mutable.ListBuffer[String]
var table = ""

var PKIncrement = false
var primaryKeyIncrement = Option(false)
val evolutionDir = "conf/evolutions/default"
val createTableTag = "CREATE TABLE"
val primaryKeyTag = "PRIMARY KEY"
val autoIncrementTag = "AUTO_INCREMENT"
val serialTag = "SERIAL"
val identityTag = "IDENTITY"
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

	filterFields.foreach((filterField) => 
		for ( (table,fields) <- filterSchema )  {
			if ( table == filterTable ) {
				fields.foreach((f) =>
					if ( getFieldName(f) == filterField ) 
						tempFields += f
				)
			}
		}
	)
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
	selectTables.foreach((selectTable) => 

		for ( (table, flds) <- schema )  {
			if ( selectTable == table )
				filterSchema += ( table -> flds )
		}
	)				 
}


def printFields(fields: List[String], tableName: String) = fields.foreach((field) =>
				println("TABLENAME "+ tableName +" FIELD "+field) )

def duplicateSchema() = {

	for ( (table, flds) <- schema )  {
		filterSchema += ( table -> flds )
	}
}

def dummyPrintSchemas( ) = {
		println("DP PRINT SCHEMAS")
		for ( (table, flds) <- filterSchema )  {
			println("DP TABLE "+ table)
			flds.foreach((f) => primaryKey = tableKey get table )
		}
			
}

def dummyPrintSchemas2( ) = {
		println("DP2 PRINT SCHEMAS")
		for ( (table, flds) <- schema )  {
			println("DP2 TABLE "+ table)
			flds.foreach((f) => primaryKey = tableKey get table )
		}
			
}

def printSchemas(basedir: String, auth: Boolean, authTable: String, authUserField: String, authPWField: String ) = {
		println("PRINT SCHEMAS")
		val statics = new Statics(basedir,filterSchema,auth, authTable, joinKeys)
		statics.print
		if ( auth ) {
			val authentication = new Auth( basedir, filterSchema, authTable, authUserField, authPWField )
			authentication.print
			val login = new Login( basedir, filterSchema, authUserField, authPWField )
			login.print
		}
		for ( (table, flds) <- filterSchema )  {
			println("TABLE "+ table)
			flds.foreach((f) => primaryKey = tableKey get table )
			primaryKeyIncrement = tableIncriment get table
			println("TI "+primaryKeyIncrement)
			
			if ( table.toLowerCase != authTable.toLowerCase ) {
				val controller = new Controller(table, flds, primaryKey, basedir, auth, primaryKeyIncrement)
				controller.print
				val routes = new Routes(table, flds, primaryKey, basedir, primaryKeyIncrement)
				routes.print
				val html = new HTML(table, flds, primaryKey, basedir, primaryKeyIncrement)
					html.print
				val test = new Test(table, flds, primaryKey, basedir, primaryKeyIncrement)
					test.print
				val coffee = new Coffee(table, flds, primaryKey, basedir, primaryKeyIncrement)
					coffee.print
			}
			
			val model = new Model(table, flds, primaryKey, basedir, authTable, authUserField, authPWField, primaryKeyIncrement)
			model.print 

			val schema = new Schema(table, flds, primaryKey, basedir, joinKeys.toList, primaryKeyIncrement)
			schema.print
		} 
}

def printJoins(basedir: String, auth: Boolean) = {
	var table1Exists: Boolean = false
	var table2Exists: Boolean = false
	var table1 = ""
	var table2 = ""
	var table1_fields: List[String] = List()
	var table2_fields: List[String] = List()
	var table2PrimaryKey = Option("")

	var objectName = ""

	joinKeys.foreach((joinKey) => {
		for ( ( tables, keys ) <- joinKey )  {
			table1 = tables.head
			table2 = tables.tail.head
			println("printJoins table1 "+table1)
			println("printJoins table2 "+table2)
			val table1_key = keys.head
			val table2_key = keys.tail.head
			for ( (table, flds) <- filterSchema ) {
				println("printJoins filter "+table)
				
				
				if ( table1.toLowerCase == table.toLowerCase ) {
					table1Exists = true
					table1_fields = flds		
					primaryKeyIncrement = tableIncriment get table			
				}
				if ( table2.toLowerCase == table.toLowerCase ) {
					table2Exists = true
					table2_fields = flds
					flds.foreach((f) => table2PrimaryKey = tableKey get table )
				}
			}	
		}
		for ( ( tables, keys ) <- joinKey )  {

			primaryKey = Option(keys.head.toLowerCase)
			objectName = tables.head+tables.tail.head
			

		}
		if ( table1Exists && table2Exists ) {
			val joinController = new JoinController(joinKey, basedir, schema, auth, table1_fields, table2_fields, table2PrimaryKey.get)
			joinController.print()
			val joinModel = new JoinModel(joinKey, basedir, schema)
			joinModel.print()
			val joinCoffee = new JoinCoffee(joinKey, basedir, schema)
			joinCoffee.print()
			val joinHTML = new JoinHTML(joinKey, basedir, schema, tableIncriment)
			joinHTML.print()  
			val joins = new Joins(joinKey, basedir, schema, table1_fields, table2_fields)
			joins.print()

		}
		table1Exists = false
		table2Exists = false
	})	
}

def foundCreateTable(line: String) = {
	val objectStart = line.toUpperCase.indexOf(createTableTag)+createTableTag.length()+spaceLength
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
	if ( findAutoIncriment(line) ) {
		println("FOUND AUTO INCRIMENT")
		tableIncriment += ( tableName -> true )
	} else {
		tableIncriment += ( tableName -> false )
	}
}

def findAutoIncriment(line: String): Boolean = {
	var found = false;

	if (line.toUpperCase.indexOf(autoIncrementTag) > 0) 
		found = true;
	if (line.toUpperCase.indexOf(serialTag) > 0) 
		found = true;
	if (line.toUpperCase.indexOf(identityTag) > 0) 
		found = true;
	
	found
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
	start = line.toUpperCase.indexOf("REFERENCES")+11
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
		if (line.toUpperCase.trim.startsWith(createTableTag)) {
			foundCreateTable(line)
			parseTable = true
		} else if (line.trim.startsWith(")")) {
			foundEndTable()
			parseTable=false
		} else if (line.toUpperCase.trim.startsWith("PRIMARY KEY")) {
			foundPrimaryKey(line)

		} else if (line.toUpperCase.trim.startsWith("FOREIGN KEY")) {
			foundForiegnKey(line)
		} else {
			if ( parseTable ) {
				foundFieldName(line)
				if (line.toUpperCase.indexOf(primaryKeyTag) > 0 ) {
					foundPrimaryKey(line)

				}
			}
		}		
}


	def getFieldName(line: String):String = {
		var start = 0
		var end = line.indexOf(' ', start) 
		val fieldName = line.substring(start,end).trim
		fieldName
	}

}	



