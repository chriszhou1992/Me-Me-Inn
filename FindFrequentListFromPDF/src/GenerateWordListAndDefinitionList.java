import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.meme.AllData;
import com.meme.DataObject;
import com.meme.DefinitionAPI;


public class GenerateWordListAndDefinitionList {
	Gson gson = new Gson();
	DefinitionAPI definitionAPI = new DefinitionAPI(); 
	try {
 
		BufferedReader br = new BufferedReader(new FileReader(fileName));
 
		//convert the json string back to object
		AllData obj = gson.fromJson(br, AllData.class);
		ArrayList<DataObject> results = obj.getResults();
		
		
		Iterator<DataObject> iter = results.iterator();

		while (iter.hasNext()) {
		    DataObject dataObject = iter.next();
		    String word =dataObject.getWord();
		    if (word ==null || word.contains(" "))
		        iter.remove();
		}
		
		iter = results.iterator();
		
		int count = 0;
		while (iter.hasNext()) {
			System.out.println(count++);
		    DataObject dataObject = iter.next();
			String defination = DefinitionAPI.wordToDefinition(dataObject.getWord());
			if(defination==null){
				iter.remove();
			}else{
				dataObject.setDefination(defination);
			}
		}
		
		
		File writeFile = new File(fileName+".new.json");
		 
		// if file doesnt exists, then create it
		if (!writeFile.exists()) {
			writeFile.createNewFile();
		}

		FileWriter fw = new FileWriter(writeFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		String json = gson.toJson(obj);
		bw.write(json);
		bw.close();
 
	} catch (IOException e) {
		e.printStackTrace();
	}
}
}
