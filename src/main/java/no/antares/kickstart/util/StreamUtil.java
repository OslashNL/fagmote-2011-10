package no.antares.kickstart.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 *
 * @author Tommy Skodje
 */
public class StreamUtil {

    /**	Closes the Writer	*/
    public static void flush(Writer s) {
        if (s != null)
            try {
                s.flush();
            } catch (Exception e) {
            }
    } // close()

    /**	Closes the Writer	*/
    public static void close(Reader s) {
        if (s != null)
            try {
                s.close();
            } catch (Exception e) {
            }
    } // close()

    /**	Closes the stream	*/
    public static void close(InputStream s) {
        if (s != null)
            try {
                s.close();
            } catch (Exception e) {
            }
    } // close()

    /**	Closes the stream	*/
    public static void close(OutputStream s) {
        if (s != null)
            try {
                s.close();
            } catch (Exception e) {
            }
    } // close()

    /**	Closes the stream	*/
    public static void close(Writer s) {
        if (s != null)
            try {
                s.close();
            } catch (Exception e) {
            }
    } // close()

    /** Read from an input stream and stuff into the output stream
     * @param in InputStream to read from
     * @param out OutputStream to write to
     */
    public static void streamData(InputStream in, OutputStream out) throws IOException {
        if ((in != null) && (out != null)) {
            byte[] buf = new byte[4 * 1024]; // 4K buffer
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        }
    } // streamData()

    /** Read from an input stream and return as String
     * @param in InputStream to read from
     */
    public static String toString(InputStream in) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        streamData(in, output);
        StreamUtil.close(output);
        return output.toString();
    } // toString()

}
