package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import wordList.*;
public class GenerateWordListAndDefinitionListTest {

	@Test
	public void testIfGeneratedGMATFileExists() {
		String fileName = "GRE_PDFs/GMAT.json.new.json";
		File writeFile0 = new File("GRE_PDFs/GMATWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	@Test
	public void testIfGeneratedGREFileExists() {
		String fileName = "GRE_PDFs/GRE.json.new.json";
		File writeFile0 = new File("GRE_PDFs/GREWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	
	@Test
	public void testIfGeneratedSATFileExists() {
		String fileName = "GRE_PDFs/SAT.json.new.json";
		File writeFile0 = new File("GRE_PDFs/SATWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	
	@Test
	public void testIfGeneratedTOEFLFileExists() {
		String fileName = "GRE_PDFs/TOEFL.json.new.json";
		File writeFile0 = new File("GRE_PDFs/TOEFLWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		Boolean assertValue = false;
		if (writeFile0.exists()) {
			assertValue = true;
		}
		assertTrue(assertValue);
	}
	
	
	@Test
	public void testIfGeneratedGMATFileWordMatch() {
		String fileName = "GRE_PDFs/GMAT.json.new.json";
		File writeFile0 = new File("GRE_PDFs/GMATWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		String sCurrentLine = null;
		String result = null;
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(writeFile0));
			do{
				sCurrentLine = br.readLine();
				if(sCurrentLine!=null && (!sCurrentLine.equals(""))){
					result = sCurrentLine;
				}
			}while (sCurrentLine!=null && (!sCurrentLine.equals("")));
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		assertEquals(result,"zoom");
	}
	
	
	@Test
	public void testIfGeneratedSATFileWordMatch() {
		String fileName = "GRE_PDFs/GRE.json.new.json";
		File writeFile0 = new File("GRE_PDFs/GREWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		String sCurrentLine = null;
		String result = null;
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(writeFile0));
			do{
				sCurrentLine = br.readLine();
				if(sCurrentLine!=null && (!sCurrentLine.equals(""))){
					result = sCurrentLine;
				}
			}while (sCurrentLine!=null && (!sCurrentLine.equals("")));
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		assertEquals(result,"yarn");
	}
	
	
	@Test
	public void testIfGeneratedTOEFLFileWordMatch() {
		String fileName = "GRE_PDFs/SAT.json.new.json";
		File writeFile0 = new File("GRE_PDFs/SATWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		String sCurrentLine = null;
		String result = null;
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(writeFile0));
			do{
				sCurrentLine = br.readLine();
				if(sCurrentLine!=null && (!sCurrentLine.equals(""))){
					result = sCurrentLine;
				}
			}while (sCurrentLine!=null && (!sCurrentLine.equals("")));
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		assertEquals(result,"zodiac");
	}
	
	
	@Test
	public void testIfGeneratedGREFileWordMatch() {
		String fileName = "GRE_PDFs/TOEFL.json.new.json";
		File writeFile0 = new File("GRE_PDFs/TOEFLWordlist.txt");
		GenerateWordListAndDefinitionList.generateNewFile(fileName, writeFile0);
		String sCurrentLine = null;
		String result = null;
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(writeFile0));
			do{
				sCurrentLine = br.readLine();
				if(sCurrentLine!=null && (!sCurrentLine.equals(""))){
					result = sCurrentLine;
				}
			}while (sCurrentLine!=null && (!sCurrentLine.equals("")));
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		assertEquals(result,"ozone");
	}

}
