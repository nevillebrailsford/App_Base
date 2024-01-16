package application.storage;

public class StoreDetails {
	private LoadData loadData = null;
	private String modelDirectory = "";
	private String modelFileName = "";

	public StoreDetails(LoadData loadData, String modelDirectory, String modelFileName) {
		this.loadData = loadData;
		this.modelDirectory = modelDirectory;
		this.modelFileName = modelFileName;
	}

	public LoadData loadData() {
		return loadData;
	}

	public String modelDirectory() {
		return modelDirectory;
	}

	public String modelFileName() {
		return modelFileName;
	}

}
