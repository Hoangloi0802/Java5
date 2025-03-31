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

public class ChangePasswordTest {
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

    private void login() {
        driver.get("http://localhost:8080/login");
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        
        usernameField.sendKeys("hoangloi");
        passwordField.sendKeys("123");
        loginButton.click();
        
        wait.until(ExpectedConditions.urlContains("/")); 
    }

    @BeforeMethod
    public void navigateToChangePasswordPage() {
        driver.get("http://localhost:8080/doimk");
    }

    @Test
    public void testChangePasswordSuccess() {
        changePassword("123", "hoangloi8", "hoangloi8", "Đổi mật khẩu thành công!");
    }

    @Test
    public void testChangePasswordWrongOldPassword() {
        changePassword("123456", "hoangloi8", "hoangloi8", "Mật khẩu cũ không đúng hoặc mật khẩu mới không hợp lệ!");
    }

    @Test
    public void testChangePasswordMismatchNewPassword() {
        changePassword("123231", "hoangloi8", "hoangloi1", "Mật khẩu cũ không đúng hoặc mật khẩu mới không hợp lệ!");
    }

    @Test
    public void testChangePasswordEmptyNewPassword() {
        changePassword("123231", "", "hoangloi8", "Vui lòng nhập mật khẩu mới");
    }

    @Test
    public void testChangePasswordNewPasswordSameAsOld() {
        changePassword("123231", "123231", "123231", "Mật khẩu mới không được trùng mật khẩu cũ");
    }

    private void changePassword(String oldPassword, String newPassword, String confirmPassword, String expectedMessage) {
        WebElement oldPasswordField = driver.findElement(By.id("currentPassword"));
        WebElement newPasswordField = driver.findElement(By.id("newPassword"));
        WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
        WebElement changePasswordButton = driver.findElement(By.xpath("//button[@type='submit']"));

        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();

        oldPasswordField.sendKeys(oldPassword);
        newPasswordField.sendKeys(newPassword);
        confirmPasswordField.sendKeys(confirmPassword);
        changePasswordButton.click();

        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@style='color:red;'] | //p[@style='color:green;']")));
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