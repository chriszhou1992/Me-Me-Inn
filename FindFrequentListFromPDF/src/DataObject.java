

public class DataObject {
	private String createdAt;
    private String defination;
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
	public String getDefination() {
		return defination;
	}
	public void setDefination(String defination) {
		this.defination = defination;
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
			   "\"definition\":"+"\""+defination+"\","+
			   "\"objectId\":"+"\""+objectId+"\","+
			   "\"updatedAt\":"+"\""+updatedAt+"\","+
			   "\"word\":"+"\""+word+"\"";
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
