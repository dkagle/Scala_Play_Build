package ezbuilder {

import java.io._

abstract class scalaMVC()  {
	
	def write(writer: PrintWriter, line: String) =
		writer.write(line+"\n")

	def lookupType(dataType: String):String = dataType.toLowerCase match {
		case "var" => "String"
		case "tex" => "String"
		case "cha" => "String"
		case "nch" => "String"
		case "blo" => "String"
		case "clo" => "String"
		case "ncl" => "String"
		case "tim" => "java.sql.Timestamp"
		case "dat" => "java.sql.Date"
		case "dou" => "Double"
		case "rea" => "Double"
		case "med" => "Int"
		case "int" => "Int"
		case "sma" => "Int"
		case "ser" => "Int"
		case "big" => "Long"
		case "lon" => "Long"
		case "bit" => "Byte"
		case "tin" => "Byte"
		case "boo" => "Byte"
		case "dec" => "Float"
		case "bin" => "Float"
		case _ => dataType
	}

	
	def lookupControllerType(dataType: String):String = dataType.toLowerCase match {
		case "var" => "String"
		case "tex" => "String"
		case "cha" => "String"
		case "nch" => "String"
		case "blo" => "String"
		case "clo" => "String"
		case "ncl" => "String"
		case "tim" => "String"
		case "dat" => "String"
		case "dou" => "Double"
		case "rea" => "Double"
		case "med" => "Int"
		case "int" => "Int"
		case "sma" => "Int"
		case "ser" => "Int"
		case "big" => "Long"
		case "lon" => "Long"
		case "bit" => "Byte"
		case "tin" => "Byte"
		case "boo" => "Byte"
		case "dec" => "Float"
		case "bin" => "Float"
		case _ => dataType
	}
	
	def lookupMinKeyWord(dataType: String):String = dataType.toLowerCase match {
		case "var" => "minLength"
		case "tex" => "minLength"
		case "cha" => "minLength"
		case "nch" => "minLength"
		case "blo" => "minLength"
		case "clo" => "minLength"
		case "ncl" => "minLength"
		case "tim" => "minLength"
		case "dat" => "minLength"
		case "dou" => "min"
		case "rea" => "min"
		case "med" => "min"
		case "int" => "min"
		case "sma" => "min"
		case "ser" => "min"
		case "big" => "min"
		case "lon" => "lon"
		case "bit" => "min"
		case "tin" => "min"
		case "boo" => "min"
		case "dec" => "min"
		case "bin" => "min"		
		case _ => dataType
	}

	def lookupMinKeyValue(dataType: String):String = dataType.toLowerCase match {
		case "var" => "1"
		case "tex" => "1"
		case "cha" => "1"
		case "nch" => "1"
		case "blo" => "1"
		case "clo" => "1"
		case "ncl" => "1"
		case "tim" => "19"
		case "dat" => "10"
		case "dou" => "0"
		case "rea" => "0"
		case "med" => "0"
		case "int" => "0"
		case "sma" => "0"
		case "ser" => "0"
		case "big" => "0"
		case "lon" => "0"
		case "bit" => "0"
		case "tin" => "0"
		case "boo" => "0"
		case "dec" => "0"
		case "bin" => "0"
		case _ => dataType
	}


	def getFieldName(line: String):String = {
		var start = 0
		var end = line.indexOf(' ', start) 
		val fieldName = line.substring(start,end).trim
		fieldName
	}

	def getType(line: String):String = {
		var start = 1
		var end = line.indexOf(' ', start) 
		val typeStart = line.indexOf(' ',end)+1
		var typeEnd =  line.indexOf(' ', typeStart)
		if (typeEnd < 0 ) {
			typeEnd = line.length
		}
		val fieldType = line.substring(typeStart,typeEnd)
		fieldType
	}

}


}
