package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.meme.DefinitionHTTPS;

public class DefinitionHTTPSTest {

	@Test
	public void testGetWordDefinition1(){
		DefinitionHTTPS definitionHTTPS = new DefinitionHTTPS();
		assertEquals("Being",definitionHTTPS.givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException("ubiquitous").substring(0,5));
	}
	
	@Test
	public void testGetUnfoundWordDefinition2(){
		DefinitionHTTPS definitionHTTPS = new DefinitionHTTPS();
		assertEquals("To strengthen or support with other evidence; make more certain. See Synonyms at confirm.",definitionHTTPS.givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException("corroborate"));
	}
	
	@Test
	public void testGetWordDefinition3(){
		DefinitionHTTPS definitionHTTPS = new DefinitionHTTPS();
		assertEquals("Disagreeing, as in opinion or belief.",definitionHTTPS.givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException("dissident"));
	}
	
	@Test
	public void testGetWordDefinition4(){
		DefinitionHTTPS definitionHTTPS = new DefinitionHTTPS();
		assertEquals("A chiefly Mediterranean perennial herb (Melissa officinalis) in the mint family, grown for its lemon-scented foliage, which is used as a seasoning or for tea. Also called lemon balm.",definitionHTTPS.givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException("balm"));
	}
	
	@Test
	public void testNullWord(){
		DefinitionHTTPS definitionHTTPS = new DefinitionHTTPS();
		assertEquals("Having",definitionHTTPS.givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException(null).substring(0,6));
	}
	
	@Test
	public void testEmptyWord(){
		DefinitionHTTPS definitionHTTPS = new DefinitionHTTPS();
		assertEquals("",definitionHTTPS.givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException(""));
	}
	
}
