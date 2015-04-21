package com.meme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
//import com.parse.FindCallback;
//import com.parse.Parse;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;

public class DefinitionAPI{
	// These code snippets use an open-source library. http://unirest.io/java
	public static void main(String[] args) throws UnirestException{
		String word = "butterfly";
		//wordToDefinition(word);
		//parse();
	}
	
	public static void parse(){

		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get("https://api.parse.com/1/classes/GMAT")
					.header("X-Parse-Application-Id", "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ")
					.header("X-Parse-REST-API-Key", "f052MmT8KHViGGO3vwAQ9WxMC3cSt2R1nc2tEWlG")
					.header("Content-Type", "application/json")
					.asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonNode myBody = response.getBody();
		JSONArray wordArray = myBody.getObject().getJSONArray("results");
		for(int i=0;i<wordArray.length();i++){
			JSONObject currentWordObject = wordArray.getJSONObject(i);
			System.out.println(currentWordObject.get("word"));
			System.out.println(i);
		}
		System.out.println(myBody);
	}
	
	public static String wordToDefinition(String word){
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get("https://montanaflynn-dictionary.p.mashape.com/define?word="+word)
					.header("X-Mashape-Key", "nCRyaWXsd2mshUNex28BHe2mxGFqp1ttnRljsnB74qRPINjEt3")
					.header("Accept", "application/json")
					.asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			return null;
		}
		String firstDefinition = null;
		try{
			JsonNode myBody = response.getBody();
			JSONArray myDefinitions = myBody.getObject().getJSONArray("definitions");
			firstDefinition = (String) myDefinitions.getJSONObject(0).get("text");
			System.out.println(firstDefinition);
		}catch(JSONException e){
			return null;
		}
		return firstDefinition;
	}
}
