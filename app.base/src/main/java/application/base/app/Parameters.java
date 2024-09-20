package application.base.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * An improved method of handling parameters passed into a program from the
 * command line.
 * 
 * @author neville
 * @version 3.0.0
 */
public class Parameters {
	private Map<String, String> named = new HashMap<>();
	private ArrayList<String> raw = new ArrayList<>();

	/**
	 * Create the Parameters object.
	 * 
	 * @param parameters needing to be interpreted.
	 * @throws DuplicateParameterException if a parameter is specified twice.
	 */
	public Parameters(String[] parameters) {
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				raw.add(parameters[i]);
				if (parameters[i].startsWith("--") && parameters[i].indexOf("=") != -1) {
					StringTokenizer st = new StringTokenizer(parameters[i], "=");
					String key = st.nextToken().substring(2);
					String value = st.nextToken();
					if (named.containsKey(key)) {
						throw new DuplicateParameterException("Parameters: duplicate key = " + key);
					}
					named.put(key, value);
				}
			}
		}
	}

	/**
	 * Obtain the parameters as key, value pairs in a map.
	 * 
	 * @return the map containing the pairs.
	 */
	public Map<String, String> getNamed() {
		return named;
	}

	/**
	 * Obtain the parameters as passed into the object.
	 * 
	 * @return the parameters.
	 */
	public List<String> getRaw() {
		return raw;
	}
}
