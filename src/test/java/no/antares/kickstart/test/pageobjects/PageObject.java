package no.antares.kickstart.test.pageobjects;

import java.util.List;

import no.antares.kickstart.test.util.SeleniumStarter;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/** Base PageObject Class
 * @see http://code.google.com/p/selenium/wiki/PageObjects
 * @author Tommy Skodje
 */
public abstract class PageObject {

    protected final WebDriver driver;

    protected final SeleniumStarter seleniumUtil;

    private String currUrl;

    protected PageObject(PageObject po) {
        seleniumUtil = po.seleniumUtil;
        driver = seleniumUtil.driver;
    }

    /**  */
    protected PageObject(SeleniumStarter util, String url) {
        seleniumUtil = util;
        driver = seleniumUtil.driver;
        go2(url);
    }

    protected void throwIfTitleNot(String expectedTitle) throws IllegalStateException {
        if (!expectedTitle.equals(driver.getTitle()))
            throw new IllegalStateException("Expected title: " + expectedTitle + " got: " + driver.getTitle());
    }

    public String pageDebug() {
        return "\n page visited: " + currUrl;
    }

    public WebElement clearAndType(By elementLocator, String text) {
        WebElement element = findElement(elementLocator);
        try {
            element.clear();
            element.sendKeys(text);
        } catch (Throwable t) {
            Assert.fail(String.format("Could not type '%s' into [%s]", text, elementLocator));
        }
        return element;
    }

    /** assert presence of all elements provided */
    public void assertPresent(By... elementLocators) {
        if (elementLocators == null)
            throw new NullPointerException("assertPresent( null )");
        for (By elementLocator : elementLocators)
            findElement(elementLocator);
    }

    /** find element, fail if not present */
    public WebElement findElement(By elementLocator) {
        return findElement(elementLocator, null);
    }

    /** find element, fail if not present */
    public WebElement findElement(By elementLocator, String message) {
        try {
            return driver.findElement(elementLocator);
        } catch (Throwable t) {
            if (message == null)
                message = "Couldn't find an element with locator: " + elementLocator + pageDebug();
            Assert.fail(message);
            return null;
        }
    }

    /**
     * Finds a WebElement on the page within a specified time.
     * @param by Locator for the WebElement
     * @param timeout When to stop
     * 
     * @return The WebElement, or null if it couldn't be found
     */
    protected WebElement tryFindElement(By by, long timeout) {
        WebElement element = null;
        long now = System.currentTimeMillis();
        long end = now + timeout;
        while (element == null && System.currentTimeMillis() < end) {
            try {
                element = driver.findElement(by); // Cannot use this.findElement(), it fails on exceptions
            } catch (NoSuchElementException e) {
                //logger.error(e.getMessage());
                try {
                    Thread.sleep(0x64);
                } catch (InterruptedException ix) {
                    // logger.error(ix.getMessage());
                }
            }
        }

        return element;
    }

    /** find element, return false if not present */
    public boolean isElementPresent(By elementLocator) {
        try {
            return driver.findElement(elementLocator) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    /** Toggles a checkbox element, fails is element not present.
     * @return Whether the toggled element is selected (true) or not (false) after this toggle is complete
     */
    public boolean toggle(By by) {
        WebElement e = findElement(by);
        e.click();
        return e.isSelected();
    }

    /** Selects a value in an 'option' element (dropdown / combo box).
     * @param by How to find the option element
     * @param value The value to select
     * @return The index of the selected value, or -1 if the value wasn't found
     */
    public int select(By by, String value) {
        if (value == null) {
            String msg = "'value' is null. If using JSON test data, make sure FormDoubles etc are formatted correctly. ";
            throw new IllegalArgumentException(msg);
        }

        int index = -1, selected = -1;
        WebElement select = findElement(by);
        if ("select".equals(select.getTagName())) {
            String optValue;
            List<WebElement> options = select.findElements(By.tagName("option"));
            //System.out.println(id + " options: " + options.size());
            for (WebElement option : options) {
                index++;
                optValue = option.getAttribute("value");
                if (value.equals(optValue)) {
                    if (!option.isSelected())
                        option.click();
                    selected = index;
                    break;
                }
            }
        } else
            Assert.fail("Wrong tagName: '" + select.getTagName() + "', expected: 'select'.");
        return selected;
    }

    /** Selects a value in an 'option' element (dropdown / combo box).
     * @param byId The id of the option element
     * @param value The value to select
     */
    public int select(String byId, String value) {
        return select(By.id(byId), value);
    }

    public String logConfig() {
        return seleniumUtil.toString();
    }

    /** Wraps WebDriver get to avoid adding context to urls. */
    protected void go2(String url) {
        seleniumUtil.go2(url);
        currUrl = url;
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

}