package ezbuilder {

import java.io._


class Test(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String) extends scalaMVCFile(objectName, fields, primaryKey, basedir)  {
def print() = {
		calculate()
		val mvcDir = "test/"
		var fileName = capitalizedObjectName+"test.scala"
		val writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		var comma = ","
		write(writer,"import org.scalatestplus.play._")
		write(writer,"import play.api.test._")
		write(writer,"import play.api.test.Helpers.{GET => GET_REQUEST, _}")
		write(writer,"import play.api.libs.json._")
		write(writer,"import play.api.libs.ws._")
		write(writer,"import scala.concurrent.{Await,Future}")
		write(writer,"class "+capitalizedObjectName+"Test extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory  {")
		write(writer,tab+"\"The OneServerPerTest trait\" must {")
		write(writer,tab+tab+"\"test server logic\" in {")
		write(writer,tab+tab+tab+"val wsClient = app.injector.instanceOf[WSClient]")
		write(writer,tab+tab+tab+"val myPublicAddress =  s\"localhost:$port\"")
		write(writer,tab+tab+tab+"val scalabuildURL = s\"http://$myPublicAddress\"")
		write(writer,tab+tab+tab+"val baseURL = s\"http://$myPublicAddress/item\"")
		write(writer,tab+tab+tab+"var URL = s\"http://$myPublicAddress/item/1\"")
		write(writer,"")
		write(writer,tab+tab+tab+"var response = await(wsClient.url(baseURL).get())")
		write(writer,tab+tab+tab+"println(response)")
		write(writer,tab+tab+tab+"response.status mustBe (OK)")
		write(writer,tab+tab+tab+"")
		write(writer,tab+tab+tab+"var data = Json.obj(")
		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val dataType = getType(f)
			var length = dataType.length
			var scalaType = getLookupType(dataType)
			if ( f == fields.last ) 
				comma = ")"
			println("FieldName "+fieldName)
			println("PK "+primaryKey.get)
			if ( fieldName != primaryKey.get ) 
				write(writer,tab+tab+tab+tab+"\""+fieldName+"\" ->  \""+TestString(scalaType, fieldName)+"\""+comma)
		}

		comma = ","
		write(writer,tab+tab+tab+"var post"+capitalizedObjectName+" = await(wsClient.url(baseURL).post(data))")
		
		write(writer,tab+tab+tab+"data = Json.obj(")

		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val dataType = getType(f)
			var length = dataType.length
			var scalaType = getLookupType(dataType)
			if ( f == fields.last ) 
				comma = ")"
			
			write(writer,tab+tab+tab+tab+"\""+fieldName+"\" ->  \""+TestString(scalaType, fieldName)+"\""+comma)

		}

		write(writer,tab+tab+tab+"URL = s\"http://$myPublicAddress/"+objectName+"/update\"")
		write(writer,tab+tab+tab+"post"+capitalizedObjectName+" = await(wsClient.url(baseURL).post(data))")
		write(writer,tab+tab+tab+"println(\"Update \"+post"+capitalizedObjectName+")")
		write(writer,tab+tab+tab+"URL = s\"http://$myPublicAddress/"+objectName+"/1\"")
		write(writer,tab+tab+tab+"var request: WSRequest = wsClient.url(URL)")
		write(writer,tab+tab+tab+"var Response: WSResponse = await(request.get())")
		write(writer,tab+tab+tab+"println(Response.body)")

		write(writer,tab+tab+tab+"request = wsClient.url(baseURL)")
		write(writer,tab+tab+tab+"Response = await(request.get())")
		write(writer,tab+tab+tab+"println(Response.body)")
		write(writer,tab+tab+"}")
		write(writer,tab+"}")
		write(writer,"}")
		writer.close
	}

	def TestString(scalaType: String, fieldName: String):String = {
			var testString = ""
			if ( scalaType == "String" )
				testString = fieldName + objectName 
			else if ( scalaType == "Double" )
				testString = "7.7"
			else if ( scalaType == "Int" )
				testString = "7"
			else if ( scalaType == "java.sql.Timestamp" )
				testString = "1977-07-07 07:07:07"
			else if ( scalaType == "java.sql.Date" )
				testString = "1977-07-07" 
		
			testString
	}
}

}

