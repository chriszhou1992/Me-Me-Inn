

public class DataObject {
	private String createdAt;
    private String definition;
    private String objectId;
    private String updatedAt;
    private String word;
    private int frequency;

	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	@Override
	public String toString() {
		return "\"createdAt\":"+"\""+createdAt+"\","+
			   "\"definition\":"+"\""+definition+"\","+
			   "\"objectId\":"+"\""+objectId+"\","+
			   "\"updatedAt\":"+"\""+updatedAt+"\","+
			   "\"word\":"+"\""+word+"\"";
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
