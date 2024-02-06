
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import java.util.List;
import java.util.ArrayList;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Scraper {
    public static WebDriver driver = new FirefoxDriver();
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a city and state in this format: City ST: ");
        String location = scanner.nextLine();
        location = location.replace(" ", "_");

        String url = "https://www.realtor.com/realestateandhomes-search/" + location;

        // Set path to driver exec.
        System.setProperty("webdriver.firefox.driver", "/Users/farisallaf/Downloads/geckodriver");


        try {
            driver.get(url);
            JavascriptExecutor js = (JavascriptExecutor) driver;

            List<String[]> list = new ArrayList<>();
            String[] header = {"id", "price", "address", "description"};
            list.add(header);
            int number = 1;

            Actions actions = new Actions(driver);

            WebElement button = driver.findElement(By.cssSelector("[aria-label='Go to next page']"));

            // Scrape data
            scrapeData(driver, js, list, number, button);

            // Write to CSV
            writeToCSV(list);

        } catch (NoSuchElementException e) {
            System.out.println("data-testid not found; Please change it and try again :)");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }

    private static void scrapeData(WebDriver driver, JavascriptExecutor js, List<String[]> list, int number, WebElement button) throws InterruptedException {
        int housesCount = 0;
        List<WebElement> prices, address, meta;

        try {
            while (button.getAttribute("disabled") == null) {
                Thread.sleep(2000);
                scrollPage(js, button);

                // Find elements
                prices = driver.findElements(By.cssSelector("[data-testid='card-price']"));
                address = driver.findElements(By.cssSelector("[data-testid='card-address']"));
                meta = driver.findElements(By.cssSelector("[data-testid='card-meta']"));
                housesCount = prices.size();

                // Process data
                for (int j = 0; j < housesCount; j++) {
                    String Meta = meta.get(j).getText();
                    String singleLineMeta = Meta.replace("\n", " ");
                    int index = singleLineMeta.indexOf("sqft");
                    String newMeta = singleLineMeta.substring(0, index + 4);
                    String[] record = {String.valueOf(number), prices.get(j).getText(), address.get(j).getText(), newMeta};
                    list.add(record);
                    number++;
                }
                button.click();
            }
        } catch (StaleElementReferenceException e) {
            // Handle stale element exception
            handleStaleElementException(driver, js, list, number);
        }
    }

    private static void scrollPage(JavascriptExecutor js, WebElement button) {

        int targetLoc = button.getLocation().getY() - 100;
        int currentLoc = 0;

        while (currentLoc < targetLoc) {
            currentLoc += 100; // Adjust this value to control the scroll step size
            js.executeScript("window.scrollTo(0, " + currentLoc + ");");
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100)");
    }

    private static void handleStaleElementException(WebDriver driver, JavascriptExecutor js, List<String[]> list, int number) throws InterruptedException {
        Thread.sleep(2000);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Long scrollHeight = (Long) js.executeScript("return document.documentElement.scrollHeight;");
        int currentLoc = 0;

        while (currentLoc < scrollHeight) {
            currentLoc += 75; // Adjust this value to control the scroll step size
            js.executeScript("window.scrollTo(0, " + currentLoc + ");");
        }
        //properties to be looked for
        Thread.sleep(2000);
        List<WebElement> prices = driver.findElements(By.cssSelector("[data-testid='card-price']"));
        List<WebElement> address = driver.findElements(By.cssSelector("[data-testid='card-address']"));
        List<WebElement> meta = driver.findElements(By.cssSelector("[data-testid='card-meta']"));
        int housesCount = prices.size();
        // scroll to button

        for (int j = 0; j < housesCount; j++) {
            String Meta = meta.get(j).getText();
            String singleLineMeta = Meta.replace("\n", " ");
            int index = singleLineMeta.indexOf("sqft");
            String newMeta = singleLineMeta.substring(0, index + 4);
            String[] record = {String.valueOf(number), prices.get(j).getText(), address.get(j).getText(), newMeta};
            list.add(record);
            number++;
        }
    }

    private static void writeToCSV(List<String[]> list) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter("c:\\test\\test.csv"))) {
            writer.writeAll(list);
        }
    }
}
