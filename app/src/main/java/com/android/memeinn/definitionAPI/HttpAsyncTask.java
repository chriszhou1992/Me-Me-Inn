package com.android.memeinn.definitionAPI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by yifan on 4/7/15.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    public ConnectivityManager connMgr;
    public Context context;
    public String result=null;
    public EditText editText;

    public HttpAsyncTask(ConnectivityManager connMgr, Context context, EditText editText){
        //connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        this.connMgr = connMgr;
        this.context = context;
        this.editText = editText;
    }

    @Override
    public String doInBackground(String... urls) {

        return GET(urls[0]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    public void onPostExecute(String result) {
        Toast.makeText(context, "Received!", Toast.LENGTH_LONG).show();
        try {
            JSONObject json = new JSONObject(result);

            String str = "";

            JSONArray definitions = json.getJSONArray("definitions");
            str = definitions.getJSONObject(0).getString("text");

            this.result=str;
            editText.setText(str);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);
            Header[] headers = new Header[2];
            headers[0] = new BasicHeader("X-Mashape-Key", "nCRyaWXsd2mshUNex28BHe2mxGFqp1ttnRljsnB74qRPINjEt3");
            headers[1] = new BasicHeader("Accept", "application/json");
            httpGet.setHeaders(headers);

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
