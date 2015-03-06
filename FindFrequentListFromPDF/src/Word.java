
public class Word {
	private String word;
	private String definition;
	private int frequency;
	Word(){}
	Word(String word, String definition, int frequency){
		this.word=word;
		this.definition=definition;
		this.frequency=frequency;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
}
