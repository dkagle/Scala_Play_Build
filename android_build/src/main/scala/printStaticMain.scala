package ezbuilder {

import java.io._


class StaticMainActivity(basedir: String, schemas: scala.collection.mutable.ListBuffer[Map[String, List[String]]] ) extends scalaMVC {



	def print() = {

		val tab = "\t"
		val fileName = "MainActivity.java"
		val writer = new PrintWriter(new File(basedir+fileName))

		write(writer,"package com.scalaitoinfinity.androidbuild;")
		write(writer,"import android.app.Activity;")

		write(writer,"import android.content.Intent;")

		write(writer,"import android.os.Bundle;")

		write(writer,"import android.util.Log;")
		write(writer,"import android.view.Menu;")
		write(writer,"import android.view.MenuItem;")


		write(writer,"")
		write(writer,"import java.net.URL;")

		write(writer,"public class MainActivity extends Activity {")
		write(writer,tab+"public static final String TAG = \"AndroidBuild\";")
		write(writer,tab+"@Override")
		write(writer,tab+"protected void onCreate(Bundle savedInstanceState) {")
		write(writer,tab+"super.onCreate(savedInstanceState);")
		write(writer,tab+tab+"setContentView(R.layout.main);")
		write(writer,tab+"}")
		write(writer,tab+"@Override")
		write(writer,tab+"public boolean onCreateOptionsMenu(Menu menu) {")
		write(writer,tab+tab+"getMenuInflater().inflate(R.menu.main, menu);")
		write(writer,tab+tab+"return true;")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"@Override")
		write(writer,tab+"public boolean onOptionsItemSelected(MenuItem item) {")
		write(writer,tab+tab+"switch (item.getItemId()) {")
		for ( schema <- schemas ) {
			for ( (table, flds) <- schema )  {
					val Table=table.toLowerCase.capitalize
					write(writer,tab+tab+tab+"case R.id."+Table+":")
					write(writer,tab+tab+tab+tab+"Intent "+table.toLowerCase+"listIntent = new Intent(this, "+Table+"MainActivity.class);")
					write(writer,tab+tab+tab+tab+"startActivity("+table.toLowerCase+"listIntent);")
					write(writer,tab+tab+tab+tab+"return true;")
			}
		}
		write(writer,tab+tab+"}")
		write(writer,tab+tab+"return false;")
		write(writer,tab+"}")
		write(writer,"}")

		
		
		writer.close



	}
}
}
