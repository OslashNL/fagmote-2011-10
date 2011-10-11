package no.antares.kickstart.util;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author Tommy Skodje
 */
public class FileUtil {

    private FileUtil() {
    }

    /**	Is this a file-based url?.	*/
    public static boolean isFile(URL url) {
        if (url == null)
            return false;
        return url.toString().startsWith("file:/") || url.toString().startsWith("jar:");
    } // isFile()

    /**	Is this a http-based url?.	*/
    public static boolean isHttp(URL url) {
        if (url == null)
            return false;
        return url.toString().startsWith("http://");
    } // isFile()

    /**	Is this a file-based url?.	*/
    public static File getFile(URL url) {
        if (url.toString().startsWith("jar:file:"))
            try {
                url = new URL(url.toString().substring(4));
            } catch (Exception e) {
                // don't care - we'll break soon anyhow
            }
        if (!isFile(url)) {
            return null;
        }

        try {
            return new File(new URI(url.toString()).getPath());
        } catch (Exception e) {
            String msg = "Unbelievable - Exception in getFile( " + url + " )\n";
            throw new RuntimeException(msg);
        }
    } // getFile()

    /**	Is this a file-based url?.	*/
    public static URL getUrl(File f) {
        if (f == null)
            return null;
        try {
            URI u = f.toURI();
            return u.toURL();
        } catch (Exception e) {
            String msg = "Unbelievable - Exception in getUrl( " + f + " )\n";
            throw new RuntimeException(msg, e);
        }
    } // isFile()

    /**	Is this a file-based url?.	*/
    public static URI getUri(File f) {
        if (f == null)
            return null;
        try {
            return f.toURI();
        } catch (Exception e) {
            String msg = "Unbelievable - Exception in getUri( " + f + " )\n";
            throw new RuntimeException(msg, e);
        }
    } // isFile()

    /**	Uses classloader to find a resource. */
    public static URL getResourceUrl(String name) throws FileNotFoundException {
        StringBuilder debug = new StringBuilder("FileUtil.getRezource( " + name + " )\n");
        InputStream rez = null;
        try {
            // debug.append( "System properties: \n" + getSystemProperties() );

            ClassLoader loader = FileUtil.class.getClassLoader();
            java.net.URL u = loader.getResource(name);
            if (u == null)
                u = loader.getResource("/" + name);
            debug.append("\nGot url: ").append(u);

            // check existence by opening it
            rez = u.openStream();
            return u;
        } catch (Throwable e) {
            debug.append("Caught: ").append(e);
            throw new FileNotFoundException(debug.toString());
        } finally {
            StreamUtil.close(rez);
        }
    } // getResourceUrl()

    /**
     * Laster en ressurs fra classpath og returnerer den som en String
     * CR ('\r'), LF ('\n'), eller CRLF ('\r\n') i ressursen blir erstattet med en enkelt LF ('\n').
     * Denne metoden fungerer for alle tekstfiler, men kan ikke benyttes til å laste binære data.
     * @param classpath
     * @return en String som inneholder alt som er lest
     * @throws java.io.IOException
     */
    public static String loadResourceAsString(String classpath) throws IOException {
        Class< ? > cl = FileUtil.class;
        InputStream instr = cl.getResourceAsStream(classpath);
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(instr));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } finally {
            if (in != null) {
                in.close();
            }
            if (instr != null) {
                instr.close();
            }
        }
    }

    /**	Uses classloader to find a resource. */
    public static File getResource(String name) throws FileNotFoundException {
        StringBuilder debug = new StringBuilder("FileUtil.getRezource( " + name + " )\n");
        InputStream rez = null;
        try {
            // debug.append( "System properties: \n" + getSystemProperties() );

            ClassLoader loader = FileUtil.class.getClassLoader();
            java.net.URL u = loader.getResource(name);
            if (u == null)
                u = loader.getResource("/" + name);
            debug.append("\nGot url: ").append(u);

            // check existence by opening it
            rez = u.openStream();
            return getFile(u);
        } catch (Throwable e) {
            debug.append("Caught: ").append(e);
            throw new FileNotFoundException(debug.toString());
        } finally {
            StreamUtil.close(rez);
        }
    } // getResource()

    /**	Uses classloader to find a resource. */
    public static Properties getProperties(String name) throws FileNotFoundException {
        StringBuilder debug = new StringBuilder("FileUtil.getProperties( " + name + " )\n");
        Properties p = new Properties();
        InputStream rez = null;
        try {
            // debug.append( "System properties: \n" + getSystemProperties() );

            ClassLoader loader = FileUtil.class.getClassLoader();
            java.net.URL u = loader.getResource(name);
            debug.append("\nGot url: ").append(u);

            // check existence by opening it
            rez = u.openStream();
            p.load(rez);
        } catch (Throwable e) {
            debug.append("Caught: ").append(e);
            throw new FileNotFoundException(debug.toString());
        } finally {
            StreamUtil.close(rez);
        }
        return p;
    } // getProperties()

    /* Stream data from url down to specified file */
    public static File writeUrl2File(URL url, File f) {
        OutputStream os = null;
        InputStream is = null;
        try {
            File parent = f.getParentFile();
            if (!parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent.getAbsolutePath());
                }
            }

            if (!f.createNewFile()) {
                throw new IOException("Failed to create file " + f.getAbsolutePath());
            }

            os = new FileOutputStream(f);
            is = url.openStream();
            StreamUtil.streamData(is, os);
            return f;
        } catch (Throwable t) {
            throw new RuntimeException("Error in writeUrl(" + url + " , " + f + ")", t);
        } finally {
            StreamUtil.close(is);
            StreamUtil.close(os);
        }
    }

}
