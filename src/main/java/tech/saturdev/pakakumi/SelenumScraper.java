package tech.saturdev.pakakumi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import tech.saturdev.pakakumi.models.PakakumiEntry;
import tech.saturdev.pakakumi.repository.PakakumiEntryRepository;
import tech.saturdev.pakakumi.service.ScraperStatusService;

@Component
public class SelenumScraper implements ApplicationRunner {

    @Autowired
    private PakakumiEntryRepository repository;

    @Autowired
    private ScraperStatusService scraperStatusService;

    private static final int INITIAL_DELAY_MS = 10000;
    private static final int LOOP_DELAY_MS = 1000;
    private static final int REQUESTS_PER_CACHE_CLEAR = 500;
    // private static final int REQUESTS_PER_BROWSER_RESTART = 5000;
    private static final int CACHE_CLEAR_PER_BROWSER_RESTART = 4;

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    String previousValue = "0";

    @Override
    public void run(ApplicationArguments args) {
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(true);
            WebDriver driver = new FirefoxDriver(options);

            // Navigate to the web page
            driver.get("https://play.pakakumi.com/");

            // Initial delay before starting the loop
            Thread.sleep(INITIAL_DELAY_MS);
            // System.out.println("Reset");
            // Loop to scrape web pages
            while (true) {
                // Scrape data from the web page
                previousValue = recordEntities(driver, previousValue);

                // Increment request count and check if a cache clear is needed
                scraperStatusService.incrementRequestCount();
                // System.out.println("Request : " + scraperStatusService.getRequestCount());
                if (scraperStatusService.getRequestCount() >= REQUESTS_PER_CACHE_CLEAR) {
                    // Clear memory cache and cookies
                    driver.manage().deleteAllCookies();
                    scraperStatusService.incrementCacheClearCount();
                    scraperStatusService.setRequestCount(0);
                }

                // Check if a browser restart is needed
                if (scraperStatusService.getCacheClearCount() >= CACHE_CLEAR_PER_BROWSER_RESTART) {
                    // Close the current Firefox driver
                    driver.quit();

                    // Set up a new Firefox driver with headless mode
                    options = new FirefoxOptions();
                    options.setHeadless(true);
                    driver = new FirefoxDriver(options);

                    // Navigate to the web page
                    driver.get("https://play.pakakumi.com/");

                    // Initial delay before starting the loop
                    Thread.sleep(INITIAL_DELAY_MS);

                    // Reset counters
                    scraperStatusService.setRequestCount(0);
                    scraperStatusService.setCacheClearCount(0);
                    scraperStatusService.incrementBrowserRestartCount();
                }

                // Wait for a short period before continuing the loop
                Thread.sleep(LOOP_DELAY_MS);
            }
        } catch (Exception e) {
            System.out.println(
                    ANSI_RED + "An error occurred while scraping the web page: " + e.getMessage() + ANSI_RESET);
        }
    }

    private String recordEntities(WebDriver driver, String previousValue) {
        String currentValue = "";
        try {
            WebElement tourMultiplier = driver.findElement(By.id("tour_multiplier"));
            String value = tourMultiplier.findElement(By.cssSelector("div:nth-child(1)")).getText();

            List<String> resultList = Arrays.asList(value.split(" "));
            if (resultList.size() == 2) {
                currentValue = resultList.get(1);

                if (!previousValue.equalsIgnoreCase(currentValue)) {
                    PakakumiEntry entry = new PakakumiEntry();
                    entry.setBetTime(LocalDateTime.now());
                    entry.setBetDuration(Duration.ofMinutes(15));
                    entry.setBustedAt(currentValue);
                    entry.setPlaying(1);
                    entry.setOnline(0);
                    repository.save(entry);
                    return currentValue;// 1

                }
                return previousValue;

            }
            return "0";
        } catch (Exception e) {
            System.out.println(ANSI_RED + "An error occurred while scraping the data: " + e.getMessage() + ANSI_RESET);
            return previousValue; // return the previous value if an error occurs
        }
    }
}