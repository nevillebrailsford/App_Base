package application.storage;

/**
 * Definition of methods needed to be implemented by classes used to load data
 * from storage
 * 
 * @author nevil
 *
 */
public interface StoreData extends Runnable {
	/**
	 * The name of the file that is to be loaded.
	 * 
	 * @param fileName - a string containing the location of the file used to store
	 *                 the model for this application.
	 */
	public void setFileName(String fileName);

	/**
	 * The name of the file that is to be loaded.
	 * 
	 * @return fileName - a string containing the location of the file used to store
	 *         the model for this application.
	 */
	public String fileName();

}
