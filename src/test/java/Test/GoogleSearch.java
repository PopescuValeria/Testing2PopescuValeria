package Test;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleSearch {
    @Test
    public void googleSearch() {
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();


        //String driverExecutablePath = "C:\\Users\\user\\Downloads\\chromedriver_win32\\chromedriver.exe";
        //String driverExecutablePath = "D:\\Install\\chromedriver_win32\\chromedriver.exe";
        String driverExecutablePath = "D:\\Install\\geckodriver-v0.31.0-win32\\geckodriver.exe";

        //System.setProperty("webdriver.chrome.driver", driverExecutablePath);

        System.setProperty("webdriver.gecko.driver", driverExecutablePath);

        /*WebDriver driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();*/;
        WebDriver driver = new FirefoxDriver();
        FirefoxOptions options = new FirefoxOptions();

        String selectLinkOpenInNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
        options.addArguments("--start-maximized");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //wait before each element
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);


        driver.get("https://www.google.com/");
        //or old version
        //driver.navigate().to("google.com");
        driver.getWindowHandle();


        WebElement element = driver.findElement(By.name("q"));
        //element = driver.findElement(By.xpath(".//*[@title='Căutați']"));
//        driver.findElements(By.xpath()) --- returns list of WebElements
//        driver.findElement(By.xpath())
//        driver.findElement(By.cssSelector())
//        driver.findElement(By.id())
//        and other variants

        String key = "Cheetah";

        element.sendKeys(key);
        element.submit();

        //root element
        Element rootElement = doc.createElement(key);
        doc.appendChild(rootElement);

        //callback function wait for event - load page


        System.out.println("Page title " + driver.getTitle());

        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver1) {
                return driver1.getTitle().toLowerCase().startsWith("cheetah");
            }
        });

        WebDriverWait driverWait = new WebDriverWait(driver,15);
        List<WebElement> links = driver.findElements(By.xpath("//div[@class='g']//a[contains(.,'heetah')]"));

        //calculam cate ori este afisat cuvantul cheetah
        //List<WebElement> links = driver.findElements(By.xpath( "//div//h3//span[not(contains(text(),'Oamenii au mai întrebat și'))][contains(.,'heetah')]"));
        ////div//h3[not(contains(text(),'Oamenii au mai întrebat și'))][not(contains(text(),'Videoclipuri'))][contains(text(),'heetah')]
        //"//div//h3[not(contains(text(),'People also ask'))]//div[@class='r']//a[contains(.,'heetah')]"));
        //for one element
        System.out.println("Result = "+links.size());

        //driverWait.until(ExpectedConditions.elementToBeClickable(links.get(1))).click();
        //links.get(0).sendKeys(selectLinkOpenInNewTab);
        /*JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", links.get(0));*/

        System.out.println("Page title tab1" + driver.getTitle());
        System.out.println("Page url tab1" + driver.getCurrentUrl());
        //        add stringUtils in pom.xml
       //System.out.println("Number of cheetahs: " + StringUtils.countMatches(driver.getPageSource().toLowerCase(), "cheetah"));

            Element element1 = doc.createElement("links");
            rootElement.appendChild(element1);

        int numberOfLinks = links.size();
        for (int iLinks = 0; iLinks < numberOfLinks; iLinks++) {
            links.get(iLinks).sendKeys(selectLinkOpenInNewTab);
        }


        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        for (int iTabs = 0; iTabs < tabs.size(); iTabs++) {
            driver.switchTo().window(tabs.get(iTabs));
            driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
            System.out.println("Page title  " + iTabs + " " + driver.getTitle());
            System.out.println("Page url "  + iTabs + " " +  driver.getCurrentUrl());
            System.out.println("Number of cheetahs: on "  +  driver.getTitle() + "   "  + StringUtils.countMatches(driver.getPageSource().toLowerCase(), "cheetah"));
            System.out.println();

            Element element2 = doc.createElement("link");
            element1.appendChild(element2);

            Element url = doc.createElement("url");
            url.appendChild(doc.createTextNode(driver.getCurrentUrl()));
            element2.appendChild(url);

            Element pageName = doc.createElement("page-name");
            pageName.appendChild(doc.createTextNode(driver.getTitle()));
            element2.appendChild(pageName);

            Element number = doc.createElement("number-of-occurrence-of-yhe-word-on-page");
            number.appendChild(doc.createTextNode(String.valueOf(StringUtils.countMatches(driver.getPageSource().toLowerCase(), "cheetah"))));
            element2.appendChild(number);
        }
        System.out.println("numberOfLinks = " + numberOfLinks);

        Thread.sleep(30000);
        driver.quit();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("testare2.xml"));
            transformer.transform(source, result);

            //output to console for testing
            StreamResult condoleResult = new StreamResult(System.out);
            transformer.transform(source, condoleResult);

        }catch (Exception e){

        }
    }
}
