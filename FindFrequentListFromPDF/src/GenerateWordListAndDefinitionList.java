import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;


public class GenerateWordListAndDefinitionList {
	public static void main(String[] args){
		Gson gson = new Gson();
		String fileName = "GRE_PDFs/GMAT.json.new.json";
		//String fileName = "GRE_PDFs/SAT.json.new.json";
		//String fileName = "GRE_PDFs/TOEFL.json.new.json";
		try {
	 
			BufferedReader br = new BufferedReader(new FileReader(fileName));
	 
			//convert the json string back to object
			AllData obj = gson.fromJson(br, AllData.class);
			ArrayList<DataObject> results = obj.getResults();
			
			File writeFile0 = new File("GRE_PDFs/wordList1.txt");
			 
			// if file doesnt exists, then create it
			if (!writeFile0.exists()) {
				writeFile0.createNewFile();
			}
	
			FileWriter fw0 = new FileWriter(writeFile0);
			BufferedWriter bw0 = new BufferedWriter(fw0);
			
			File writeFile1 = new File("GRE_PDFs/definitionList1.txt");
			 
			// if file doesnt exists, then create it
			if (!writeFile1.exists()) {
				writeFile1.createNewFile();
			}
	
			FileWriter fw1 = new FileWriter(writeFile1);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			
			
			Iterator<DataObject> iter = results.iterator();
	
			while (iter.hasNext()) {
			    DataObject dataObject = iter.next();
			    String word =dataObject.getWord();
			    String definition = dataObject.getDefinition();
			    bw0.write(word+"\n");
			    bw1.write(definition+"\n");
			}
			
			bw0.close(); bw1.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
