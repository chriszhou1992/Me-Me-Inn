package com.meme;

import static org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.StatusLine;
import org.apache.http.HttpEntity;

import java.io.InputStream;

public class DefinitionHTTPS {
	public DefinitionHTTPS(){
		
	}
	public static void main(String[] args){
		String defination = givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException("ubiquitous");
		System.out.println(defination);
	}
	public static String givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException(String word){
		
		String ret="";
		 //Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		
		 Header header0 = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		 Header header1 = new BasicHeader("X-Mashape-Key", "nCRyaWXsd2mshUNex28BHe2mxGFqp1ttnRljsnB74qRPINjEt3");
		 Header header2 = new BasicHeader("Accept", "application/json");
		 
		 List<Header> headers = Lists.newArrayList(header0);
		 
		 headers.add(header1); headers.add(header2);
		 
		 TrustStrategy acceptingTrustStrategy = new TrustStrategy() { @Override public boolean isTrusted(X509Certificate[] certificate, String authType) { return true;
		 }
		 };
		 SSLSocketFactory sf = null;
			try {
				sf = new SSLSocketFactory(acceptingTrustStrategy,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			} catch (KeyManagementException | UnrecoverableKeyException
					| NoSuchAlgorithmException | KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 SchemeRegistry registry = new SchemeRegistry();
		 registry.register(new Scheme("https", 8443, sf));
		 ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);
		 DefaultHttpClient httpClient = new DefaultHttpClient(ccm);
		 
		 
		 String SAMPLE_URL = "https://montanaflynn-dictionary.p.mashape.com/define?word="+word;
		 HttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();
		 HttpUriRequest request = RequestBuilder.get().setUri(SAMPLE_URL).build();
		 HttpResponse response = null;
		 try {
			response = client.execute(request);
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 
		//Perform the request and check the status code
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			InputStream content = null;
			try {
				content = entity.getContent();
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			/*
			 * For contnt String return
			 */
			
			/*
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(content));
			String line;
			StringBuilder sb = new StringBuilder();
			try {
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ret = sb.toString();
			*/
			
			
			//Read the server response and attempt to parse it as JSON
				try {
					//Read the server response and attempt to parse it as JSON
					Reader reader = new InputStreamReader(content);
					
					GsonBuilder gsonBuilder = new GsonBuilder();
	                //gsonBuilder.setDateFormat("M/d/yy hh:mm a");
					Gson gson = gsonBuilder.create();
					DefinitionEntities definitionEntities = gson.fromJson(reader, DefinitionEntities.class);
					ret = definitionEntities.getDefinitions().get(0).getText();
					content.close();
					
				} catch (Exception ex) {
					System.out.println("Failed to parse JSON due to: " + ex);
				}
			} else {
					System.out.println("Server responded with status code: " + statusLine.getStatusCode());
			}
		
		 return ret;
		 
		 /*
		  * for request without header
		  */
		 /*
		 String urlOverHttps ="https://www.google.com";
		 HttpGet getMethod = new HttpGet(urlOverHttps);
		 HttpResponse response = httpClient.execute(getMethod);*/

		 //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));

	 }

}
