

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestCost {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {

	  DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
	  caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS,true);
	  driver = new InternetExplorerDriver(caps);
	  // driver = new InternetExplorerDriver();
	  // driver = new ChromeDriver();
	//   driver = new FirefoxDriver();
          // baseUrl = "http://www.nskes.ru/";
    
  }
  @Test
  public void testCost() throws Exception {
	
    driver.get("http://www.nskes.ru/");

    WebElement element = driver.findElement(By.xpath("//div[@id='pagewrapper']/div[2]/div[3]/div/ul/li[3]/span/a/span"));
//    WebElement element = driver.findElement(By.xpath("//div[@id='pagewrapper']/div[2]/div[3]/div/ul/li[3]"));
  
    Actions actions = new Actions(driver);
    actions.moveToElement(element).build().perform();

  this.sleep(1);
 
  //driver.findElement(By.linkText("Ценовой калькулятор")).click();
  
    if (driver.findElement(By.linkText("Ценовой калькулятор")).isDisplayed()==true)
    {
    	driver.findElement(By.linkText("Ценовой калькулятор")).click();
    }
    else if (driver.findElement(By.linkText("Ценовой калькулятор")).isDisplayed()==false)
    {
    	this.sleep(2);
        driver.findElement(By.linkText("Ценовой калькулятор")).click();
    }
 
    this.sleep(1);
    new Select(driver.findElement(By.name("year"))).selectByVisibleText("2016");
    this.sleep(1);
    new Select(driver.findElement(By.name("month"))).selectByVisibleText("Июнь");
    new Select(driver.findElement(By.id("napr"))).selectByVisibleText("Низкое напряжение (0.4 КВ и ниже)");
    new Select(driver.findElement(By.id("diap"))).selectByVisibleText("менее 150 кВт");
    driver.findElement(By.name("obemE")).clear();
    this.sleep(1);
    driver.findElement(By.name("obemE")).sendKeys("10");
    this.sleep(1);
    driver.findElement(By.name("submit")).click();
   
  }

  @After
  public void tearDown() throws Exception {
	  this.sleep(1);
	  driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
  public void sleep(int seconds) 
  {
      try {
          Thread.sleep(seconds * 1000);
      } catch (InterruptedException e) {      }
  }
  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
