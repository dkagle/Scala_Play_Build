package ezbuilder
import ezbuilder._
import scala.swing._
import scala.io._
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.JFrame
import scala.swing.BorderPanel.Position._
import event._
//import java.util.NoSuchElementException

object scalabuild  extends scala.swing.SimpleSwingApplication  {
val addButton = new Button {
	text = "Add"
	borderPainted = true
	enabled = true
}

val doneButton = new Button {
	text = "Done"
	borderPainted = true
	enabled = true
}

val deleteButton = new Button {
	text = "Delete"
	borderPainted = true
	enabled = true
}


val editFieldsButton = new Button {
	text = "Edit Fields"
	borderPainted = true
	enabled = true
}

val addFieldButton = new Button {
	text = "Add"
	borderPainted = true
	enabled = true
}

val saveButton = new Button {
	text = "Save"
	borderPainted = true
	enabled = true
}


val saveFieldButton = new Button {
	text = "Save"
	borderPainted = true
	enabled = true
}

val deleteFieldButton = new Button {
	text = "Delete"
	borderPainted = true
	enabled = true
}

val authFieldButton = new Button {
	text = "Select Authtication Fields"
	borderPainted = true
	enabled = true
}

var frame = new JFrame 
var basedir = ""
def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines().toList
var tables = new scala.collection.mutable.ListBuffer[String]
var selectTables = new scala.collection.mutable.ListBuffer[String]
var selectFields = new scala.collection.mutable.ListBuffer[String]
var viewFields = new scala.collection.mutable.ListBuffer[String]
var authTables = new scala.collection.mutable.ListBuffer[String]
var authUserFields = new scala.collection.mutable.ListBuffer[String]
var authPassFields = new scala.collection.mutable.ListBuffer[String]
var fieldEditTable = ""

def top = new MainFrame {
		title = "ScalaBuild"
		val ezbuild = new Ezbuild()
		
		var chooser = new JFileChooser
		chooser.setDialogTitle("Scala Play! Directory")
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
		
		if (chooser.showDialog(null, "Choose") == JFileChooser.APPROVE_OPTION) {
			println("getSelectedFile " + chooser.getSelectedFile.toString)
			basedir = chooser.getSelectedFile.toString+"/"
	    	} else {
	      		println("No Selection ")
	    	}


		val fc = new FileChooser()
		fc.title="SQL File"
				
		val returnVal = fc.showOpenDialog(null)

		if (returnVal == FileChooser.Result.Approve) {
	    		def file = fc.selectedFile
			println("FILE "+file.getName)
			for ( line <- fileLines(file) ) {
				ezbuild.parseLine(line)
				println(line)
			}
		
			ezbuild.duplicateSchema
			for ( table <- ezbuild.getTables )  {
				println("PRINTGUI TABLE "+table)
				tables += table
				selectTables += table 
				authTables += table
			}


			val tableListView = new ListView(tables) 
			val selectTableListView = new ListView(selectTables) 
			val selectFieldListView = new ListView(selectFields) 
			val fieldListView = new ListView(viewFields) 
			val authtableListView = new ListView(authTables) 
			val authUserFieldsListView = new ListView(authUserFields)
			val authPassFieldsListView = new ListView(authPassFields)

			val tablePanel = new FlowPanel(new ScrollPane(tableListView) )
			val selectedTablePanel = new FlowPanel(new ScrollPane(selectTableListView) )
			val selectedFieldPanel = new FlowPanel(new ScrollPane(selectFieldListView) )		
			val fieldPanel = new FlowPanel(new ScrollPane(fieldListView) )
			val authtablePanel = new FlowPanel(new ScrollPane(authtableListView) )
			val authUserFieldsPanel = new FlowPanel(new ScrollPane(authUserFieldsListView) )
			val authPassFieldsPanel = new FlowPanel(new ScrollPane(authPassFieldsListView) )


			val allLabel = new Label("All Tables")
			val filterLabel = new Label("Selected Tables")
			val authLabel = new Label("Auth Table")
			val authUserFieldLabel = new Label("Auth Table User Field")
			val authPWFieldLabel = new Label("Auth Table Password Field")

			val labelPanel = new GridPanel(1,2) {
				contents += allLabel
				contents += filterLabel
			} 

			val tableBoxPanel = new BoxPanel(Orientation.Horizontal) {
				contents += tablePanel
				contents += selectedTablePanel
			}

			val buttonPanel = new BoxPanel(Orientation.Horizontal) {
				val mutex = new ButtonGroup(addButton,editFieldsButton,saveButton,deleteButton)
				contents ++= mutex.buttons
			}



			val allFieldLabel = new Label("All Fields")
			val filterFieldLabel = new Label("Selected Fields")
			val primaryKeyLabel = new Label()
			val authCheckBox = new CheckBox("Authentication")
			
			val fieldLabelPanel = new GridPanel(2,2) {
				contents += allFieldLabel
				contents += filterFieldLabel
				contents += primaryKeyLabel
				contents += primaryKeyLabel
			}


			val fieldBoxPanel = new BoxPanel(Orientation.Horizontal) {
				contents += fieldPanel
				contents += selectedFieldPanel
			}

			val fieldButtonPanel = new BoxPanel(Orientation.Horizontal) {
				val mutex = new ButtonGroup(addFieldButton,saveFieldButton,deleteFieldButton)
				contents ++= mutex.buttons
			}




			val labelTableBoxPanel = new BoxPanel(Orientation.Vertical) {
				contents += labelPanel
				contents += tableBoxPanel
				contents += buttonPanel
				contents += fieldLabelPanel
				contents += fieldBoxPanel
				contents += fieldButtonPanel
			}
			val authTableBoxPanel = new BoxPanel(Orientation.Vertical) {
				contents += authCheckBox
				contents += authLabel
				contents += authtablePanel
				contents += authFieldButton
			}

			val authFieldBoxPanel = new BoxPanel(Orientation.Vertical) {
				contents += authUserFieldLabel
				contents += authUserFieldsPanel
				contents += authPWFieldLabel
				contents += authPassFieldsPanel
			}


			contents = new BorderPanel {
				layout(labelTableBoxPanel) = North
				layout(authTableBoxPanel) = West
				layout(authFieldBoxPanel) = East
				layout(doneButton) = South

			}

			listenTo(addButton)
			listenTo(doneButton)
			listenTo(deleteButton)
			listenTo(editFieldsButton)
			listenTo(tableListView.selection)
			listenTo(selectTableListView.selection)
			listenTo(addFieldButton)
			listenTo(saveButton)
			listenTo(saveFieldButton)
			listenTo(deleteFieldButton)
			listenTo(fieldListView.selection)
			listenTo(selectFieldListView.selection)
			listenTo(authtableListView.selection)	
			listenTo(authFieldButton)
		
		reactions += {
			case ButtonClicked(component) if component == addButton =>
				println("ADDBUTTON")
				var items = tableListView.selection.items
				for ( item <- items ) {
					if ( !selectTables.contains(item) )
						selectTables += item
						println("SELECTED "+item)
					}

					selectTableListView.listData = selectTables
			case ButtonClicked(component) if component == doneButton =>
				println("DONEBUTTON ")
				var authTables = ""
				var authUserField = ""
				var authPassWordField = ""
				var exec = 0
				if ( authCheckBox.selected ) {
					try {
						authTables = authtableListView.selection.items.head
					} catch {
						case ex: Exception => {
						print("No Auth Table Selected")
						JOptionPane.showMessageDialog(frame,"No Auth Table Selected.","Inane error",JOptionPane.ERROR_MESSAGE);
						exec = 1
					  }
					}
					try {
						authUserField = authUserFieldsListView.selection.items.head
					} catch {
						case ex: Exception => {
						print("No User Selected")
						JOptionPane.showMessageDialog(frame,"No User Selected.","Inane error",JOptionPane.ERROR_MESSAGE);
						exec = 1
					  }
					}
					try {
						authPassWordField = authPassFieldsListView.selection.items.head
					} catch {
						case ex: Exception => {
						print("No Password Selected")
						JOptionPane.showMessageDialog(frame,"No Password Selected.","Inane error",JOptionPane.ERROR_MESSAGE);
						exec = 1
						}
					}
				}
				if ( exec == 0 ) {
					ezbuild.printSchemas(basedir, authCheckBox.selected, authTables, authUserField, authPassWordField)
					ezbuild.printJoins(basedir)
					sys.exit(0)
				}
			case ButtonClicked(component) if component == editFieldsButton =>
				println("EDITBUTTON")		
				viewFields.clear
				selectFields.clear
				var itemBuffer = new scala.collection.mutable.ListBuffer[String]
				val items = selectTableListView.selection.items
				println(items)
				if ( items.isEmpty ) {
					println("No Table Selected")
				} else {
					var fields = ezbuild.getFields(items(0))
					for ( field <- fields ) {
						if ( field != ezbuild.primaryKey.get) {
							viewFields += field
						}
					}
					fields = ezbuild.getFilterFields(items(0))
					for ( field <- fields ) {
						if ( field != ezbuild.primaryKey.get) {
							selectFields += field
							println("SELECTFIELDS "+field)
						}
					}
					fieldListView.listData = viewFields
					selectFieldListView.listData = selectFields
					fieldEditTable=items(0)
					primaryKeyLabel.text = "PK = "+ezbuild.primaryKey.get
				}

			case ButtonClicked(component) if component == authFieldButton =>
				println("AUTH FIELD BUTTON")	
				authUserFields.clear
				authPassFields.clear
				var itemBuffer = new scala.collection.mutable.ListBuffer[String]
				val items = authtableListView.selection.items
				println(items)
				if ( items.isEmpty ) {
					println("No Table Selected")
				} else {
					var fields = ezbuild.getFields(items(0))
					for ( field <- fields ) {
						if ( field != ezbuild.primaryKey.get) {
							authUserFields += field
						}
					}
					fields = ezbuild.getFilterFields(items(0))
					for ( field <- fields ) {
						if ( field != ezbuild.primaryKey.get) {
							authPassFields += field
							println("SELECTFIELDS "+field)
						}
					}
					authUserFieldsListView.listData = authUserFields
					authPassFieldsListView.listData = authPassFields
				//	fieldEditTable=items(0)
				//	primaryKeyLabel.text = "PK = "+ezbuild.primaryKey.get
				}	
			case ButtonClicked(component) if component == saveButton =>
				println("SAVE")			
				ezbuild.updateSchema(selectTables.toList)

			case ButtonClicked(component) if component == deleteButton =>
				println("DELETEBUTTON")
				var items = selectTableListView.selection.items
				for ( item <- items ) {
					selectTables -= item
					println("SELECTED "+item)
				}
				selectTableListView.listData = selectTables

			case ButtonClicked(component) if component == addFieldButton =>
				println("ADDFIELDBUTTON")
				var items = fieldListView.selection.items
				for ( item <- items ) {
					selectFields += item
					println("SELECTED "+item)
				}
				selectFieldListView.listData = selectFields	

			case ButtonClicked(component) if component == deleteFieldButton =>
				println("DELETEFieldBUTTON")
				var items = selectFieldListView.selection.items
				for ( item <- items ) {
					selectFields -= item
					println("SELECTED "+item)
				}
				selectFieldListView.listData = selectFields
				for ( selectField <- selectFields ) 
					println("SELECT FLDS "+selectField)

			case ButtonClicked(component) if component == saveFieldButton =>
				println("SAVE FIELDS")
				
				val selectFields = new scala.collection.mutable.ListBuffer[String]
				val seqSelectFields = selectFieldListView.listData
				for ( seqSelectFields <- seqSelectFields ) 
					selectFields += seqSelectFields
				ezbuild.rebuildSchema(fieldEditTable, selectFields.toList )
				selectFields.clear
				viewFields.clear
				selectFieldListView.listData = selectFields
				fieldListView.listData = viewFields	
			}	
		}
	}
}
