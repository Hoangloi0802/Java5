package ass.ass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest
public class LoginTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Đợi tối đa 5s
        driver.manage().window().maximize(); // Phóng to cửa sổ trình duyệt
    }

   @Test(dataProvider = "loginData")
public void testLogin(String username, String password, String expectedMessage) throws IOException {
    driver.get("http://localhost:8080/login");

    WebElement emailField = driver.findElement(By.id("username"));
    WebElement passwordField = driver.findElement(By.id("password"));
    WebElement loginButton = driver.findElement(By.id("login-button"));

    emailField.clear();
    passwordField.clear();
    emailField.sendKeys(username);
    passwordField.sendKeys(password);
    loginButton.click();

    WebElement messageElement = driver.findElement(By.tagName("p"));
    String actualMessage = messageElement.getText();
    

    Assert.assertEquals(actualMessage, expectedMessage);
}
    

@DataProvider(name = "loginData")
public Object[][] getLoginData() {
    return new Object[][]{
        {"hoangloi", "123", "Đăng nhập thành công!"},
        {"demo4", "123", "Tài khoản không tồn tại"},
        {"hoangloi", "12345", "Sai tài khoản hoặc mật khẩu!"},
        {"hoangloi123", "123", "Sai tài khoản hoặc mật khẩu!"},
    };
}

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
