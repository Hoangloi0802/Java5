package ass.ass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

@SpringBootTest
public class RegisterTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @Test(dataProvider = "registerData")
    public void testRegister(String fullName, String username, String email, String password, String confirmPassword, String expectedMessage) {
        driver.get("http://localhost:8080/dangky");

        WebElement fullNameField = driver.findElement(By.id("inputFullname"));
        WebElement usernameField = driver.findElement(By.id("inputUsername"));
        WebElement emailField = driver.findElement(By.id("inputEmail"));
        WebElement passwordField = driver.findElement(By.id("inputPassword"));
        WebElement confirmPasswordField = driver.findElement(By.id("inputConfirmPassword"));
        WebElement registerButton = driver.findElement(By.xpath("//button[@type='submit']"));

        fullNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();

        fullNameField.sendKeys(fullName);
        usernameField.sendKeys(username);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        confirmPasswordField.sendKeys(confirmPassword);
        registerButton.click();

        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("p")));
        String actualMessage = messageElement.getText();

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @DataProvider(name = "registerData")
    public Object[][] getRegisterData() {
        return new Object[][]{
            {"Thái Hoàng Lợi", "hoangloi123", "hoangloi@gmail.com", "123231", "123231", "Tạo tài khoản thành công"},
            {"Thái Test", "hoangloi", "hoangloi0802@gmail.com", "123123", "123123", "Tên đăng nhập đã tồn tại"},
            {"Thái Demo mot", "demo123", "ThaiDemo@gmail.com", "123231", "123231", "Vui lòng kiểm tra email để kích hoạt tài khoản."},
            {"", "demoTest", "test@gmail.com", "", "", "Vui lòng nhập đầy đủ thông tin"},
        };
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
