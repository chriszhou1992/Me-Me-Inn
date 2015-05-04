package wordList;

import java.util.ArrayList;

/**
 * Convert json data imported from Parse database
 * into java object
 */
public class AllData {
	private ArrayList<DataObject> results;

	public ArrayList<DataObject> getResults() {
		return results;
	}

	public void setResult(ArrayList<DataObject> results) {
		this.results = results;
	}
	
}
