package no.antares.kickstart.test;

import java.util.List;

import no.antares.kickstart.test.pageobjects.IndexPage;
import no.antares.kickstart.test.util.CustomAsserts;
import no.antares.kickstart.test.util.SeleniumStarter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


/** 
 * @author Tommy Skodje
 */
@RunWith(Parameterized.class) public class IndexPageTest {
	private final SeleniumStarter	selenium;

	@Parameters public static List< Object[] > data() {
		return SeleniumStarter.getConfigurationsPresent();
	}

	public IndexPageTest(final SeleniumStarter seleniumIn) {
		selenium = seleniumIn;
	}

	@Test public void testLoadItem() {
		IndexPage page = new IndexPage( selenium );
		CustomAsserts.assertContains( "Hello", page.greeting() );
	}

	/*
	@Test public void testLoadItemWithMock() throws Exception {
		// Will fail because we cannot get at session where bean resides from here
		// I think we need to override Spring listener configured in web.xml to get there
		ItemBean backingBean = SpringContextHolder.getBackingBean( "itemBean" );
		ItemService service = Mockito.mock( ItemService.class );
		backingBean.setItemService( service );
		Item toReturn = StubsForTest.testItem1();
		Mockito.when( service.findById( toReturn.getId() ) ).thenReturn( toReturn );

		ItemPage page = new ItemPage( selenium );
		Item expected	= StubsForTest.testItem1();
		page.loadItem( expected.getId() );
		assertContains( expected.getName(), page.itemName() );
	}*/

}
