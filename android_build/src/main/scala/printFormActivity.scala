package ezbuilder {

import java.io._


class FormActivity(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val fileName = objectName.capitalize+"FormActivity.java"
		val writer = new PrintWriter(new File(basedir+fileName))
		calculate()
		write(writer,"package com.scalaitoinfinity.androidbuild;")
		write(writer,"import android.app.Activity;")
		write(writer,"import android.app.Fragment;")
		write(writer,"import android.content.Intent;")
		write(writer,"import android.os.AsyncTask;")
		write(writer,"import android.os.Bundle;")
		write(writer,"import android.support.v4.app.NavUtils;")
		write(writer,"import android.view.LayoutInflater;")
		write(writer,"import android.view.View;")
		write(writer,"import android.view.ViewGroup;")
		write(writer,"import android.widget.EditText;")
		write(writer,"import android.widget.TextView;")
		write(writer,"")
//		write(writer,"import com.example.android.common.logger.Log;")
		write(writer,"import org.apache.http.HttpResponse;")
		write(writer,"import org.apache.http.client.methods.HttpPut;")
		write(writer,"import org.apache.http.impl.client.DefaultHttpClient;")
		write(writer,"")
		write(writer,"import java.net.URL;")
		write(writer,"import java.util.ArrayList;")
		write(writer,"public class "+capitalizedObjectName+"FormActivity extends Activity {")

		write(writer,"")
		write(writer,tab+"@Override")
		write(writer,tab+"protected void onCreate(Bundle savedInstanceState) {")
		write(writer,tab+tab+"super.onCreate(savedInstanceState);")
		write(writer,tab+tab+"Intent intent = getIntent();")
		write(writer,tab+tab+"setContentView(R.layout."+objectName+"form);")
		write(writer,tab+tab+"Bundle extras = intent.getExtras();")
		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val fn = Some(fieldName)		
			write(writer,tab+tab+"TextView "+fn.get+"TextView = (TextView) findViewById(R.id."+fn.get+");")
			write(writer,tab+tab+fn.get+"TextView.setText(extras.getString(\""+fn.get.toUpperCase+"\"));")
		}
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"public static class PlaceholderFragment extends Fragment {")
		write(writer,tab+tab+"public PlaceholderFragment() {")
		write(writer,tab+tab+"}")
		write(writer,tab+tab+"@Override")
		write(writer,tab+tab+"public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {")
		write(writer,tab+tab+tab+"View rootView = inflater.inflate(R.layout."+objectName+"form, container, false);")
		write(writer,tab+tab+tab+"return rootView;")
		write(writer,tab+tab+"}")
		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+"")
		write(writer,tab+"public void sendMessage(View view) {")

		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val fn = Some(fieldName)
			if ( fn.get == primaryKey.get ) 
				write(writer,tab+tab+"TextView "+fn.get+"text = (TextView) findViewById(R.id."+fn.get+");")
			else
				write(writer,tab+tab+"EditText "+fn.get+"text = (EditText) findViewById(R.id."+fn.get+");")
			write(writer,tab+tab+"String "+fn.get+" = "+fn.get+"text.getText().toString();")
			write(writer,tab+tab+fn.get+" = "+fn.get+".replace(\" \",\"%20\");")
		}

		write(writer,tab+tab+"new UploadTask().execute(\""+url+objectName+updateURL+");")

		write(writer,tab+tab+"NavUtils.navigateUpFromSameTask(this);")

		write(writer,tab+"}")
		write(writer,"")
		write(writer,tab+tab+"private class UploadTask extends AsyncTask<String, Void, String> {")
		write(writer,tab+tab+tab+"@Override")
		write(writer,tab+tab+tab+"protected String doInBackground(String... urls) {")		
		write(writer,tab+tab+tab+tab+"try {")
		write(writer,tab+tab+tab+tab+tab+"URL url = new URL(urls[0].toString());")
		write(writer,tab+tab+tab+tab+tab+"DefaultHttpClient httpClient = new DefaultHttpClient();")
		write(writer,tab+tab+tab+tab+tab+"HttpPut httpPut = new HttpPut(url.toString());")
		write(writer,tab+tab+tab+tab+tab+"HttpResponse httpResponse = httpClient.execute(httpPut);")
		write(writer,tab+tab+tab+tab+tab+"return url.toString();")
		write(writer,tab+tab+tab+tab+" } catch ( Exception e ) {")
		write(writer,tab+tab+tab+tab+tab+"return e.toString();")
		write(writer,tab+tab+tab+tab+"}")
		write(writer,tab+tab+tab+"}")
		write(writer,tab+tab+"}")
		write(writer,tab+"}")
		writer.close()
	}
}
}
