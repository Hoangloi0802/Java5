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

public class ForgotPasswordTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/forgot-password"); // Đổi URL nếu cần
    }

    @Test
    public void testForgotPasswordSuccess() {
        resetPassword("hoangloi0802@gmail.com", "Vui lòng kiểm tra email để đặt lại mật khẩu.");
    }

    @Test
    public void testForgotPasswordWithNonExistingEmail() {
        resetPassword("hoangloi@gmail.com", "Email không tồn tại trong hệ thống.");
    }

    @Test
    public void testForgotPasswordWithInvalidEmailFormat() {
        resetPassword("hoangloi@gmail", "Email không tồn tại trong hệ thống.");
    }

    private void resetPassword(String email, String expectedMessage) {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));

        // Xóa dữ liệu cũ (nếu có)
        emailField.clear();

        // Nhập email
        emailField.sendKeys(email);
        submitButton.click();

        // Chờ thông báo hiển thị
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'alert')]")
        ));

        // Lấy nội dung thông báo
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
