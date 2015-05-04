package com.android.memeinn.definitionAPI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    public ConnectivityManager connMgr;
    public Context context;
    public String result=null;
    public TextView editText;

    /**
     * Consturctor
     * @param connMgr
     * @param context
     * @param editText
     */
    public HttpAsyncTask(ConnectivityManager connMgr, Context context, TextView editText){
        this.connMgr = connMgr;
        this.context = context;
        this.editText = editText;
    }

    @Override
    public String doInBackground(String... urls) {
        return GET(urls[0]);
    }

    /**
     * onPostExecute displays the results of the AsyncTask.
     * After the post operation is executed, update the text view according
     * to the returned string result
     * @param result
     */
    @Override
    public void onPostExecute(String result) {
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


    /**
     * Http method Get method set up and calling
     * Including the correct user token for online dictionary API call
     * Including correct JSON header and url for the Get Call
     * @param url
     * @return
     */
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

    /**
     * Convert the input stream into readable string format
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
