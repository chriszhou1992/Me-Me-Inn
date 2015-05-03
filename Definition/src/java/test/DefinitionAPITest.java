package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.meme.DefinitionAPI;
import com.meme.DefinitionHTTPS;

public class DefinitionAPITest {
	@Test
	public void testGetWordDefinition1(){
		assertEquals("Being",DefinitionAPI.wordToDefinition("ubiquitous").substring(0,5));
	}
	
	@Test
	public void testGetUnfoundWordDefinition2(){
		assertEquals("To strengthen or support with other evidence; make more certain. See Synonyms at confirm.",DefinitionAPI.wordToDefinition("corroborate"));
	}
	
	@Test
	public void testGetWordDefinition3(){
		assertEquals("Disagreeing, as in opinion or belief.",DefinitionAPI.wordToDefinition("dissident"));
	}
	
	@Test
	public void testGetWordDefinition4(){
		assertEquals("A chiefly Mediterranean perennial herb (Melissa officinalis) in the mint family, grown for its lemon-scented foliage, which is used as a seasoning or for tea. Also called lemon balm.",DefinitionAPI.wordToDefinition("balm"));
	}
	
	@Test
	public void testNullWord(){
		assertEquals("Having",DefinitionAPI.wordToDefinition(null).substring(0,6));
	}
	
	@Test
	public void testEmptyWord(){
		assertEquals(null,DefinitionAPI.wordToDefinition(""));
	}
}
