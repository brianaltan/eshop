package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProductAndVerifyInList(ChromeDriver driver) throws Exception {
        //Navigasi Route
        driver.get(baseUrl + "/product/create");

        //Fill Data
        driver.findElement(By.id("nameInput")).sendKeys("Test 101");
        driver.findElement(By.id("quantityInput")).sendKeys("101");

        //Submit
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        //Verify Redirect
        assertTrue(driver.getCurrentUrl().contains(baseUrl + "/product/list"));

        //Verify Product baru sudah ada di halaman sekarang
        String productListBodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(productListBodyText.contains("Test 101"));
        assertTrue(productListBodyText.contains("101"));
    }
}