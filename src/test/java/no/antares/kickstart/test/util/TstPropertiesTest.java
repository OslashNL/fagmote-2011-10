package no.antares.kickstart.test.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TstPropertiesTest {

    @Test
    public void testGetITestProperties() {
        TstProperties sut   = TstProperties.getITestProperties();
        assertNotNull( sut.baseDir );
    }

}
