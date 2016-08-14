package selenium2;

import java.util.regex.Pattern;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

// Класс для тестирования Simple Page (простенькой странички)
public class TestSP1 { 
  private WebDriver driver;
  private Logger log  =  Logger.getLogger(RemoteWebDriver.class.getName());

  //Начальная предустановка для теста (метод, который будет вызван до исполнения теста)
  @Before
  public void setUp() throws Exception {
//Здесь перечислены варианты подключения (нужный просто надо раскомментировать)
//так же это можно было сделать в mavenе в pom.xml  в виде профилей, но Я подумал здесь будет нагляднее
//-------Браузер InternetExplore (в моём случае 11 х64) 
    //  driver = new InternetExplorerDriver();
/* так же можно запустить с различными опциями тогда например будет работает более чувствительно	   
         DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
	     caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS,true);
	     driver = new InternetExplorerDriver(caps); */
//-------Браузер Google Chrome
     //  driver = new ChromeDriver();
//-------Браузер Fire Fox 
     //  driver = new FirefoxDriver();
//-------Варианты для удалённого запуска браузера (ничего не мешает использовать  его для локального запуска)
        driver = new RemoteWebDriver(
	    new URL("http://192.168.2.100:4444/wd/hub"),
	    DesiredCapabilities.firefox());
	 //   DesiredCapabilities.internetExplorer());
	 //   DesiredCapabilities.chrome());
// Устанавливаю логирования (с уровнем детализации INFO)
      ((RemoteWebDriver) driver).setLogLevel(Level.INFO);
//Весь вывод сохраняем в файл (можно было бы так же воспользоваться различными фрэймворками например "slf4j" или т.п.) 
       FileHandler fh = new FileHandler("c:\\logTest1.txt");
       fh.setFormatter(new SimpleFormatter());
       log.addHandler(fh);
}
  
//-------Тест для  успешной авторизации 
  @Test
  public void testLogon() throws Exception {
//Добавляем в лог, что Тест авторизации начал выполнение
    log.info("!!!!!!!! TEST START GOOD LOGON !!!!!!!!");
//открываем тестовую страничку
    driver.get("http://192.168.2.100/index.html");
//Находим по имени тега поле для ввода логина и производим его очистку
    driver.findElement(By.name("login")).clear();
//Вводим логин "admin"   
    driver.findElement(By.name("login")).sendKeys("admin");
//Используя язык CSS находим по имени inputa поле для ввода пароля и очищаем его      
    driver.findElement(By.cssSelector("input[name=\"password\"]")).clear();
//Вводим пароль "admin"    
    driver.findElement(By.cssSelector("input[name=\"password\"]")).sendKeys("admin");
//Используя язык запросов xpath находим кнопку входа авторизации и нажимаем по ней 
    driver.findElement(By.xpath("/html/body/div[1]/form/fieldset/input")).click();
//Проверяем результат авторизации     
    try{
//проверяем наличие элемента на страницы Logon_ok 
	  driver.findElement(By.xpath("//div[@id='logon_ok']"));
//Если элемент не найден, то добавляем информацию об ошибке в лог и завершаем тест как неудачный
    }catch(NoSuchElementException e){ 
                                    log.info("!!!!!!!! TEST FINISH BAD LOGON !!! " 
                                    + e.toString() + " !!!!!!!!");
//Небольшая задержка, что бы успеть увидеть в момент тестирования результат в консоли 	  
	                                Thread.sleep(1000);
	                                fail();
	                                return;
	   }
//Если элемент нашелся то заносим в лог информацию об удачном завершении
    log.info("!!!!!!!! TEST FINISH GOOD LOGON !!!!!!!!");
    Thread.sleep(1000);
     }
//---------Тест элементов управления 
  @Test
  public void testElement() throws Exception {
    log.info("!!!!!!!! TEST START ELEMENTS  !!!!!!!!");	
//открываем тестовую страничку
	driver.get("http://192.168.2.100/index.html");
// Тестируем переключатели (радиокнопки), по очереди перебираем каждую
    driver.findElement(By.name("group1")).click();
    driver.findElement(By.xpath(".//*[@id='elements']/form/fieldset/input[2]")).click();
    driver.findElement(By.cssSelector("input[value=\"r3\"]")).click();
// Тестируем Сheckbox
    driver.findElement(By.id("c1")).click();
    driver.findElement(By.cssSelector("#c7")).click();
    driver.findElement(By.xpath("//div[@id='elements']/form/fieldset/label[3]/input")).click();
    driver.findElement(By.xpath("//input[@id='c777']")).click();
    driver.findElement(By.xpath("//label[5]/input")).click();
// Раскрывающейся список
    new Select(driver.findElement(By.name("List"))).selectByVisibleText("Очень понравилось!");
    new Select(driver.findElement(By.xpath("//select[@name='List']"))).selectByVisibleText("Не очень");;
//Область ввода текста
    driver.findElement(By.id("text")).clear();
    driver.findElement(By.cssSelector("#text")).sendKeys("Привет мир!");
//Нажимаем на кнопку "Сброс"
    driver.findElement(By.name("reset")).click();
//Проверяем, что кнопка "Сброс" отработала как надо и элементы вернули первоначальные значения
   try{
//---поле ввода текста
     assertEquals("Тестовый текст...", driver.findElement(By.id("text")).getText());
//---и Сheckbox "другая цифра" 
     assertTrue(driver.findElement(By.id("other")).isSelected()) ;
       } catch(Exception e){
    	                   log.info("!!!!!!!! TEST FINISH ELEMENTS FAIL " + e.toString()+ " !!!!!!!!");	
		                   fail();
		                   return;
	  }
    Thread.sleep(1000);
    log.info("!!!!!!!! TEST FINISH ELEMENT GOOD !!!!!!!!");	
  }
//-------Тест для  не успешной авторизации (изменен только пароль)
  @Test
  public void testNoLogon() throws Exception {
//Добавляем в лог, что Тест авторизации начал выполнение
    log.info("!!!!!!!! ТЕСТ BAD АВТОРИЗАЦИИ НАЧАЛ ВЫПОЛНЕНИЯ !!!!!!!!");
//открываем тестовую страничку
    driver.get("http://192.168.2.100/index.html");
//Находим по имени тега поле для ввода логина и производим его очистку
    driver.findElement(By.name("login")).clear();
//Вводим логин "admin"   
    driver.findElement(By.name("login")).sendKeys("admin");
//Используя язык CSS находим по имени inputa поле для ввода пароля и очищаем его      
    driver.findElement(By.cssSelector("input[name=\"password\"]")).clear();
//Вводим пароль "admin"    
    driver.findElement(By.cssSelector("input[name=\"password\"]")).sendKeys("admin1");
//Используя язык запросов xpath находим кнопку входа авторизации и нажимаем по ней 
    driver.findElement(By.xpath("/html/body/div[1]/form/fieldset/input")).click();
//Проверяем результат авторизации     
    try{
//проверяем наличие элемента на страницы Logon_ok 
	  driver.findElement(By.xpath("//div[@id='logon_ok']"));
//Если элемент не найден, то добавляем информацию в лог и завершаем тест как неудачный
    }catch(NoSuchElementException e){ 
    	                            log.info("!!!!!!!! TEST FINISH BAD LOGON !!!" + 
                                    e.toString() + " !!!!!!!!");
//Небольшая задержка, что бы успеть увидеть в момент тестирования результат в консоли 	  
	                                Thread.sleep(1000);
	                                fail();
	                                return;
	  }
//Если элемент нашелся то заносим в лог информацию об удачном завершении
    log.info("!!!!!!!! TEST FINISH GOOD LOGON !!!!!!!!");
    Thread.sleep(1000);
     }
//Закрываем браузер и освобождаем все ресурсы (метод будет вызван после выполнения тестов)
  @After
  public void tearDown() throws Exception {
    driver.quit();
  }
 }
