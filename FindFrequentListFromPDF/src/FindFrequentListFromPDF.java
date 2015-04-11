import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.gson.Gson;

public class FindFrequentListFromPDF {
	static Map<String, Integer> wordFreq = new HashMap<String, Integer>();
	static ArrayList<Word> baseWordList = new ArrayList<Word>();
	// convert one pdf to WordFrequencyHashmap
	static void pdftoHashmap(String fileName, boolean isBase) {
		PDFParser parser;
		String parsedText = null;;
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		File file = new File("GRE_PDFs/"+fileName);
		if (!file.isFile()) {
			System.err.println("File " + fileName + " does not exist.");
			return;
		}
		try {
			parser = new PDFParser(new FileInputStream(file));
		} catch (IOException e) {
			System.err.println("Unable to open PDF Parser. " + e.getMessage());
			return;
		}
		try {
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			int numOfPages = pdDoc.getNumberOfPages();
			
			//Process Page Range from 0 to endOfPDF, with file name to Word Frequency Hashmap
			for(int i=1;i<=numOfPages;i++){
				System.out.println("processing file "+ fileName +" page "+i);
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
				parsedText = pdfStripper.getText(pdDoc);
				ArrayList<String> pageInWords = stripPageToStringArray(parsedText);
				stringArrayToHashmap(pageInWords, isBase);
			}
		} catch (Exception e) {
			System.err
					.println("An exception occured in parsing the PDF Document."
							+ e.getMessage());
		} finally {
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//Strip Page String into String Array
	static ArrayList<String> stripPageToStringArray(String str){
		ArrayList<String> newArr = new ArrayList<String>();
		str = str.toLowerCase();
		String tempS = "";
		for(int i=0;i<str.length();i++){
			char curChar = str.charAt(i);
			int curInt = (int)curChar;
			if(curInt>=97 && curInt<=122){
				tempS+=curChar;
			}else{
				if(!tempS.equals(""))
					newArr.add(tempS);
				tempS ="";
			}
		}
		return newArr;
	}
	
	//Feed String Array HashMap of String to Frequency(int)
	static void stringArrayToHashmap(ArrayList<String> strs, boolean isBase){
			for(String cur:strs){
				if(!cur.equals("")){
					if(!isBase) {
						if(wordFreq.containsKey(cur)){
							wordFreq.put(cur, wordFreq.get(cur)+1);
						}else{
							wordFreq.put(cur, 1);
						}
					}else {// this is for base wordList
						baseWordList.add(new Word(cur,null,1));
					}
				}
			}
	}
	
	public static void readMultipleFiles(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input nubmer of files:");
		int numOfF = scan.nextInt();
		for(int i=0;i<numOfF;i++){
			System.out.println("Processing file "+i);
			String fileName =i+".pdf";
			pdftoHashmap(fileName,false);
		}
	}
	
	public static void main(String args[]) throws IOException{
		
		readMultipleFiles();
		int maxFreq = 0;
	    Iterator it0 = wordFreq.entrySet().iterator();
	    while (it0.hasNext()) {
	        Map.Entry pair = (Map.Entry)it0.next();
	        int curFeq = (int) pair.getValue();
	        if(curFeq>maxFreq) maxFreq = curFeq;
	    }
	    
	    /*
	    //Create an array based on maxFeq
	    String[] words = new String[maxFreq+1];
	    Iterator it1 = wordFreq.entrySet().iterator();
	    while (it1.hasNext()) {
	        Map.Entry pair = (Map.Entry)it1.next();
	        words[(int) pair.getValue()] = (String) pair.getKey();
	    }
	    
	    
	    for(int i=words.length-1;i>=0;i--){
	    	if(words[i]==null) continue;
	    	//if(words[i].length()>6){
	    		System.out.println(words[i]+":"+(i));
	    	//}
	    }*/
	    
	   pdftoHashmap("wordList1.pdf",true);
//	   for(Word w:baseWordList) {
//		    	w.setFrequency(wordFreq.get(w.getWord()));
//		    System.out.println(w.getWord()+":"+w.getFrequency());
//	   }
	   
	   
	   //read all definitions
//        BufferedReader br = null;
//		String sCurrentLine;
//		br = new BufferedReader(new FileReader("GRE_PDFs/definitionList1.txt"));
//		int lineNum=0;
//		while ((sCurrentLine = br.readLine()) != null) {
//			baseWordList.get(lineNum).setDefinition(sCurrentLine);
//			Word curWord = baseWordList.get(lineNum);
//			System.out.println(curWord.getWord()+":"+curWord.getDefinition()+":"+curWord.getFrequency());
//			lineNum++;
//		}
//	   
//		br.close();
		
		//Creat new CSV
		
		
//		File file =new File("wordList.csv");
//		 
//		//if file doesnt exists, then create it
//		if(!file.exists()){
//			file.createNewFile();
//		}
//
//		//true = append file
//		FileWriter fileWriter = new FileWriter(file.getName());
//        BufferedWriter bw = new BufferedWriter(fileWriter);
//        bw.write("word,definition,frequency");
//        for (Word w:baseWordList) {
//			bw.write("\n"+w.getWord()+","+w.getDefinition()+","+w.getFrequency());
//		}
//		bw.close();
//		fileWriter.close();
		
	   
	   BufferedReader br = new BufferedReader(new FileReader("GRE_PDFs/TOEFL.json.new.json"));
	   	Gson gson = new Gson();
	   //convert the json string back to object
		AllData obj = gson.fromJson(br, AllData.class);
		ArrayList<DataObject> results = obj.getResults();
		
		Iterator iter = results.iterator();
		
		int count = 0;
		while (iter.hasNext()) {
			try{
			System.out.println(count++);
		    DataObject dataObject = (DataObject) iter.next();
			dataObject.setFrequency(wordFreq.get(dataObject.getWord()));
			}
			catch(NullPointerException e){
			 iter.remove();
			}
		}
	   
	   
		File writeFile = new File("TOEFL_NEW_FEQ.json");
		 
		// if file doesnt exists, then create it
		if (!writeFile.exists()) {
			writeFile.createNewFile();
		}
		
		FileWriter fw = new FileWriter(writeFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		String json = gson.toJson(obj);
		bw.write(json);
		bw.close();
	}

}