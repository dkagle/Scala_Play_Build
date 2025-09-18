package ezbuilder {




abstract class scalaMVCFile(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String) extends scalaMVC {
	
	val tab = "\t"
	var jsonAdd="\"{"
	var updateURL=""
	var tagString="";
	var idString="";
	val capitalizedObjectName=objectName.toLowerCase.capitalize
	val lowerCaseObjectName=objectName.toLowerCase
	val upperCaseObjectName=objectName.toUpperCase 
	val pluralUpperCaseObjectName=objectName.toUpperCase 
	var comma = ", "
	var slash = "/"
	var plusquote = "+\""
	var quote = ""



	def print()

	def calculate() = {
	for ( f <- fields ) {
//			println("FIELDSTRING "+f)
			val fieldName = getFieldName(f.replaceAll("`",""))
			val dataType = getType(f.replaceAll("`",""))
			val fn = Some(fieldName)
			val scalaType = lookupType(dataType.substring(0,3))

			if ( f == fields.last ) {
				comma = ""
				slash = ""
				plusquote = ""
				quote = "\""

			}
			updateURL=updateURL+"/\"+"+fn.get+plusquote
			tagString=tagString+"TAG_"+fn.get.toUpperCase+comma
			idString=idString+"R.id."+fn.get+comma
			

//			println("SCALATYPE "+scalaType)
			if ( fn.get == primaryKey.get ) {
				
				
			} else {
				if ( scalaType == "Double") 
					jsonAdd=jsonAdd+"\\\""+fn.get+"\\\":\"+"+fn.get+comma
				else 
					jsonAdd=jsonAdd+"\\\""+fn.get+"\\\":\\\"\"+"+fn.get+"+\"\\\""+quote+comma
			}
				
	}
	jsonAdd=jsonAdd+"+\"}\"";
	
}






}
}
