package application.definition;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;

/**
 * ApplicationDefinition contains information specific to an application using
 * application.base as a base for logging, configuration and auditing functions.
 * Information such as the application name for configuration details, log file
 * directory and so forth can be obtained from this class.
 * <p>
 * All public methods are final except Level() and version().
 * 
 * @author nevil
 *
 */
public class ApplicationDefinition {
	private static final DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern(BaseConstants.dateFormatForArchiveFileName);
	private String applicationName;

	/**
	 * Create the application specific details.
	 * 
	 * @param applicationName - the name for this application.
	 * @throws IllegalArgumentException if applicationName is null or empty
	 */
	public ApplicationDefinition(String applicationName) {
		if (applicationName == null) {
			throw new IllegalArgumentException("ApplicationDefinition - applicationName is null");
		}
		if (applicationName.trim().isEmpty()) {
			throw new IllegalArgumentException("ApplicationDefinition - applicationName is empty");
		}
		this.applicationName = applicationName;
	}

	public static Optional<String> getFromManifest(String key) throws Exception {
		return getFromManifest(key, ApplicationDefinition.class);
	}

	public static Optional<String> getFromManifest(String key, Class<?> caller) throws Exception {
		String result = null;
		try {
			Enumeration<URL> resEnum = Thread.currentThread().getContextClassLoader()
					.getResources(JarFile.MANIFEST_NAME);
			URL thisURL = null;
			if (resEnum.hasMoreElements()) {
				thisURL = resEnum.nextElement();
				Manifest manifest = new Manifest(thisURL.openStream());
				Attributes attr = manifest.getMainAttributes();
				result = attr.getValue(key);
			}
		} catch (MalformedURLException e) {
			System.out.println(e);
			throw e;
		} catch (IOException e) {
			System.out.println(e);
			throw e;
		}
		return Optional.ofNullable(result);
	}

	/**
	 * This is the default trace level for this application. Override to change the
	 * default setting.
	 * 
	 * @return level the trace level being used.
	 */
	public Level level() {
		return Level.ALL;
	}

	/**
	 * This is the default version for this application. Override to change the
	 * default setting. The default is to use the definition in the manifest file
	 * for the jar - under the key Application-Version.
	 * 
	 * @return version the version for this application.
	 */
	public String version() {
		String result = "";
		try {
			result = getFromManifest("Application-Version").orElse("Could not be determined");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This is the build date for the application. This information is extracted
	 * from the manifest file for the jar - under the key Build-Date.
	 * 
	 * @return the build date for this application.
	 */
	public String buildDate() {
		String result = "";
		try {
			result = getFromManifest("Build-Date").orElse("Could not be determined");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This is the build number for the application. This information is extracted
	 * from the manifest file for the jar - under the key Build-Number.
	 * 
	 * @return the build number
	 */
	public String buildNumber() {
		String result = "";
		try {
			result = getFromManifest("Build-Number").orElse("Could not be determined");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Does the application require a model to be stored in a model file.
	 * 
	 * @return true if there is a model file, false otherwise.
	 */
	public boolean hasModelFile() {
		return true;
	}

	/**
	 * The top colour for all panels used by the application.
	 * 
	 * @return the colour to be used as the top colour.
	 */
	public Optional<Color> topColor() {
		return Optional.ofNullable((Color) null);
	}

	/**
	 * The bottom colour for all panels used by the application.
	 * 
	 * @return the colour to be used as the bottom colour.
	 */
	public Optional<Color> bottomColor() {
		return Optional.ofNullable((Color) null);
	}

	public final String applicationName() {
		return applicationName;
	}

	public final String loggerName() {
		return applicationName;
	}

	public final String loggerDirectory() {
		return loggerDirectoryFile().getAbsolutePath();
	}

	public final String loggerFile() {
		return loggerFileFile().getAbsolutePath();
	}

	public final String auditDirectory() {
		return auditDirectoryFile().getAbsolutePath();
	}

	public final String auditFile() {
		return auditFileFile().getAbsolutePath();
	}

	public final String archiveDirectory() {
		return archiveDirectoryFile().getAbsolutePath();
	}

	public final String archiveFile() {
		return archiveFileFile().getAbsolutePath();
	}

	public final String iniFileDirectory() {
		return iniFileDirectoryFile().getAbsolutePath();
	}

	public final String iniFile() {
		return iniFileFile().getAbsolutePath();
	}

	public final String reportDirectory() {
		return reportFileDirectoryFile().getAbsolutePath();
	}

	public final String reportItemFile() {
		return reportItemFileFile().getAbsolutePath();
	}

	private final File applicationWorkingDirectoryFile() {
		File rootDirectory = ApplicationConfiguration.rootDirectory();
		File applicationDirectory = new File(rootDirectory, applicationName);
		return applicationDirectory;
	}

	private final File loggerDirectoryFile() {
		File logDirectory = new File(applicationWorkingDirectoryFile(), BaseConstants.LOG_DIRECTORY_NAME);
		return logDirectory;
	}

	private final File loggerFileFile() {
		File logFile = new File(loggerDirectoryFile(), applicationName + BaseConstants.LOG_FILE_SUFFIX);
		return logFile;
	}

	private final File auditDirectoryFile() {
		File auditDirectory = new File(applicationWorkingDirectoryFile(), BaseConstants.AUDIT_DIRECTORY_NAME);
		return auditDirectory;
	}

	private final File auditFileFile() {
		File auditFile = new File(auditDirectoryFile(), applicationName + BaseConstants.AUDIT_FILE_SUFFIX);
		return auditFile;
	}

	private final File archiveDirectoryFile() {
		File archiveDirectory = new File(applicationWorkingDirectoryFile(), BaseConstants.ARCHIVE_DIRECTORY_NAME);
		return archiveDirectory;
	}

	private final File archiveFileFile() {
		String archiveFileName = applicationName + BaseConstants.ARCHIVE_FILE_SUFFIX
				+ formatter.format(LocalDateTime.now());
		File archiveFile = new File(archiveDirectoryFile(), archiveFileName);
		return archiveFile;
	}

	private final File iniFileDirectoryFile() {
		File iniFileDirectory = new File(applicationWorkingDirectoryFile(), BaseConstants.INIFILE_DIRECTORY_NAME);
		return iniFileDirectory;
	}

	private final File iniFileFile() {
		File iniFile = new File(iniFileDirectoryFile(), applicationName + BaseConstants.INIFILE_SUFFIX);
		return iniFile;
	}

	private final File reportFileDirectoryFile() {
		File reportFileDirectory = new File(applicationWorkingDirectoryFile(), BaseConstants.REPORT_DIRECTORY_NAME);
		return reportFileDirectory;
	}

	private final File reportItemFileFile() {
		File reportFile = new File(reportFileDirectoryFile(),
				applicationName + BaseConstants.REPORT_FILE_ITEM_SUFFIX + BaseConstants.REPORT_FILE_SUFFIX);
		return reportFile;
	}

}
