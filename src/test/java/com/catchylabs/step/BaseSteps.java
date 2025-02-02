package com.catchylabs.step;

import com.catchylabs.base.BaseTest;
import com.catchylabs.model.ElementInfoo;
import com.catchylabs.model.CallbackHelper;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Step;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import org.openqa.selenium.support.ui.*;

import java.io.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Fail.fail;

public class BaseSteps extends BaseTest {
    static HashMap<String, Object> hashMap = new HashMap<String, Object>();
    static HashMap<String, String> headers = new HashMap<>();
    static HashMap<String, String> params = new HashMap<>();
    private static HashMap<String, String> dataList = new HashMap<>();

    @BeforeScenario
    @Override
    public void setUp() throws IOException {
        super.setUp();
    }

    @AfterScenario
    @Override
    public void tearDown() {
        super.tearDown();
    }

    public void storeData(String key, String value) {
        dataList.put(key, value);

    }

    public String getData(String key) {
        return dataList.get(key);
    }

    public static final int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static final int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        jsExecutor.executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

    public By getElementInfoToBy(ElementInfoo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    @SuppressWarnings("unused")
    private boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    @SuppressWarnings("unused")
    private boolean isDisplayedBy(By by) {
        return driver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return driver.switchTo().alert().getText();
    }


    public String randomString(int stringLength) {
        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }

    @Step("Print page source")
    public void printPageSource() {
        logger.info(getPageSource());
    }

    public void javascriptClicker2(WebElement element) {
        jsExecutor.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    public void javascriptClicker(WebElement element) {
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    /**
     * A static wait method for a specified period of seconds. Logs can be shut off.
     */
    public void waitForSeconds(long duration, boolean logRequested) {
        if (logRequested)
            logger.info(String.format("Waiting for %d seconds...", duration));

        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            logger.error("waitForSeconds method failure!", e);
        }

        if (logRequested)
            logger.info(String.format("Waited for %d seconds.", duration));
    }

    /** waitForSeconds method that logs are enabled by default. */
    @Step({ "Wait <value> seconds",
            "<int> saniye bekle" })
    public void waitForSeconds(long duration) {
        waitForSeconds(duration, true);
    }

    /**
     * A static wait method for a specified period of milliseconds. Logs can be shut
     * off.
     */
    public void waitForMilliseconds(long duration, boolean logRequested) {
        if (logRequested)
            logger.info(String.format("Waiting for %d milliseconds...", duration));

        try {
            TimeUnit.MILLISECONDS.sleep(duration);
        } catch (InterruptedException e) {
            logger.error("waitForMilliseconds method failure!", e);
        }

        if (logRequested)
            logger.info(String.format("Waited for %d milliseconds.", duration));
    }

    /** waitForMilliseconds method that logs are enabled by default. */
    @Step({ "Wait <value> milliseconds",
            "<long> milisaniye bekle" })
    public void waitForMilliseconds(long duration) {
        waitForMilliseconds(duration, true);
    }

    @Step({ "Wait for element then click <key>",
            "Elementi bekle ve sonra tikla <key>" })
    public void checkElementExistsThenClick(String key) {
        getElementWithKeyIfExists(key);
        clickElement(key);
        logger.info(String.format("%s elementine tiklandi.", key));
    }

    @Step({ "Click to element <key>",
            "Elementine tikla <key>" })
    public void clickElement(String key) {
        if (!key.isEmpty()) {
            clickElement(scrollToElementToBeVisible(key));
            logger.info(String.format("%s elementine tiklandi.", key));
        }
    }

    @Step({ "<key> elementin ustunde bekle" })
    public void hover(String key) {
        hoverElement(findElement(key));
    }

    @Step({ "Click to element <key> with focus",
            "<key> elementine focus ile tikla" })
    public void clickElementWithFocus(String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(String.format("%s elementine focus ile tiklandi.", key));
    }

    @Step({ "Check if element <key> exists",
            "Element var mi kontrol et <key>" })
    public void getElementWithKeyIfExists(String key) {
        ElementInfoo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                logger.info(String.format("%s elementi bulundu.", key));
                return;
            }
            loopCount++;
            waitForMilliseconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(String.format("Element: '%s' doesn't exist", key));
    }

    @Step({ "Go to <url> address",
            "<url> adresine git" })
    public void goToUrl(String url) {
        logger.info(String.format("%s adresi goruldu.", url));
        driver.get(url);
        logger.info(String.format("%s adresine gidiliyor.", url));
    }

    @Step("<key> comboboxtan <value> degerini sec")
    public void selectDropdown(String key, String text) {
        WebElement e = findElement(key);
        javascriptClicker(e);
        Select dropdown = new Select(e);
        dropdown.selectByVisibleText(text);
        logger.info(String.format("%s dropdownindan %s degeri secildi", key, text));
    }

    // ----------------------SONRADAN
    // YAZILANLAR-----------------------------------\\

    public void randomPick(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

    // Javascript driverin baslatilmasi
    private JavascriptExecutor getJSExecutor() {
        return jsExecutor;
    }

    // Javascript scriptlerinin calismasi icin gerekli fonksiyon
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    // Belirli bir locasyona sayfanin kaydirilmasi
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    public void sayfasonu() {
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        waitForMilliseconds(500);
    }

    public void sayfabasi() {
        jsExecutor.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
        waitForMilliseconds(500);
    }

    // Belirli bir elementin oldugu locasyona websayfasinin kaydirilmasi
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfoo elementInfo = findElementInfoByKey(key);
        WebElement webElement = driver.findElement(getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }

    @Step({ "<key> alanina kaydir" })
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
        logger.info(String.format("%s elementinin oldugu alana kaydirildi", key));

    }

    @Step({ "Delete all the text in the element <key>",
            "<key> elementindeki tum texti sil" })
    public void deleteText(String key) {
        WebElement element = findElement(key);

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac"))
            element.sendKeys(Keys.COMMAND + "a");
        else
            element.sendKeys(Keys.CONTROL + "a");

        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(String.format("%s <key> elementindeki tum text silindi", key));
    }

    @Step({ "Delete all the text in the element <key> - js",
            "<key> elementindeki tum texti sil - js" })
    public void deleteTextJS(String key) {
        WebElement element = findElement(key);
        jsExecutor.executeScript("arguments[0].setSelectionRange(0, arguments[0].value.length);", element);
        jsExecutor.executeScript("arguments[0].value = '';", element);
        logger.info(String.format("%s <key> elementindeki tum text silindi", key));
    }

    // Zaman bilgisinin alinmasi
    public Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    @Step("<key> elementine javascript ile tikla")
    public void clickToElementWithJavaScript(String key) {
        WebElement element = findElement(key);
        javascriptClicker(element);
        logger.info(String.format("%s elementine javascript ile tiklandi", key));
    }

    // Belirli bir key degerinin oldugu locasyona websayfasinin kaydirilmasi
    public void scrollToElementToBeVisiblest(WebElement webElement) {
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
    }

    @Step("<key> elementine <text> degerini js ile yaz")
    public void writeToKeyWithJavaScript(String key, String text) {
        WebElement element = findElement(key);
        jsExecutor.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));", element, text);
        logger.info(String.format("%s elementine %s degeri js ile yazildi.", key, text));
    }

    @Step({ "Check if element <key> exists then click",
            "Element <key> varsa tikla" })
    public void getElementWithKeyClickIfExists(String key) {

        List<WebElement> liste = findElements(key);
        if (liste.size() > 0) {
            logger.info("Element bulundu");
            findElementWithKey(key).click();
            logger.info("Elemente tiklandi");
        } else
            logger.info("Element bulunamadi");
    }


    @Step({ "Check <key> element is disabled",
            "<key> elementi aktif degil mi kontrol et" })
    public void checkElementIsDisabled(String key) {
        if (findElementWithKey(key).isEnabled()) {
            Assertions.fail(String.format("%s elementi aktif", key));
        }
        logger.info(String.format("%s elementi aktif degil", key));
    }

    @Step({ "Check <key> element is enabled",
            "<key> elementi aktif mi kontrol et" })
    public void checkElementIsEnabled(String key) {
        if (!findElementWithKey(key).isEnabled()) {
            Assertions.fail(String.format("%s elementi aktif degil", key));
        }
        logger.info(String.format("%s elementi aktif", key));
    }

    @Step("Navigate to back")
    public void navigateToBack() {
        driver.navigate().back();
    }

    @Step("Navigate to forward")
    public void navigateToForward() {
        driver.navigate().forward();
    }

    @Step("Navigate to refresh")
    public void navigateToRefresh() {
        driver.navigate().refresh();
    }

    @Step({ "Write value <text> to element <key>",
            "<text> textini <key> elemente yaz" })
    public void ssendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(String.format("%s elementine %s texti yazildi.", key, text));
        }
    }

    @Step("<message> mesajini bastir - dbgInfo: <dbgInfo>")
    public void printInfo(String message, String dbgInfo) {
        boolean printDbgInfo = (dbgInfo.toLowerCase().equals("true")) ? true : false;
        if (!printDbgInfo) {
            return;
        }
        logger.info(String.format("%s%n", message));
    }

    @Step({ "<elementKey> elementinin <attr> degerini hashmape kaydet - crop: <regex>, key: <key>" })
    public void saveElementAttrOrTextIntoHashmap(String elementKey, String attr, String crop, String key) {
        if (attr.equalsIgnoreCase("text"))
            hashMap.put(key, findElement(elementKey).getText());
        else {
            hashMap.put(key, findElement(elementKey).getAttribute(attr));
        }

        if (!crop.isEmpty()) {
            hashMap.put(key, ((String) hashMap.get(elementKey)).replaceAll(crop, ""));
        }

        logger.info(String.format("%s elementinin %s degeri hashmap'e kaydedildi (%s).", elementKey, attr,
                hashMap.get(key)));
    }

    @Step("<key> keyi ile goruntulenen eleman sayisi hashmape kaydet - <hkey>")
    public void saveElementCount(String key, String hkey) {
        hashMap.put(hkey, new String(findElements(key).size() + ""));
        logger.info(String.format("Goruntulenen eleman sayisi (%s) kaydedildi.", hashMap.get(hkey)));
    }

    @Step("<key> keyi ile hashpmapte bulunan degerini sayisal islem yapilabilir hale getir ve double olarak sakla - locale: <locale>")
    public void convertIntoOperableValue(String key, String locale) {
        String value = hashMap.get(key).toString().trim();
        switch (locale.toLowerCase()) {
            case "tr":
                value = value.replaceAll(".", "").replace(",", ".");
                break;
            default:
                value = value.replaceAll(",", "");
        }

        hashMap.put(key, Double.parseDouble(value));
        logger.info(String.format("%s elemani %s keyi ile saklandi.", value, key));
    }

    @Step("Hashmapte <k1> ve <k2> elemanlarinin farkinin <value> oldugu dogrulanir")
    public void verifyDifference(String k1, String k2, double value) {
        double val1 = (double) hashMap.get(k1);
        double val2 = (double) hashMap.get(k2);
        final double MAX_ERROR = 0.01;

        double difference = Math.abs(val1 - val2);
        boolean isDifferent = (Math.abs(value - difference)) > MAX_ERROR;
        if(isDifferent) {
            Assertions.fail(String.format("Hesaba eklenen tutar dogru degil (%.3f - %.3f)!", val1, val2));
        }
        logger.info(String.format("Eklenen para tutari dogru (%.3f - %.3f).", val1, val2));
        
    }

    @Step("<key> elementlerinde <attr> ozelliginin <value> degerini <containment> durumu dogrulanir")
    public void verifyValueNotExistInElementList(String key, String attr, String value, String containment) {
        List<WebElement> valueExistingElementList = null;
        if (attr.equalsIgnoreCase("text")) {
            valueExistingElementList = findElements(key).stream().filter(element -> {
                return element.getText().contains(value);
            }).toList();
        } else {
            valueExistingElementList = findElements(key).stream().filter(element -> {
                return element.getAttribute(attr).contains(value);
            }).toList();
        }

        final int FOUND_SIZE = valueExistingElementList.size();
        switch (containment.toLowerCase()) {
            case "icerme":
                if (FOUND_SIZE <= 0) {
                    Assertions.fail(String.format("%s degeri aratilan elementlerin arasinda bulunmadi !", value));
                }
                logger.info(String.format("Aratilan %s degeri %d elementte bulundu.", value, FOUND_SIZE));
                break;
            case "icermeme":
                if (FOUND_SIZE > 0) {
                    Assertions.fail(String.format("Aratilan %s degeri %d elementte bulundu !", value, FOUND_SIZE));
                }
                logger.info(String.format("%s degeri aratilan elementlerin arasinda bulunmadi.", value));
                break;
            default:
                Assertions.fail(String.format("Hatali icerme durumu parametresi: %s", containment));
        }
    }

    @Step({ "Scroll to the bottom of the page dynamically - max scroll count: <maxIteration> - scroll waiting interval (ms): <milliseconds>",
            "Dinamik olarak sayfa sonuna in - max scroll sayisi: <maxIteration> - scroll bekleme araligi (ms): <milliseconds>" })
    public void scrollToEndOfPage(int maxIterationCount, long duration) {
        int iteration = 0;
        if (maxIterationCount == 0)
            throw new IllegalArgumentException("Max iterasyon 0 girilemez!");
        Object jsHeightObj = executeJS("return document.body.scrollHeight", true);
        int heightOld = -1, heightNew = Integer.parseInt(jsHeightObj.toString());
        do {
            logger.info(String.format("Scroll islemi gerceklestiriliyor, document.body.scrollHeight: %s", heightNew));
            executeJS("window.scrollTo(0, document.body.scrollHeight);", true);
            waitForMilliseconds(duration);
            jsHeightObj = executeJS("return document.body.scrollHeight", true);
            heightOld = heightNew;
            heightNew = Integer.parseInt(jsHeightObj.toString());
            ++iteration;
            if (iteration == maxIterationCount) {
                logger.info("Max scroll sayisina ulasildi!");
                break;
            }
        } while (heightNew > heightOld);
        logger.info("Scroll tamamlandi.");
    }

    @Step("<outer> textindeki <regex> ile isaretli bolumu <inner> texti ile degistir ve bastir")
    public void printFormattedStrings(String outer, String regex, String inner) {
        if (regex.isEmpty()) {
            outer += inner;
        } else {
            outer = outer.replaceAll(regex, inner);
        }
        logger.info(outer);
    }

    public Object executeAndReturnNonStaleElement(String key, CallbackHelper helper) {
        WebElement element = null;

        ElementInfoo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        final int elementStaleCheckCount = 5;
        for (int attempt = 1; true; ++attempt) {
            try {
                element = webDriverWait
                        .until(ExpectedConditions.presenceOfElementLocated(by));
                logger.info(String.format("Verifying that element '%s (%s)' is not stale...", key, by.toString()));
                return helper.execute(element);
            } catch (StaleElementReferenceException ex) {
                if (attempt == elementStaleCheckCount) {
                    throw new StaleElementReferenceException(String.format("Element '%s (%s)' was stale after %d retries!", 
                        key, by.toString(), elementStaleCheckCount));
                }
                logger.warn(String.format("StaleElementReferenceException has been occurred! Retrying (%d/5)...", attempt));
            }
        }
    }

    @Step({ "Verify <key> element or its childs contain <text> text",
            "<key> elementi veya child elementlerinin <text> metnini icerdigini dogrula" })
    public void checkIfElementContainsText(String key, String text) {

        CallbackHelper helperForElement = (WebElement e) -> {
            return e;
        };
        CallbackHelper helperForText = (WebElement e) -> {
            jsExecutor.executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", e);
            return e.getText();
        };

        WebElement element = (WebElement) executeAndReturnNonStaleElement(key, helperForElement);
        String eText = (String) executeAndReturnNonStaleElement(key, helperForText);
        logger.info(String.format("text: %s", eText));
        if (eText.contains(text)) {
            logger.info(String.format("%s elementi '%s' textini iceriyor.", key, text));
            return;
        }

        String xpath = "./*[contains(text(), \"" + text + "\")]";
        try {
            logger.info("Alt elementlerde arama yapiliyor...");
            element.findElement(By.xpath(xpath));
        } catch (StaleElementReferenceException e) {
            logger.warn("Element was stale, retrying...");
            element = (WebElement) executeAndReturnNonStaleElement(key, helperForElement);
            element.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            fail(String.format("%s elementi veya child elementler '%s' textini icermiyor !", key, text));
        }
        logger.info(String.format("'%s' elementinin child elementlerinden biri veya birkaci '%s' textini iceriyor.", key, text));
    }

    @Step({ "Verify <key> element is not visible in <x> seconds at most",
            "<key> elementinin gorunur olmadigini en fazla <x> saniye icinde dogrula" })
    public void verifyInvisibilityOfElement(String key, long seconds) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(infoParam));
        } catch (TimeoutException ignored) {
            Assertions.fail(String.format("%s elementinin gorunur olmadigi dogrulanamadi!", key));
        }
        logger.info(String.format("%s elementinin gorunur olmadigi dogrulandi.", key));
    }

    @Step({ "Verify <key> element is visible in <x> seconds at most",
            "<key> elementinin gorunur oldugunu en fazla <x> saniye icinde dogrula" })
    public void verifyVisibilityOfElement(String key, long seconds) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(ofSeconds(seconds)).pollingEvery(ofMillis(500))
                    .ignoring(NotFoundException.class).ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions.visibilityOfElementLocated(getElementInfoToBy(findElementInfoByKey(key))));
        } catch (TimeoutException ignored) {
            Assertions.fail(String.format("%s elementinin gorunur oldugu dogrulanamadi!", key));
        }
        logger.info(String.format("%s elementinin gorunur oldugu dogrulandi.", key));
    }

    @Step({ "Verify <key> element is visible and clickable in <x> seconds at most",
            "<key> elementinin gorunur ve tiklanabilir oldugunu en fazla <x> saniye icinde dogrula" })
    public void verifyVisibilityAndClickableOfElement(String key, long seconds) {
        try {
            new FluentWait<>(driver)
                .withTimeout(ofSeconds(seconds))
                .pollingEvery(ofMillis(500))
                .ignoring(NotFoundException.class)
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.elementToBeClickable(getElementInfoToBy(findElementInfoByKey(key))));
        } catch (TimeoutException ignored) {
            Assertions.fail(String.format("%s elementinin gorunur oldugu dogrulanamadi!", key));
        }
        logger.info(String.format("%s elementinin gorunur oldugu dogrulandi.", key));
    }

    @Step("<key> elementinin yalnizca sayi icermedigini dogrula")
    public void verifyNotOnlyNumeric(String key) {
        WebElement e = findElement(key);
        String elementText = e.getText();
        try {
            Integer.parseInt(elementText);
            Assertions.fail("Hesap adi yalnizca sayi iceriyor!");
        } catch (Exception ex) {
            logger.info("Hesap adi yalnizca sayi icermiyor, dogrulama basarili.");
        }
    }
}