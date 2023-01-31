package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.base.Function;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Driver {

    public Driver() {
    }

    public static WebDriver driver;
    public static ExtentHtmlReporter html;
    public static ExtentReports report;
    public static ExtentTest test;
    public static String runtype;
    public static final String USERNAME = "notificacionesgl1";
    public static final String AUTOMATE_KEY = "pvsqyH7AACsUaciFt2uF";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub.browserstack.com/wd/hub";
    public static Logger log = LogManager.getLogger(Driver.class);
    @BeforeTest(alwaysRun = true)
    public void generateReport() {
        html = new ExtentHtmlReporter(Constants.REPORT_FILEPATH);
        html.config().setTheme(Theme.DARK);
        html.config().setDocumentTitle("Orange HRM Testing Report");
        html.config().setReportName("HRM Testing Execution Report");

        report = new ExtentReports();
        report.attachReporter(html);

            report.setSystemInfo("QA Engineer", Constants.USER_NAME);
            report.setSystemInfo("Environment", Constants.OS_NAME);
            report.setSystemInfo("Browser", ConfigReader.getProperty("browser"));

    }

    @AfterTest(alwaysRun = true)
    public void writeReport() {
        //report.flush();
        closeDriver();
    }

    @BeforeMethod(alwaysRun = true)

    public static WebDriver getDriver() {
        try {
            runtype = ConfigReader.getProperty("runtype");
            if (runtype.equalsIgnoreCase("local")) {
                if (driver == null) {
                    switch (ConfigReader.getProperty("browser")) {
                        case "chrome":
                            WebDriverManager.chromedriver().setup();
                            driver = new ChromeDriver();
                            break;
                        case "firefox":
                            WebDriverManager.firefoxdriver().setup();
                            driver = new FirefoxDriver();
                            break;
                        case "ie":
                            WebDriverManager.iedriver().setup();
                            driver = new InternetExplorerDriver();
                            break;
                        case "safari":
                            WebDriverManager.getInstance(SafariDriver.class).setup();
                            driver = new SafariDriver();
                            break;
                        case "headless-chrome":
                            WebDriverManager.chromedriver().setup();
                            driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
                            break;
                    }

                }
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driver.manage().window().maximize();
            }

            else if (runtype.equalsIgnoreCase("remote")){
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setPlatform(Platform.MAC);
                caps.setCapability("browserName", "chrome");
                caps.setCapability("device", "iPhone 11");
                caps.setCapability("realMobile", "true");
                caps.setCapability("os_version", "14.0");
                caps.setCapability("name", "BStack-[Java] Caro Cuore DeepTest Validation");
                java.net.URL browserStackUrl = new URL(URL);
                driver = new RemoteWebDriver(browserStackUrl, caps);
                //Implicit wait for 30 seconds
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
        }catch (Exception e ){
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());
        }




        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * This method will take screenshot and save it with the given file name
     *
     * @param fileName
     */
    public static String takeScreenShot(String fileName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screen = ts.getScreenshotAs(OutputType.FILE);
        String scrshotFile = Constants.SCREENSHOTS_FILEPATH + fileName + "_" + getTime() + ".png";
        try {
            FileUtils.copyFile(screen, new File(scrshotFile));
        } catch (IOException e) {
            System.out.println("Cannot take screenshot");
        }
        return scrshotFile;
    }

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String formattedDate = sdf.format(date.getTime());
        return formattedDate;
    }

    public static void wait(int secs) {
        try {
            Thread.sleep(1000 * secs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * switches to new window by the exact title returns to original window if
     * windows with given title not found
     */
    public static void switchToWindow(String targetTitle) throws Exception {

        String origin = getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            getDriver().switchTo().window(handle);
            if (getDriver().getTitle().equals(targetTitle)) {
                return;
            }
        }
        getDriver().switchTo().window(origin);
    }

    public static void hover(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public void visit(String url) {
        getDriver().get(url);

    }

    /**
     * return a list of string from a list of elements ignores any element with no
     * text
     *
     * @param list
     * @return
     */
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            elemTexts.add(el.getText());
        }
        return elemTexts;
    }

    public static List<String> getElementsText(By locator) throws Exception {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            elemTexts.add(el.getAttribute("textContent"));
        }
        System.out.println(elemTexts);
        return elemTexts;
    }

    public static List<String> getElements(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            elemTexts.add(el.getText());
        }
        System.out.println(elemTexts);
        return elemTexts;
    }

    public static WebElement waitForVisibility(WebElement element, int timeToWaitInSec) throws Exception {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeToWaitInSec));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForVisibility(By locator, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static Boolean waitForInVisibility(By locator, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickablility(WebElement element, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickablility(By locator, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSeconds));
            wait.until(expectation);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static WebElement fluentWait(final WebElement webElement, int timeinsec) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(timeinsec)).pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return webElement;
            }
        });
        return element;
    }

    /**
     * Verifies whether the element matching the provided locator is displayed on
     * page fails if the element matching the provided locator is not found or not
     * displayed
     *
     * @param by
     */
    public static void verifyElementDisplayed(By by) {
        try {
            assertTrue("Element not visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + by);
        }
    }

    /**
     * Verifies whether the element matching the provided locator is NOT displayed
     * on page fails if the element matching the provided locator is not found or
     * not displayed
     *
     * @param by
     */
    public static void verifyElementNotDisplayed(By by) {
        try {
            assertFalse("Element should not be visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies whether the element is displayed on page fails if the element is not
     * found or not displayed
     *
     * @param element
     */
    public static void verifyElementDisplayed(WebElement element) {
        try {
            assertTrue("Element not visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + element);
        }
    }

    /**
     * Waits for element to be not stale
     *
     * @param element
     */
    public static void waitForStaleElement(WebElement element) {
        int y = 0;
        while (y <= 15) {
            if (y == 1)
                try {
                    element.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    /**
     * Selects a random value from a dropdown list and returns the selected Web
     * Element
     *
     * @param select
     * @return
     */
    public static WebElement selectRandomTextFromDropdown(Select select) {
        Random random = new Random();
        List<WebElement> weblist = select.getOptions();
        int optionIndex = 1 + random.nextInt(weblist.size() - 1);
        select.selectByIndex(optionIndex);
        return select.getFirstSelectedOption();
    }

    /**
     * Clicks on an element using JavaScript
     *
     * @param element
     */
    public static void clickWithJS(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", element);
    }

    /**
     * Scrolls down to an element using JavaScript
     *
     * @param element
     */
    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Performs double click action on an element
     *
     * @param element
     */
    public static void doubleClick(WebElement element) {
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    /**
     * Changes the HTML attribute of a Web Element to the given value using
     * JavaScript
     *
     * @param element
     * @param attributeName
     * @param attributeValue
     */
    public static void setAttribute(WebElement element, String attributeName, String attributeValue) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2]);", element, attributeName, attributeValue);
    }

    /**
     * @param element
     * @param check
     */
    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                wait(1);
            }
        }
    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param element
     */
    public static void executeJScommand(WebElement element, String command) {
        JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
        jse.executeScript(command, element);
    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param command
     */
    public static void executeJScommand(String command) {
        JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
        jse.executeScript(command);
    }

    public static boolean isElementSelected(By locator) {
        return webAction(locator).isSelected();
    }

    public static void sendValue(By locator, String value) {
        try {
            webAction(locator).sendKeys(value);
        } catch (Exception e) {
            System.out.println("Some exception occured while sending value" + locator);
        }

    }

    public static WebElement webAction(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class).ignoring(ElementClickInterceptedException.class)
                .withMessage(
                        "Webdriver waited for 15 seconds nut still could not find the element therefore TimeOutException has been thrown");
        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
    }

    // Simple Click Method
    public void click(By locator) {
        driver.findElement(locator).click();
    }

    public void type(By locator, String value) {
        driver.findElement(locator).sendKeys(value);
    }

    public static void waitAndSendText(WebElement element, String text, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.sendKeys(text);
                return;
            } catch (WebDriverException e) {
                wait(1);
            }
        }
    }

    public static String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = System.getProperty("user.dir") + "\\reports\\" + testCaseName + ".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;

    }

    public String splitUrl(String url, String divider, int sliceIndex) {
        String[] parts = url.split(divider);
        return parts[sliceIndex];
    }

    public void mouseHoverElementAndClickInternalElement(By elementForMouseHover, By internalElementForClick) {
        Actions action = new Actions(driver);
        WebElement mainMenu = driver.findElement(elementForMouseHover);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        action.moveToElement(mainMenu).moveToElement(driver.findElement(internalElementForClick)).click().build().perform();
    }

    public void waitForElementDisplayed(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(locator)));
    }

    public static void getAllUrls() {
        try {
            List<WebElement> list = driver.findElements(By.xpath("//*[@href or @src]"));

            for (WebElement e : list) {
                String link = e.getAttribute("href");
                if (null == link)
                    link = e.getAttribute("src");

                System.out.println(e.getTagName() + "=" + link);

            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
