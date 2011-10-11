package no.antares.kickstart.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import no.antares.kickstart.util.FileUtil;
import no.antares.kickstart.util.ResourceReader;

/** Environment resources - most stem from itest properties that gets it from maven (pom or settings).
 * 
 * @author �ystein L�vaas / Tommy Skodje
 */
public class TstProperties {
	public final List< String >	browsers	= new ArrayList< String >();

	public final String					firefoxPath;
	public final String					chromePath;
	public final String					chromeDriverPath;

	public final URL						testBaseURL;

	public final String					baseDir;
	public final String					webAppDir;
	public final String					targetDir;

	public static TstProperties getITestProperties() {
		TstProperties res = new TstProperties( "itest.properties" );
		return res;
	}

	/** Parses resource file (on classpath), throwing RuntimeException if file problem */
	public TstProperties(String resourcefileName) {
		this( readResourceFile( resourcefileName ) );
	}

	/**  */
	public TstProperties(ResourceBundle bundle) {
		String browsers2test = nullIfEmpty( get( bundle, "browsers2test" ) );
		if ( browsers2test != null )
			for ( String browser : browsers2test.split( "," ) )
				browsers.add( browser );

		// Paths to installed browsers
		firefoxPath = nullIfEmpty( get( bundle, "firefoxPath" ) );
		chromePath = nullIfEmpty( get( bundle, "chromePath" ) );
		chromeDriverPath = nullIfEmpty( get( bundle, "chromeDriverPath" ) );

		baseDir = throwIfEmpty( bundle, "baseDir" );
		webAppDir = baseDir + "/src/main/webapp";
		targetDir = baseDir + "/target/fagmote";

		String tmp = throwIfEmpty( bundle, "baseURL" );
		try {
			testBaseURL = new URL( tmp );
		} catch ( MalformedURLException mue ) {
			throw new RuntimeException( "bad url in itest.properties: " + tmp, mue );
		}
	}

	public String contextPath() {
		String path = testBaseURL.getPath();
		if ( path == null )
			return "";
		if ( path.endsWith( "/" ) )
			return path.substring( 0, path.length() - 1 );
		return path;
	}

	private static ResourceBundle readResourceFile(String resourcefileName) {
		try {
			File properties = FileUtil.getResource( resourcefileName );
			File localOverride = getLocalOverride( properties );
			if ( localOverride.exists() )
				properties = localOverride;
			return ResourceReader.readResourceBundle( properties );
		} catch ( FileNotFoundException fnfe ) {
			throw new RuntimeException( "Resource file " + resourcefileName + " not found", fnfe );
		} catch ( IOException ioe ) {
			throw new RuntimeException( "Resource file " + resourcefileName + " not readable", ioe );
		}
	}

	private static File getLocalOverride(File resourceFile) {
		return new File( resourceFile.getParentFile(), "local." + resourceFile.getName() );
	}

	private static String nullIfEmpty(String res) {
		if ( ( res != null ) & !"".equals( res ) )
			return res;
		return null;
	}

	private static String throwIfEmpty(ResourceBundle productBundle, String format, Object... parameters) {
		String res = get( productBundle, format, parameters );
		if ( ( res != null ) & !"".equals( res ) )
			return res;
		throw new MissingResourceException( "Missing property in itest.properties", "TestResource", format );
	}

	/**
	 * Gets a string from a ResourceBundle or one of its parents.
	 * 
	 * @param productBundle The ResourceBundle
	 * @param format A format string for generating the key for the desired string
	 * @param parameters Arguments referenced by the format specifiers in the format string.
	 * If 'praramters' is omitted, 'format' is used directly as the key.
	 * 
	 * @return the string for the given / formatted key
	 */
	private static String get(ResourceBundle productBundle, String format, Object... parameters) {
		if ( ( parameters == null ) || ( parameters.length == 0 ) )
			return productBundle.getString( format );
		else
			return productBundle.getString( String.format( format, parameters ) );
	}

}
