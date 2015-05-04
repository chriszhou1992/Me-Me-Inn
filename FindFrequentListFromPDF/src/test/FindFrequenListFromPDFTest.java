package test;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.Test;
import com.google.gson.Gson;
import wordList.AllData;
import wordList.DataObject;
import wordList.FindFrequentListFromPDF;
import wordList.GenerateWordListAndDefinitionList;

/**
 * This class test all of the functions implemented
 * in FindFrequentListFromPDF class
 */
public class FindFrequenListFromPDFTest {

	@Test
	public void testIfGeneratedTOEFL_FEQExists() throws IOException {
		String wordList = "TOEFLWordList.pdf";
		String newFile = "GRE_PDFs/TOEFL.json.new.json";
		String newJSON = "TOEFL_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		File writeFile0 = new File(newJSON);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	
	@Test
	public void testIfGeneratedGRE_FEQExists() throws IOException {
		String wordList = "GREWordList.pdf";
		String newFile = "GRE_PDFs/GRE.json.new.json";
		String newJSON = "GRE_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		File writeFile0 = new File(newJSON);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	@Test
	public void testIfGeneratedSAT_FEQExists() throws IOException {
		String wordList = "SATWordList.pdf";
		String newFile = "GRE_PDFs/SAT.json.new.json";
		String newJSON = "SAT_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		File writeFile0 = new File(newJSON);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	@Test
	public void testIfGeneratedGMAT_FEQExists() throws IOException {
		String wordList = "GMATWordList.pdf";
		String newFile = "GRE_PDFs/GMAT.json.new.json";
		String newJSON = "GMAT_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		File writeFile0 = new File(newJSON);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	
	@Test
	public void testIfGeneratedTOEFLFrequencyMatch() throws IOException {
		String wordList = "TOEFLWordList.pdf";
		String newFile = "GRE_PDFs/TOEFL.json.new.json";
		String newJSON = "TOEFL_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		
	   BufferedReader br = new BufferedReader(new FileReader(newJSON));
	   	Gson gson = new Gson();
	   //convert the json string back to object
		AllData obj = gson.fromJson(br, AllData.class);
		ArrayList<DataObject> results = obj.getResults();
		Iterator iter = results.iterator();
		DataObject dataObject = (DataObject) iter.next();
		assertEquals(50, dataObject.getFrequency());
	}
	
	@Test
	public void testIfGeneratedSATFrequencyMatch() throws IOException {
		String wordList = "SATWordList.pdf";
		String newFile = "GRE_PDFs/SAT.json.new.json";
		String newJSON = "SAT_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		
	   BufferedReader br = new BufferedReader(new FileReader(newJSON));
	   	Gson gson = new Gson();
	   //convert the json string back to object
		AllData obj = gson.fromJson(br, AllData.class);
		ArrayList<DataObject> results = obj.getResults();
		Iterator iter = results.iterator();
		DataObject dataObject = (DataObject) iter.next();
		assertEquals(4, dataObject.getFrequency());
	}
	
	
	@Test
	public void testIfGeneratedGREFrequencyMatch() throws IOException {
		String wordList = "GREWordList.pdf";
		String newFile = "GRE_PDFs/GRE.json.new.json";
		String newJSON = "GRE_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		
	   BufferedReader br = new BufferedReader(new FileReader(newJSON));
	   	Gson gson = new Gson();
	   //convert the json string back to object
		AllData obj = gson.fromJson(br, AllData.class);
		ArrayList<DataObject> results = obj.getResults();
		Iterator iter = results.iterator();
		DataObject dataObject = (DataObject) iter.next();
		assertEquals(3, dataObject.getFrequency());
	}
	
	
	@Test
	public void testIfGeneratedGMATFrequencyMatch() throws IOException {
		String wordList = "GMATWordList.pdf";
		String newFile = "GRE_PDFs/GMAT.json.new.json";
		String newJSON = "GMAT_NEW_FEQ.json";
		
		FindFrequentListFromPDF.generateNewJSON(wordList, newFile, newJSON);
		
	   BufferedReader br = new BufferedReader(new FileReader(newJSON));
	   	Gson gson = new Gson();
	   //convert the json string back to object
		AllData obj = gson.fromJson(br, AllData.class);
		ArrayList<DataObject> results = obj.getResults();
		Iterator iter = results.iterator();
		DataObject dataObject = (DataObject) iter.next();
		assertEquals(16, dataObject.getFrequency());
	}

}
