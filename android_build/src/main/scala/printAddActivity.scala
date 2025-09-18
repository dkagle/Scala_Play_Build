package ezbuilder {

import java.io._


class AddActivity(objectName: String, fields: List[String], primaryKey: Option[String], basedir: String, url: String ) extends scalaMVCFile(objectName, fields, primaryKey, basedir, url)   {

	def print() = {
		val fileName = objectName.capitalize+"AddActivity.java"
		val writer = new PrintWriter(new File(basedir+fileName))
		calculate()
		write(writer,"package com.scalaitoinfinity.androidbuild;")
		write(writer,"import android.app.Activity;")
		write(writer,"import android.app.Fragment;")
		write(writer,"import android.content.Intent;")
		write(writer,"import android.os.AsyncTask;")
		write(writer,"import android.os.Bundle;")
		write(writer,"import android.view.LayoutInflater;")
		write(writer,"import android.view.View;")
		write(writer,"import android.view.ViewGroup;")
		write(writer,"import android.widget.Button;")
		write(writer,"import android.widget.EditText;")
		write(writer,"import android.widget.TextView;")
		write(writer,"")
//		write(writer,"import com.example.android.common.logger.Log;")
		write(writer,"")
		write(writer,"import org.apache.http.HttpResponse;")
		write(writer,"import org.apache.http.NameValuePair;")
		write(writer,"import org.apache.http.client.methods.HttpPost;")
		write(writer,"import org.apache.http.entity.StringEntity;")
		write(writer,"import org.apache.http.impl.client.DefaultHttpClient;")
		write(writer,"import java.net.URL;")
		write(writer,"import java.util.ArrayList;")
		write(writer,"import java.util.List;")
		write(writer,"")
		write(writer,"public class "+capitalizedObjectName+"AddActivity extends Activity {")
		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val fn = Some(fieldName)

			write(writer,tab+"String "+fn.get+" = null;")
		}
		write(writer,tab+"@Override")
		write(writer,tab+"protected void onCreate(Bundle savedInstanceState) {")
		write(writer,tab+tab+"super.onCreate(savedInstanceState);")
		write(writer,tab+tab+"Intent intent = getIntent();")
		write(writer,tab+tab+"ArrayList result = intent.getCharSequenceArrayListExtra(\""+objectName+"\");")
		write(writer,tab+tab+"setContentView(R.layout."+objectName+"form);")
		write(writer,tab+tab+"Button button = (Button)findViewById(R.id.Send);")
		write(writer,tab+tab+"button.setText(\"Add\");")
		write(writer,tab+"}")
		write(writer,tab+"")
		write(writer,tab+"public static class PlaceholderFragment extends Fragment {")
		write(writer,tab+tab+"public PlaceholderFragment() {}")
		write(writer,tab+tab+"@Override")
		write(writer,tab+tab+"public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {")
		write(writer,tab+tab+tab+"View rootView = inflater.inflate(R.layout."+objectName+"form, container, false);")
		write(writer,tab+tab+tab+"return rootView;")
		write(writer,tab+tab+"}")
		write(writer,tab+"}")
		write(writer,tab+"public void sendMessage(View view) {")
		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val fn = Some(fieldName)
			var view = ""
			if ( fn.get == primaryKey.get ) 
				view = "TextView";
			else
				view = "EditText";
			write(writer,tab+tab+view+" "+fn.get+" = ("+view+") findViewById(R.id."+fn.get+");")
		}
		for ( f <- fields ) {
			val fieldName = getFieldName(f)
			val fn = Some(fieldName)
			write(writer,tab+tab+"this."+fn.get+" = "+fn.get+".getText().toString();")

		}
		write(writer,tab+tab+"new UploadTask().execute(\""+url+objectName+"\");")
		write(writer,tab+tab+"}")
		write(writer,tab+"")
		write(writer,tab+"private class UploadTask extends AsyncTask<String, Void, String> {")
		write(writer,tab+"@Override")
		write(writer,tab+"protected String doInBackground(String... urls) {")
		write(writer,tab+"try {")
		write(writer,tab+tab+tab+tab+"URL url = new URL(urls[0].toString());")
		write(writer,tab+tab+tab+tab+"DefaultHttpClient httpClient = new DefaultHttpClient();")
		write(writer,tab+tab+tab+tab+"HttpPost httpPost = new HttpPost(url.toString());")
		write(writer,tab+tab+tab+tab+"httpPost.addHeader(\"Content-Type\", \"application/json\");")
		write(writer,tab+tab+tab+tab+"String insertString = "+jsonAdd+";")
		write(writer,tab+tab+tab+tab+"StringEntity entity = new StringEntity(insertString);")
		write(writer,tab+tab+tab+tab+"httpPost.setEntity(entity);")
		write(writer,tab+tab+tab+tab+"HttpResponse httpResponse = httpClient.execute(httpPost);")
		write(writer,tab+tab+tab+tab+"return url.toString();")
		write(writer,tab+tab+tab+" } catch ( Exception e ) {")
//		write(writer,tab+tab+tab+tab+"Log.i(\"ERROR\", e.toString());")
		write(writer,tab+tab+tab+tab+"return e.toString();")
		write(writer,tab+tab+tab+"}")
		write(writer,tab+tab+"}")
		write(writer,tab+"}")
		write(writer,"}")

		writer.close()

	}
}
}
