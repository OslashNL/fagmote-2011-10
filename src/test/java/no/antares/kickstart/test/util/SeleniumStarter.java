package no.antares.kickstart.test.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.opera.core.systems.OperaDriver;
import com.thoughtworks.selenium.Selenium;

/**
 * @author Tommy Skodje / �ystein L�vaas
 */
public class SeleniumStarter {

	public final Selenium							selenium;
	public final WebDriver						driver;
	public final String								configS;
	public final static TstProperties	testResource;
	private final static URL					baseURL;			// http://localhost:8080/ikano.no/

	private enum Driver {
				fireFox, ie, chrome, opera, htmlUnit
	};

	private static final Log											logger					= LogFactory.getLog( SeleniumStarter.class );
	private static ChromeDriverService						chromeDriverService;

	private static final List< SeleniumStarter >	browsersStarted	= new ListWithoutNull();

	static {
		// Get properties for test.
		testResource = TstProperties.getITestProperties();
		baseURL = testResource.testBaseURL;
		String ffPath = testResource.firefoxPath;
		if ( ffPath != null && !ffPath.equals( "" ) )
			System.setProperty( "webdriver.firefox.bin", ffPath );
	}

	private SeleniumStarter(Driver driverEn, WebDriver driverIn) {
		driver = driverIn;
		selenium = new org.openqa.selenium.WebDriverBackedSelenium( driver, baseURL.toExternalForm() );
		configS = String.format( "%s", driverEn.name() );
	}

	public static List< Object[] > getConfigurationsPresent() {
		startBrowsers(); // in case it was not called before
		List< Object[] > configurations = new ArrayList< Object[] >();
		for ( SeleniumStarter configuration : browsersStarted )
			configurations.add( new Object[] { configuration } );
		return configurations;
	}

	public static void startBrowsers() {
		synchronized ( browsersStarted ) {
			if ( browsersStarted.isEmpty() )
				// start all configurations
				for ( String browser : testResource.browsers ) {
					SeleniumStarter utils = getSeleniumForBrowser( browser );
					browsersStarted.add( utils );
				}
		}
	}

	/** Ends all test sessions, killing the browsers, call when all tests done. */
	public static void stopBrowsers() throws Exception {
		// May be called from several tear-down methods, but each browser should be
		// stopped only once
		List< SeleniumStarter > browsers2stop = new ArrayList< SeleniumStarter >();
		synchronized ( browsersStarted ) {
			if ( browsersStarted.isEmpty() )
				return;
			browsers2stop.addAll( browsersStarted );
			browsersStarted.clear();
		}
		for ( SeleniumStarter browser2stop : browsers2stop ) {
			if ( browser2stop != null )
				try {
					browser2stop.selenium.stop();
				} catch ( Throwable t ) {
					// schedule for termination again
					browsersStarted.add( browser2stop );
				}
		}

		if ( chromeDriverService != null && chromeDriverService.isRunning() ) {
			chromeDriverService.stop();
			logger.debug( "chromeDriverService stopped" );
		}
	}

	/**  */
	@Override public String toString() {
		return configS;
	}

	/* Uses background window to snoop data from conversation with test-support.jsp. 
		// use debug window to avoid messing with ongoing test
		private final String testWindowHandle;
		private final String debugWindowHandle;
		debugWindowHandle	= driver.getWindowHandle();
		testWindowHandle	= openNewWindow( "welcome.jsp" );
	public Map<String,String> getConversation() {
		String id	= getConversationId();
		driver.switchTo().window( debugWindowHandle );
		go2( "test-support/snoop-session.jsp?conversationId=" + id );

		Map<String,String> cAttributes	= new HashMap<String,String>();
		List<WebElement> elements	= driver.findElements( By.className( "valz" ) );
		for ( WebElement element: elements ) {
			String name	= element.getAttribute( "id" );
			String value	= element.getText();
			cAttributes.put( name, value );
		}

		driver.switchTo().window( testWindowHandle );
		return cAttributes;
	}

	private String openNewWindow( String urlIn ) {
		String url	= fullUrl2( urlIn );
		selenium.openWindow( url, urlIn );
		try {
			driver.switchTo().alert().dismiss();	// work-around: we get a script when opening new window 
		} catch ( Throwable t ) {}
		selenium.selectWindow( urlIn );
		return driver.getWindowHandle();
	}*/

	/** try to use PageObjects in stead */
	public void go2(String url) {
		driver.get( fullUrl2( url ) );
	}

	/** Wraps WebDriver.get to avoid adding context to urls. */
	public String fullUrl2(String url) {
		return baseURL + url;
	}

	/** Constructor method: switches on desired browser-driver */
	private static SeleniumStarter getSeleniumForBrowser(final String browserDriver) {
		String dbg = "getSeleniumForBrowser, driver = " + browserDriver;
		try {
			if ( "fireFox".equalsIgnoreCase( browserDriver ) ) {
				return new SeleniumStarter( Driver.fireFox, createFireFoxDriver() );
			}
			if ( "ie".equalsIgnoreCase( browserDriver ) ) {
				return new SeleniumStarter( Driver.ie, new InternetExplorerDriver() );
			}
			if ( "chrome".equalsIgnoreCase( browserDriver ) ) {
				return createAndStartChromeDriver();
			}
			if ( "opera".equalsIgnoreCase( browserDriver ) ) {
				return new SeleniumStarter( Driver.opera, new OperaDriver() );
			}
			if ( "htmlUnit".equalsIgnoreCase( browserDriver ) ) {
				BrowserVersion browser = BrowserVersion.getDefault();
				System.out.println( "HtmlUnit's default User Agent: " + browser.getUserAgent() );
				browser.setBrowserLanguage( "no-no" );
				HtmlUnitDriver driver = new HtmlUnitDriver( browser );
				driver.setJavascriptEnabled( true );
				return new SeleniumStarter( Driver.htmlUnit, driver );
			}
		} catch ( Throwable t ) {
			logger.error( dbg, t );
		}
		return null;
	}

	private static WebDriver createFireFoxDriver() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference( "intl.accept_languages", "no,en-us,en" );
		// to find profile keys, look in <Firefox profile>/prefs.js
		return new FirefoxDriver( profile );
	}

	/**
	 * Creates and starts a {@link ChromeDriverService}.
	 * @return A {@link SeleniumStarter} object
	 * @throws IOException If the service couldn't start
	 * @throws IllegalStateException
	 * @see <a href="http://code.google.com/p/selenium/wiki/ChromeDriver">Information about the Chrome Driver</a>
	 */
	private static SeleniumStarter createAndStartChromeDriver() throws IOException, IllegalStateException {
		// Get the paths for Chrome and ChromeDriver server
		String cPath = testResource.chromePath;
		if ( cPath == null ) {
			throw new IllegalStateException( "Missing path to chrome.exe (chromePath) in 'itest.properties'" );
		}
		String cdPath = testResource.chromeDriverPath;
		if ( cdPath == null ) {
			throw new IllegalStateException( "Missing path to chromedriver.exe (chromeDriverPath) in 'itest.properties'. \n"
										+ "Download from http://code.google.com/p/chromium/downloads/list and unzip anywhere." );
		}
		// Start ChromeDriver service
		chromeDriverService = new ChromeDriverService.Builder().usingChromeDriverExecutable( new File( cdPath ) ).usingAnyFreePort().build();
		chromeDriverService.start();
		logger.debug( "chromeDriverService started" );

		// Create Chrome WebDriver
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setJavascriptEnabled( true );
		// caps.setCapability("chrome.binary", cPath);
		// Set Chrome's preffered language.
		caps.setCapability( "chrome.switches", Arrays.asList( "--lang=no" ) );
		return new SeleniumStarter( Driver.chrome, new RemoteWebDriver( chromeDriverService.getUrl(), caps ) );
	}

	private static class ListWithoutNull extends ArrayList< SeleniumStarter > {
		// need to avoid adding null
		private static final long	serialVersionUID	= 1L;

		@Override public boolean add(SeleniumStarter util) {
			if ( util != null )
				return super.add( util );
			return false;
		}
	};

	/** Quick test */
	public static void main(String[] args) throws Exception {
		startBrowsers();
		System.out.println( "HERE" );
		stopBrowsers();
	}

}
