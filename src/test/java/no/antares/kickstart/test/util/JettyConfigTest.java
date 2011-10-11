package no.antares.kickstart.test.util;

import org.eclipse.jetty.server.Server;
import org.junit.Test;


public class JettyConfigTest {
    @Test
    public void testStart() throws Exception {
        Server server = JettyConfig.getJettyServer(TstProperties.getITestProperties());
        /* not a real test as it does not check for errors - you have to see for yourself
        server.start();
        server.stop();
        */
    }

}
