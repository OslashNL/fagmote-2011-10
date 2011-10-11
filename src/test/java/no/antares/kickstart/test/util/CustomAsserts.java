package no.antares.kickstart.test.util;

import static org.junit.Assert.fail;

public class CustomAsserts {
    public static void assertContains(String expectedContents, String actual) {
        if (!actual.contains(expectedContents))
            fail( "[" + actual + "] does not contain " + expectedContents);
    }

}
