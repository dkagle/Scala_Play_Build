package ezbuilder {

import java.io._


class Layout(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val mvcDir = "layout/"
		var previousField = ""
		var fileName=objectName+"listheader.xml"
		var writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<LinearLayout android:id=\"@+id/listheader\"")
		write(writer,tab+"xmlns:android=\"http://schemas.android.com/apk/res/android\"")
		write(writer,tab+"android:orientation=\"vertical\"")
		write(writer,tab+"android:layout_height=\"fill_parent\"")
		write(writer,tab+"android:layout_width=\"fill_parent\">")
		write(writer,"")
		write(writer,tab+"<LinearLayout android:id=\"@+id/header\"")
		write(writer,tab+tab+"android:background=\"@color/colorPrimaryDark\"")
		write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
		write(writer,tab+tab+"android:layout_width=\"fill_parent\"")
		write(writer,tab+">")

		for ( f <- fields ) {
			val fieldName = getFieldName(f)

			val fn = Some(fieldName)
			
			write(writer,tab+tab+"<TextView android:id=\"@+id/"+fn.get+"\"")
			write(writer,tab+tab+tab+"android:layout_width=\"wrap_content\"")
			write(writer,tab+tab+tab+"android:layout_height=\"fill_parent\"")
			write(writer,tab+tab+tab+"android:text=\""+fn.get.capitalize+"\"")
			write(writer,tab+tab+tab+"android:width=\"100dip\"")
			write(writer,tab+tab+tab+"android:height=\"30dip\"")
			write(writer,tab+tab+tab+"/>")
		}
		write(writer,tab+"</LinearLayout>")
		write(writer,tab+"<View android:layout_width=\"fill_parent\"")
	    write(writer,tab+tab+"android:layout_height=\"1dip\"")
	    write(writer,tab+tab+"android:background=\"?android:attr/listDivider\" />")

	    write(writer,tab+"<LinearLayout android:id=\"@+id/layout\"")
        write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
	    write(writer,tab+tab+"android:layout_height=\"fill_parent\">")
	    write(writer,tab+tab+"<ListView android:id=\"@+id/"+objectName+"list\"")
	    write(writer,tab+tab+tab+"android:background=\"@color/colorPrimaryLight\"")
	    write(writer,tab+tab+tab+"android:layout_height=\"fill_parent\"")
        write(writer,tab+tab+tab+"android:layout_width=\"fill_parent\">") 
	    write(writer,tab+tab+"</ListView>")
    	write(writer,tab+"</LinearLayout>")
		write(writer,"</LinearLayout>") 
		writer.close()

		fileName=objectName+"deleteform.xml"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"")
	    write(writer,tab+"android:orientation=\"vertical\" android:layout_width=\"match_parent\" android:background=\"@color/colorPrimaryLight\"")
	    write(writer,tab+"android:layout_height=\"wrap_content\">")
		write(writer,"")

		for ( f <- fields ) {
			val fieldName = getFieldName(f)

			val fn = Some(fieldName)
			write(writer,tab+"<TextView")
			write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
	        write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
        	write(writer,tab+tab+"android:textAppearance=\"?android:attr/textAppearanceLarge\"")
        	write(writer,tab+tab+"android:id=\"@+id/"+fn.get+"Tag"+"\"")
	        write(writer,tab+tab+"android:text=\"@string/"+objectName+fn.get+"\"")
	        write(writer,tab+tab+"android:layout_marginLeft=\"14dp\"")
        	write(writer,tab+tab+"android:layout_marginTop=\"18dp\"")
	        if ( f != fields.head ) 
		        write(writer,tab+tab+"android:layout_below=\"@+id/"+previousField+"Tag"+"\"")
        	write(writer,tab+"/>")
			previousField = fn.get
		}
  


		for ( f <- fields ) {
			val fieldName = getFieldName(f)

			val fn = Some(fieldName)
			write(writer,tab+"<TextView")
			write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
	        write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
        	write(writer,tab+tab+"android:textAppearance=\"?android:attr/textAppearanceLarge\"")
        	write(writer,tab+tab+"android:id=\"@+id/"+fn.get+"\"")
//	        write(writer,tab+tab+"android:text=\"@string/"+objectName+fn.get+"\"")
	        write(writer,tab+tab+"android:layout_marginLeft=\"14dp\"")
        	write(writer,tab+tab+"android:layout_marginTop=\"18dp\"")
			write(writer,tab+tab+"android:layout_alignParentRight=\"true\"")
	        if ( f != fields.head ) 
		        write(writer,tab+tab+"android:layout_below=\"@+id/"+previousField+"\"")
			write(writer,tab+"/>")
			previousField = fn.get
		}



    	write(writer,tab+"<Button")
        write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
        write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
        write(writer,tab+tab+"android:text=\"delete\"")
        write(writer,tab+tab+"android:id=\"@+id/Send\"")
        write(writer,tab+tab+"android:onClick=\"sendMessage\"")
        write(writer,tab+tab+"android:background=\"#7a9ec7\"")
        write(writer,tab+tab+"android:layout_centerHorizontal=\"true\"")
        write(writer,tab+tab+"android:layout_gravity=\"right\"")
        write(writer,tab+tab+"android:layout_marginTop=\"30dp\"")
        write(writer,tab+tab+"android:layout_below=\"@+id/price\"")
        write(writer,tab+"/>")
	

		write(writer,"</RelativeLayout>")		
		writer.close()
		fileName=objectName+"form.xml"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"")
	    write(writer,tab+"android:orientation=\"vertical\" android:layout_width=\"match_parent\" android:background=\"@color/colorPrimaryLight\"")
	    write(writer,tab+"android:layout_height=\"wrap_content\">")
		write(writer,"")
		
		for ( f <- fields ) {
			val fieldName = getFieldName(f)

			val fn = Some(fieldName)
			
        	write(writer,tab+"<TextView")
			write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
	        write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
        	write(writer,tab+tab+"android:textAppearance=\"?android:attr/textAppearanceLarge\"")
        	write(writer,tab+tab+"android:id=\"@+id/"+fn.get+"Tag"+"\"")
	        write(writer,tab+tab+"android:text=\"@string/"+objectName+fn.get+"\"")
	        write(writer,tab+tab+"android:layout_marginLeft=\"14dp\"")
        	write(writer,tab+tab+"android:layout_marginTop=\"18dp\"")
			if ( f != fields.head ) 
		        write(writer,tab+tab+"android:layout_below=\"@+id/"+previousField+"Tag"+"\"")
        	write(writer,tab+"/>")
			previousField = fn.get
		}
  


		for ( f <- fields ) {
			val fieldName = getFieldName(f)

			val fn = Some(fieldName)
			if ( fn == primaryKey ) {
				write(writer,tab+"<TextView")
				write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
	        	write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
        		write(writer,tab+tab+"android:textAppearance=\"?android:attr/textAppearanceLarge\"")
        		write(writer,tab+tab+"android:id=\"@+id/"+fn.get+"\"")
//	        	write(writer,tab+tab+"android:text=\"@string/"+objectName+fn.get+"\"")
	        	write(writer,tab+tab+"android:layout_marginLeft=\"14dp\"")
				write(writer,tab+tab+"android:layout_alignParentRight=\"true\"")
        		write(writer,tab+tab+"android:layout_marginTop=\"18dp\"")
				write(writer,tab+"/>")
		        
			} else {
				write(writer,tab+"<EditText")
				write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
	        	write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
				write(writer,tab+tab+"android:id=\"@+id/"+fn.get+"\"")
				write(writer,tab+tab+"android:inputType=\"text\"")
				write(writer,tab+tab+"android:ems=\"10\"")
				write(writer,tab+tab+"android:layout_alignParentRight=\"true\"")
				write(writer,tab+tab+"android:layout_marginTop=\"18dp\"")
				write(writer,tab+tab+"android:layout_weight=\"1\"")
				write(writer,tab+tab+"android:layout_below=\"@+id/"+previousField+"\"")
				write(writer,tab+"/>")
			}	
			previousField = fn.get
		}



    	write(writer,tab+"<Button")
        write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
        write(writer,tab+tab+"android:layout_height=\"wrap_content\"")
        write(writer,tab+tab+"android:text=\"Update\"")
        write(writer,tab+tab+"android:id=\"@+id/Send\"")
        write(writer,tab+tab+"android:onClick=\"sendMessage\"")
        write(writer,tab+tab+"android:background=\"@color/colorPrimaryDark\"")
        write(writer,tab+tab+"android:layout_centerHorizontal=\"true\"")
        write(writer,tab+tab+"android:layout_gravity=\"right\"")
        write(writer,tab+tab+"android:layout_marginTop=\"30dp\"")
        write(writer,tab+tab+"android:layout_below=\"@+id/price\"")
        write(writer,tab+"/>")

		write(writer,"</RelativeLayout>")
		writer.close

		fileName=objectName+"list.xml"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"")
    	write(writer,tab+"android:orientation=\"horizontal\"")
    	write(writer,tab+"android:layout_width=\"fill_parent\"")
    	write(writer,tab+"android:layout_height=\"fill_parent\">")
		
		for ( f <- fields ) {
			val fieldName = getFieldName(f)

			val fn = Some(fieldName)
    		write(writer,tab+tab+"<TextView android:id=\"@+id/"+fn.get+"\"")
	        write(writer,tab+tab+"android:text=\"row_id\"")
        	write(writer,tab+tab+"android:layout_height=\"fill_parent\"")
        	write(writer,tab+tab+"android:layout_width=\"wrap_content\"")
        	write(writer,tab+tab+"android:width=\"100dip\"")
        	write(writer,tab+tab+"/>")
		}
		write(writer,"</LinearLayout>")
		writer.close

		fileName=objectName+"main.xml"
		writer = new PrintWriter(new File(basedir+mvcDir+fileName))
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<LinearLayout")
    	write(writer,tab+"xmlns:android=\"http://schemas.android.com/apk/res/android\"")
    	write(writer,tab+"android:orientation=\"vertical\"")
	    write(writer,tab+"android:layout_width=\"fill_parent\"")
    	write(writer,tab+"android:layout_height=\"fill_parent\">")
		write(writer,"")
    	write(writer,tab+"<View")
        write(writer,tab+tab+"android:layout_width=\"fill_parent\"")
        write(writer,tab+tab+"android:layout_height=\"1dp\"")
        write(writer,tab+tab+"android:background=\"@color/colorPrimaryLight\"/>")
		write(writer,"")
 

		write(writer,"</LinearLayout>")
		writer.close()

	}
}
}
