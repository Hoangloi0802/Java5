package ass.ass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class UpdateUserInfoTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        login();
    }

    // 📌 Đăng nhập trước khi chạy test
    private void login() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

        usernameField.sendKeys("hoangloi");
        passwordField.sendKeys("hoangloi8");
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/"));
    }

    // 📌 Truy cập trang cập nhật thông tin trước mỗi test
    @BeforeMethod
    public void navigateToUpdateInfo() {
        driver.get("http://localhost:8080/profile");
    }


    @Test
    public void testUpdateWithInvalidEmail() {
        updateInfo("Thái Hoàng Lợi", "hoangloi@gmail", "Email không hợp lệ!");
    }

    @Test
    public void testUpdateWithValidInfo() {
        updateInfo("Thái Hoàng Lợi", "hoangloi@gmail.com", "Cập nhật thành công!");
    }

    @Test
    public void testUpdateWithMissingFields() {
        updateInfo("", "hoangloi@gmail.com", "Vui lòng nhập đầy đủ thông tin");
    }



    private void updateInfo(String fullName, String email, String expectedMessage) {
        WebElement nameField = driver.findElement(By.name("fullname"));
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@id='Capnhat']"));

        nameField.clear();
        emailField.clear();
        nameField.sendKeys(fullName);
        emailField.sendKeys(email);
        submitButton.click();

        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@style='color:red;' or @style='color:green;']")
        ));

        String actualMessage = messageElement.getText();
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
