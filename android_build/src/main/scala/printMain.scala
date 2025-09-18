package ezbuilder {

import java.io._


class MainActivity(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val fileName = objectName.capitalize+"MainActivity.java"
		val writer = new PrintWriter(new File(basedir+fileName))
		calculate()
		write(writer,"package com.scalaitoinfinity.androidbuild;")
		write(writer,"import android.app.Activity;")

		write(writer,"import android.content.Intent;")

		write(writer,"import android.os.Bundle;")

		write(writer,"import android.util.Log;")
		write(writer,"import android.view.Menu;")
		write(writer,"import android.view.MenuItem;")


		write(writer,"")
		write(writer,"import java.net.URL;")

		write(writer,"public class "+capitalizedObjectName+"MainActivity extends Activity {")
		write(writer,tab+"public static final String TAG = \"AndroidBuild\";")
		write(writer,tab+"@Override")
		write(writer,tab+"protected void onCreate(Bundle savedInstanceState) {")
		write(writer,tab+"super.onCreate(savedInstanceState);")
		write(writer,tab+tab+"setContentView(R.layout."+objectName+"main);")
		write(writer,tab+"}")
		write(writer,tab+"@Override")
		write(writer,tab+"public boolean onCreateOptionsMenu(Menu menu) {")
		write(writer,tab+tab+"getMenuInflater().inflate(R.menu."+objectName+"main, menu);")
		write(writer,tab+tab+"return true;")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"@Override")
		write(writer,tab+"public boolean onOptionsItemSelected(MenuItem item) {")
		write(writer,tab+tab+"switch (item.getItemId()) {")
		write(writer,tab+tab+tab+"case R.id.update_action:")
		write(writer,tab+tab+tab+tab+"Intent listIntent = new Intent(this, "+capitalizedObjectName+"ListActivity.class);")

		write(writer,tab+tab+tab+tab+"startActivity(listIntent);")
		write(writer,tab+tab+tab+tab+"return true;")
		write(writer,tab+tab+tab+"case R.id.add_action:")
		write(writer,tab+tab+tab+tab+"Intent addIntent = new Intent(this, "+capitalizedObjectName+"AddActivity.class);")
		write(writer,tab+tab+tab+tab+"startActivity(addIntent);")
		write(writer,tab+tab+tab+tab+"return true;")
		write(writer,tab+tab+tab+"case R.id.delete_action:")
		write(writer,tab+tab+tab+tab+"Intent getlistIntent = new Intent(this, "+capitalizedObjectName+"DeleteListActivity.class);")
		write(writer,tab+tab+tab+tab+"startActivity(getlistIntent);")
		write(writer,tab+tab+tab+tab+"return true;")
		write(writer,tab+tab+"}")
		write(writer,tab+tab+"return false;")
		write(writer,tab+"}")
		write(writer,"}")

		
		
		writer.close



	}
}
}
