package ezbuilder {

import java.io._


class StaticAndroidManifest( basedir: String) extends scalaMVC()   {

	def print() = {
		val tab = "\t"
		val fileName = "AndroidManifest.xml"
		val qualifiedFileName=basedir+fileName
		var writer = new PrintWriter(new File(qualifiedFileName))
		write(writer,"<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		write(writer,"<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"")
		write(writer,tab+"package=\"com.scalaitoinfinity.androidbuild\">")
		write(writer,tab+"<uses-permission android:name=\"android.permission.INTERNET\" />")
		write(writer,tab+"<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\" />")
		write(writer,"")
		write(writer,tab+"<application")
		write(writer,tab+tab+"android:allowBackup=\"true\"")
		write(writer,tab+tab+"android:icon=\"@mipmap/ic_launcher\"")
		write(writer,tab+tab+"android:label=\"@string/app_name\">")
		write(writer,tab+tab+"<activity")
		write(writer,tab+tab+tab+"android:name=\"com.scalaitoinfinity.androidbuild.MainActivity\"")
		write(writer,tab+tab+tab+"android:label=\"@string/app_name\"")
		write(writer,tab+tab+tab+"android:launchMode=\"singleTop\"")
		write(writer,tab+tab+tab+"android:uiOptions=\"splitActionBarWhenNarrow\">")
		write(writer,tab+tab+tab+"<intent-filter>")
		write(writer,tab+tab+tab+tab+"<action android:name=\"android.intent.action.MAIN\" />")
		write(writer,tab+tab+tab+tab+"<category android:name=\"android.intent.category.LAUNCHER\" />")
		write(writer,tab+tab+tab+"</intent-filter>")
		write(writer,tab+tab+"</activity>")
		write(writer,tab+" </application>")
		write(writer,"</manifest>")
		writer.close

		
	}
}	
}
