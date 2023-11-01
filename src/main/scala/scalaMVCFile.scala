package ezbuilder {




abstract class scalaMVCFile(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, primaryKeyIncrement: Option[Boolean]) extends scalaMVC {
	
	val tab = "\t"
	var fieldString = ""
	var createModelFieldString = ""
	var createFieldString = ""
	var controllerFieldString = ""
	var routesFieldString = ""
	var delimitedFields = ""
	var controllerDelimitedFields = ""
	var xFieldString=""
	var updateFieldString=""
	var updateFormString = ""
	var primaryKeyDataType = ""
	var modelFields = ""
	var updateControllerFieldString = ""
	var routesString = ""
	val capitalizedObjectName=objectName.toLowerCase.capitalize
	val createCapitalizedObjectName="create"+capitalizedObjectName
	val uppercaseCreateCapitalizedObjectName="Create"+capitalizedObjectName
	val updateCapitalizedObjectName="update"+capitalizedObjectName
	val lowerCaseObjectName=objectName.toLowerCase
	val upperCaseObjectName=objectName.toUpperCase 
	val pluralUpperCaseObjectName=objectName.toUpperCase 
	var comma = ", "
	var slash = "/"
	val PKIncrement=primaryKeyIncrement.get



	def print()

	def calculate() = {
	println("scalaMVCFile FIELDS")
	for ( f <- fields ) {
			println("FIELDSTRING "+f)
			val fieldName = getFieldName(f.replaceAll("`","")).toLowerCase
			val dataType = getType(f.replaceAll("`",""))
			val fn = Some(fieldName)
//			println("FIELD |"+fieldName+"|")
//	  		println("TYPE "+dataType)
			var length = dataType.length
			var scalaType = getLookupType(dataType)
			var scalaControllerType = getLookupControllerType(dataType)
			if ( f == fields.last ) {
				comma = ""
				slash = ""
			}

			updateControllerFieldString = updateControllerFieldString  + fieldName+": "+scalaControllerType+comma
			createModelFieldString = createModelFieldString + fieldName+": "+scalaType+comma
			modelFields = modelFields + fieldName+comma
//			println("SCALATYPE "+scalaType)
			
			if ( fn.get == primaryKey.get.toLowerCase ) {
				primaryKeyDataType = scalaType
				updateFieldString = updateFieldString+"update"+capitalizedObjectName+"."+fieldName+comma
				updateFormString = updateFormString+"update"+capitalizedObjectName+"."+fieldName+comma
				println("PK! "+primaryKey.get)
				if (PKIncrement)
					createFieldString = "0, "
				else
					createFieldString = createFieldString+"create"+capitalizedObjectName+"."+fieldName+comma
				if (!PKIncrement)
					controllerFieldString = controllerFieldString + fieldName+": "+scalaControllerType+comma
			} else {
				// include primary key for autoincriment
				controllerFieldString = controllerFieldString + fieldName+": "+scalaControllerType+comma
				fieldString = fieldString + fieldName+": "+scalaType+comma
				
				delimitedFields = delimitedFields + fieldName+comma
				
				if ( scalaType == "java.sql.Timestamp") {
					createFieldString = createFieldString+"new java.sql.Timestamp(timeStampFormat.parse(create"+capitalizedObjectName+"."+fieldName+").getTime)"+comma
					updateFieldString = updateFieldString + "new java.sql.Timestamp(timeStampFormat.parse(update"+capitalizedObjectName+"."+fieldName+").getTime)"+comma
					updateFormString = updateFormString+"update"+capitalizedObjectName+"."+fieldName+".toString"+comma
					controllerDelimitedFields = controllerDelimitedFields + "new java.sql.Timestamp(timeStampFormat.parse("+fieldName+").getTime)"+comma
					routesFieldString = routesFieldString + fieldName+": String"+comma
				}
				else if ( scalaType == "java.sql.Date") {
					createFieldString = createFieldString+"new java.sql.Date(dateFormat.parse(create"+capitalizedObjectName+"."+fieldName+").getTime)"+comma
					updateFieldString = updateFieldString+"new java.sql.Date(dateFormat.parse(update"+capitalizedObjectName+"."+fieldName+").getTime)"+comma
					updateFormString = updateFormString+"update"+capitalizedObjectName+"."+fieldName+".toString"+comma
					controllerDelimitedFields = controllerDelimitedFields + "new java.sql.Date(dateFormat.parse("+fieldName+").getTime)"+comma
					routesFieldString = routesFieldString+fieldName+": String"+comma
				} else {
					createFieldString = createFieldString+"create"+capitalizedObjectName+"."+fieldName+comma
					updateFieldString = updateFieldString+"update"+capitalizedObjectName+"."+fieldName+comma
					updateFormString = updateFormString+"update"+capitalizedObjectName+"."+fieldName+comma
					controllerDelimitedFields = controllerDelimitedFields +fieldName+comma
					routesFieldString = routesFieldString + fieldName+": "+scalaType+comma
				}
				xFieldString = xFieldString+"x."+fieldName+comma
				
				routesString = routesString+":"+fieldName+slash
			}
				
	}
}






}
}
