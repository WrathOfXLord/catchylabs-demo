package com.catchylabs.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.catchylabs.model.ElementInfoo;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.util.stream.Stream;

public abstract class BaseTest {
    private final String defaultElementsDirectory = System.getProperty("user.dir") + "/src/test/resources/elementValues";
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected JavascriptExecutor jsExecutor;
    protected Actions actions;
    DesiredCapabilities capabilities;
    ChromeOptions chromeOptions;
    FirefoxOptions firefoxOptions;

    protected final String browserName = "chrome";
    protected final String selectPlatform = System.getProperty("os.name").toLowerCase();

    ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

    public void setUp() throws IOException {
        initMap(getFileList(defaultElementsDirectory));
        logger.info("************************************  BeforeScenario  ************************************");
        if (StringUtils.isEmpty(System.getenv("key"))) {
            logger.info(String.format("Lokal cihazda %s ortaminda %s browserinda test ayaga kalkacak",
                    selectPlatform, browserName));
            if (selectPlatform.contains("win")) {
                if ("chrome".equalsIgnoreCase(browserName)) {
                    driver = new ChromeDriver(chromeOptions(selectPlatform));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                } else if ("firefox".equalsIgnoreCase(browserName)) {
                    driver = new FirefoxDriver(firefoxOptions(selectPlatform));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                }
            } else if (selectPlatform.contains("mac")) {
                if ("chrome".equalsIgnoreCase(browserName)) {
                    driver = new ChromeDriver(chromeOptions(selectPlatform));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                } else if ("firefox".equalsIgnoreCase(browserName)) {
                    driver = new FirefoxDriver(firefoxOptions(selectPlatform));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                }
            }
            driver.manage().window().maximize();
        } 
        actions = new Actions(driver);
        jsExecutor = (JavascriptExecutor) driver;
    }

    public void tearDown() {
        driver.quit();
        logger.info("Driver kapatildi");
    }

    public void initMap(List<File> fileList) {
        elementMapList = new ConcurrentHashMap<>();
        Type elementType = new TypeToken<List<ElementInfoo>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfoo> elementInfoList = null;
        for (File file : fileList) {
            try {
                elementInfoList = gson
                        .fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream()
                        .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
                logger.info(String.format("%s Dosyasinda %d Adet element degeri bulundu.", file.getName(), elementInfoList.size()));
            } catch (FileNotFoundException e) {
                // ignore
            }
        }
    }

    public static List<File> getFileList(String directoryName) throws IOException {
        List<File> dirList = new ArrayList<>();
        try (Stream<Path> walkStream = Files.walk(Paths.get(directoryName))) {
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                if (f.toString().endsWith(".json")) {
                    dirList.add(f.toFile());
                }
            });
        }
        return dirList;
    }

    /**
     * Set Chrome options
     *
     * @return the chrome options
     */
    public ChromeOptions chromeOptions(String platformName) {
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-notifications");
    
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);
    
        if (platformName.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "./web_driver/chromedriver.exe");
        } else if (platformName.contains("mac")) {
            System.setProperty("webdriver.chrome.driver", "./web_driver/chromedriver");
        } else if (platformName.contains("linux")) {
            System.setProperty("webdriver.chrome.driver", "./web_driver/chromedriver_linux");
        } else {
            throw new IllegalArgumentException("Unsupported Operating System !");
        }

        return chromeOptions;
    }

    /**
     * Set Firefox options
     *
     * @return the firefox options
     */
    public FirefoxOptions firefoxOptions(String platformName) {
    firefoxOptions = new FirefoxOptions();

    Map<String, Object> prefs = new HashMap<>();
    prefs.put("profile.default_content_setting_values.notifications", 2);
    
    FirefoxProfile profile = new FirefoxProfile();
    firefoxOptions.setProfile(profile);

    firefoxOptions.addArguments("--kiosk");
    firefoxOptions.addArguments("--disable-notifications");
    firefoxOptions.addArguments("--start-fullscreen");

    firefoxOptions.setCapability("marionette", true);

    if (platformName.contains("win")) {
        System.setProperty("webdriver.gecko.driver", "./web_driver/geckodriver.exe");
    } else if (platformName.contains("mac")) {
        System.setProperty("webdriver.gecko.driver", "./web_driver/geckodriver");
    } else if (platformName.contains("linux")) {
        System.setProperty("webdriver.gecko.driver", "./web_driver/geckodriver_linux");
    } else {
        throw new IllegalArgumentException("Unsupported Operating System !");
    }

    return firefoxOptions;
}

    public ElementInfoo findElementInfoByKey(String key) {
        return (ElementInfoo) elementMapList.get(key);
    }

    public void saveValue(String key, String value) {
        elementMapList.put(key, value);
    }

    public String getValue(String key) {
        return elementMapList.get(key).toString();
    }
}
