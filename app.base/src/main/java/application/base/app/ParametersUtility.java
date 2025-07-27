package application.base.app;

import java.util.Iterator;

/**
 * A utility class to process the parameters passed into an application.
 * 
 * @author neville
 * @version 4.1.0
 */
public class ParametersUtility {

	/**
	 * Validate parameters and return true if the parameters do not conform to the
	 * applications requirement.
	 * 
	 * @param parameters
	 * @return true if parameters are incorrectly specified, otherwise return false.
	 */
	public static boolean invalidParameters(Parameters parameters) {
		return (tooMany(parameters) || mixedTypeOf(parameters) || wrongNameIn(parameters));
	}

	private static boolean tooMany(Parameters parameters) {
		return parameters.getRaw().size() > 2;
	}

	private static boolean mixedTypeOf(Parameters parameters) {
		return parameters.getRaw().size() != parameters.getNamed().size();
	}

	private static boolean wrongNameIn(Parameters parameters) {
		boolean wrongName = false;
		Iterator<String> it = parameters.getNamed().keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (!name.equals(IApplication.NAME) && !name.equals(IApplication.DIR)) {
				wrongName = true;
			}
		}
		return wrongName;
	}

}
