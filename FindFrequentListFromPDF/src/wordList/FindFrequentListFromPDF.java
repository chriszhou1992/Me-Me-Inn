package wordList;
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

    /**
     * This function extracts word strings from the inpput pdf file,
     * load then into memory and store them in hashmap
     * @param fileName The filename of the input PDF file
     */
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

    /**
     * This function parses the strings of a pdf page
     * into a java string in the form of arrayList
     * @param str input string extracted from pdf page
     * @return java string converted from pdf page
     */
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

    /**
     * This function parses a string into a list of words and increment counts of
     * these words in a hashmap
     * @param strs input string that needs to be parsed
     */
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

    /**
     * This helper function reads multiple pdf files at the same time
     * and make them ready for processing
     */
	public static void readMultipleFiles(){
		
		int numOfF = 4;
		for(int i=0;i<numOfF;i++){
			System.out.println("Processing file "+i);
			String fileName =i+".pdf";
			pdftoHashmap(fileName,false);
		}
	}


    /**
     * This function writes data into jason file as output, which
     * contains vocabulary as well as its frequency
     * @param wordList data that needs to be write into json file
     * @param newFile filename of output file
     * @param newJSON output jason data
     */
	public static void generateNewJSON(String wordList, String newFile, String newJSON) throws IOException{
		readMultipleFiles();
		int maxFreq = 0;
	    Iterator it0 = wordFreq.entrySet().iterator();
	    while (it0.hasNext()) {
	        Map.Entry pair = (Map.Entry)it0.next();
	        int curFeq = (int) pair.getValue();
	        if(curFeq>maxFreq) maxFreq = curFeq;
	    }
	      
	   pdftoHashmap(wordList,true);
   
	   BufferedReader br = new BufferedReader(new FileReader(newFile));
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
	   
	   
		File writeFile = new File(newJSON);
		 
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

    /**
     * This is the main function that calculates the frequency of
     * vocabulary that have been stored in the database
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException{
        String wordList = "wordList1.pdf";
        String newFile = "GRE_PDFs/TOEFL.json.new.json";
        String newJSON = "TOEFL_NEW_FEQ.json";
        generateNewJSON(wordList, newFile, newJSON);
    }

}