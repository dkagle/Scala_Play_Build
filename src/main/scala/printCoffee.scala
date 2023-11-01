package ezbuilder {

import java.io._


class Coffee(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, primaryKeyIncrement: Option[Boolean]  ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, primaryKeyIncrement)   {


	def print() = {
		val mvcDir = "app/assets/javascripts/"
		var fileName=lowerCaseObjectName+".coffee"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))

		val tab = "  "

		write(writer, "require(['"+lowerCaseObjectName+"logic'], (Logic) ->")
		write(writer, tab+"for deleteBtn in document.querySelectorAll('button.delete-"+lowerCaseObjectName+"')")
		write(writer, tab+tab+"do (deleteBtn) ->  Logic(deleteBtn, deleteBtn.dataset."+primaryKey.get.toLowerCase+")")
		write(writer, ")")
		writer.close()

		fileName=lowerCaseObjectName+"logic.coffee"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer, "define(['"+lowerCaseObjectName+"ui', 'routes'], (Ui, routes) ->")
		write(writer, tab+"(node, "+primaryKey.get.toLowerCase+") ->")
		write(writer, tab+tab+"ui = Ui(node)")
		write(writer, tab+tab+"ui.forEachClick(() ->")
		write(writer, tab+tab+tab+"xhr = new XMLHttpRequest()")
		write(writer, tab+tab+tab+"route = routes.controllers."+capitalizedObjectName+"Controller.delete("+primaryKey.get.toLowerCase+")")
		write(writer, tab+tab+tab+"xhr.open(route.method, route.url)")
		write(writer, tab+tab+tab+"xhr.addEventListener('readystatechange', () ->")
		write(writer, tab+tab+tab+tab+"if xhr.readyState == XMLHttpRequest.DONE")
		write(writer, tab+tab+tab+tab+tab+"if xhr.status == 200")
		write(writer, tab+tab+tab+tab+tab+tab+"ui.delete()")
		write(writer, tab+tab+tab+tab+tab+"else")
		write(writer, tab+tab+tab+tab+tab+tab+"alert('Unable to delete the "+lowerCaseObjectName+"!')")
		write(writer, tab+tab+tab+")")
		write(writer, tab+tab+tab+"xhr.send()")
		write(writer, tab+tab+")")
		write(writer, ")")
		writer.close

		fileName=lowerCaseObjectName+"ui.coffee"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer, "define(() ->")
		write(writer, tab+"(node) ->")
		write(writer, tab+tab+"delete: () ->")
		write(writer, tab+tab+tab+"li = node.parentNode")
		write(writer, tab+tab+tab+"li.parentNode.removeChild(li)")
		write(writer, tab+tab+"forEachClick: (callback) ->")
		write(writer, tab+tab+tab+"node.addEventListener('click', callback)")
		write(writer, ")")
		writer.close
		
	}
}


}

