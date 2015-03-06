import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * In this class, we use a sample PDF and test on the generated hash table 
 * @author yifanwang
 *
 */
public class FrequencyTest {
	
	@Before
	/**
	 * Generate word frequency hash table based on PDFs
	 */
	public void preprocessing(){
		FindFrequentListFromPDF.pdftoHashmap("testdoc.pdf", false);
	}
	
	@Test
	/**
	 * Test if PDF files are successfully processed into hashmap tables 
	 */
	public void testProcessPDFFile() {
		assertEquals("not null", FindFrequentListFromPDF.wordFreq.size()>0, true);
	}

	@Test 
	/**
	 * Test the existance of words 
	 */
	public void testExistsWord(){
	    
		/*Iterator it0 = FindFrequentListFromPDF.wordFreq.entrySet().iterator();
	    while (it0.hasNext()) {
	        Map.Entry pair = (Map.Entry)it0.next();
	        System.out.println(pair.getKey() + ": " + pair.getValue());	       
	    }
	    */
		assertEquals("find his", FindFrequentListFromPDF.wordFreq.get("his").toString(), "3");
		assertEquals("find Othello", FindFrequentListFromPDF.wordFreq.get("othello").toString(), "1");
		assertEquals("find Merlin", FindFrequentListFromPDF.wordFreq.get("merlin").toString(), "1");	
	}
	
	@Test 
	/**
	 * Test the non-exist word
	 */
	public void testNonExistWord() {
		assertEquals("no beautiful exist", FindFrequentListFromPDF.wordFreq.get("beautiful"), null);
		assertEquals("no strong exist", FindFrequentListFromPDF.wordFreq.get("strong"), null);
		assertEquals("no computer exist", FindFrequentListFromPDF.wordFreq.get("computer"), null);
	}
}
