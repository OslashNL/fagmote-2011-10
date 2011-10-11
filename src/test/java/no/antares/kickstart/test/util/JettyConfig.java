package no.antares.kickstart.test.util;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/** Start Jetty embedded, publishing from with src/main/webapp for speed.
 * Adapted from http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 * @author Tommy Skodje
 */
public class JettyConfig {
    public static Server getJettyServer(TstProperties setup) {
        Server server = new Server( setup.testBaseURL.getPort() );

        WebAppContext context = new WebAppContext();
        String webAppDir = setup.webAppDir;
        context.setDescriptor(webAppDir + "/WEB-INF/web.xml");
        context.setResourceBase(webAppDir);

        context.setContextPath( setup.contextPath() );
        context.setParentLoaderPriority(true);

        server.setHandler(context);
        server.setStopAtShutdown(true);
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = getJettyServer(TstProperties.getITestProperties());
        server.start();
        server.join();
    }
}
