package application.storage;

/**
 * Definition of methods needed to be implemented by classes used to load data
 * from storage
 * 
 * @author nevil
 *
 */
public interface LoadData extends Runnable {
	/**
	 * The name of the file that is to be loaded.
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName);

	/**
	 * The name of the file that is to be loaded.
	 * 
	 * @return fileName
	 */
	public String fileName();

}
