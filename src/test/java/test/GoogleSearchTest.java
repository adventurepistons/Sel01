package test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class GoogleSearchTest {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @DataProvider(name = "searchData")
    public Object[][] getData() {
        return new Object[][]{
            {"accenture", "www.accenture.com"},
            {"infosys", "www.infosys.com"},
            {"tcs", "www.tcs.com"} // assuming correction for 'eee.tcs.com' to 'www.tcs.com'
        };
    }

    @Test(dataProvider = "searchData")
    public void verifyCompanyInGoogleResults(String searchTerm, String expectedDomain) {
        driver.get("https://www.google.com");

        // Accept cookies if required (for EU-based Google versions)
        try {
            WebElement agreeBtn = driver.findElement(By.xpath("//button[contains(., 'Accept')]"));
            agreeBtn.click();
        } catch (Exception ignored) {}

        // Search input
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys(searchTerm);
        searchBox.submit();

        // Wait for results
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Check presence of expected domain
        List<WebElement> results = driver.findElements(By.xpath("//a[@href]"));
        boolean domainFound = results.stream().anyMatch(link -> link.getAttribute("href").contains(expectedDomain));

        Assert.assertTrue(domainFound, "Expected domain not found: " + expectedDomain);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
