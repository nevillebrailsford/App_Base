package application.base.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Parameters {
	private Map<String, String> named = new HashMap<>();
	private ArrayList<String> raw = new ArrayList<>();

	/**
	 * An improved method of handling parameters passed into a program from the
	 * command line.
	 * 
	 * @param parameters
	 * @throws DuplicateParameterException
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

	public Map<String, String> getNamed() {
		return named;
	}

	public List<String> getRaw() {
		return raw;
	}
}
