package com.meme;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://www.google.com");
        // replace with your url

        HttpResponse response=null;
        try {
            response = client.execute(request);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(response);
    }
}
