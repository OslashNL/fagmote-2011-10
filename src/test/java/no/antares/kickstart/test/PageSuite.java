package no.antares.kickstart.test;

import no.antares.kickstart.test.util.JettyConfig;
import no.antares.kickstart.test.util.SeleniumStarter;
import no.antares.kickstart.test.util.TstProperties;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/** start and stop of server and browsers - common for all page tests - should be run only once.
 * @author Tommy Skodje / �ystein L�vaas
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    IndexPageTest.class
})
public class PageSuite {

    private static final TstProperties setup = TstProperties.getITestProperties();
    private static final Server server = JettyConfig.getJettyServer(setup);

    @BeforeClass
    public static void setUpForTest() throws Exception {
        loadDataBase();
        server.start();
        // don't: server.join();
        SeleniumStarter.startBrowsers();    // not needed but I prefer to be explicit
    }

    @AfterClass
    public static void tearDownAfterTest() throws Exception {
        SeleniumStarter.stopBrowsers();
        server.stop();
        server.destroy();
        server.join();
    }

    /**  */
    private static void loadDataBase() throws Exception {
        /*
import no.antares.dbunit.DbProperties;
import no.antares.dbunit.DbWrapper;
import no.antares.dbunit.JsonDataSet;
import no.antares.dbunit.converters.CamelNameConverter;
import no.antares.dbunit.converters.DefaultNameConverter;
        try {
            DbProperties props = new DbProperties("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@db3:1521:utv", "sdb", "sdb", "SDB");
            DbWrapper db = new DbWrapper(props);
            DefaultNameConverter converter = new DefaultNameConverter();
            refreshJsonDataSet("database/ApplicationsInDB.json", db, converter);

            CamelNameConverter cnConv = new CamelNameConverter();
            refreshJsonDataSet("database/Products.json", db, cnConv);
            refreshJsonDataSet("database/Credentials.json", db, cnConv);
            refreshJsonDataSet("database/CustomerCommunication.json", db, cnConv);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        */
    }

    /*
    public static void refreshJsonDataSet(String file, DbWrapper db, DefaultNameConverter converter) throws Exception, JSONException {
        JsonDataSet jsonSet = getJsonDataSet(file, converter);
        if (jsonSet != null) {
            db.refreshWithFlatJSON(jsonSet);
        } else {
            System.out.println("No dataset to refresh.");
        }
    }

    private static JsonDataSet getJsonDataSet(String name, DefaultNameConverter converter) throws Exception {
        //System.out.println( name );
        File f = FileUtil.getResource(name);
        if (f == null)
            System.out.println("File not found: " + name);
        else
            return new JsonDataSet(f, converter);
        return null;
    }
    */

}
