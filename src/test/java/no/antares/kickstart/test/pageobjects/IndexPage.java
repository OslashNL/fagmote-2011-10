package no.antares.kickstart.test.pageobjects;

import no.antares.kickstart.test.util.SeleniumStarter;

import org.openqa.selenium.By;

public class IndexPage extends PageObject {
    public IndexPage(SeleniumStarter utils) {
        super(utils, "index.html");
        throwIfTitleNot("Demo");
    }

    public String greeting() {
        return findElement(By.id("greeting")).getText();
    }

}
