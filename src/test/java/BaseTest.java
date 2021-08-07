import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@DisplayName("Тестирование заполнения полей на рефенансирование")
public class BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static ChromeOptions options;
    //шаблоны для xpath
    static private String fieldXPath = "//input[@name='%s']";
    static private String fieldXPath2 = "//div[contains(text(), '%s')]";


    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        System.out.println("BeforeAll");
    }

    //пропишем путь и проверим путь до заполнения полей
    @BeforeEach
    void BeforeEach() {
        String baseUrl1 = "https://www.raiffeisen.ru";
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get(baseUrl1);
        wait = new WebDriverWait(driver, 15, 1000);
        System.out.println("BeforeEach");

        //Выбираем пункт Ипотека
        String ipoteka = "//a[contains(text(), 'Ипотека')]";
        WebElement btnIpoteka = driver.findElement(By.xpath(ipoteka));
        waitUtilElementBeClickable(btnIpoteka);
        btnIpoteka.click();

        //Выбираем подпункт Рефинансирование
        String refinancing = "//div[@id='menu2']//a[contains(text(), 'Рефинансирование')]";
        WebElement btnRefinancing = driver.findElement(By.xpath(refinancing));
        waitUtilElementBeClickable(btnRefinancing);
        btnRefinancing.click();

        //тут сравнение надо сделать, чтобы удостовериться, что мы на нужной части.
        Assertions.assertEquals("Рефинансирование ипотеки и других кредитов", driver.findElement(By.xpath("//h1")).getText(), "Заголовок отсутствует/не соответствует требуемому");

        //Дальше нужно нажать на кнопку остаивть заявку
        String application = "//div[@class='care__block-buttons']//a[contains(text(), 'Оставить заявку')]";
        WebElement btnApplication = driver.findElement(By.xpath(application));
        moveToElement(btnApplication);
        waitUtilElementBeClickable(btnApplication);
        btnApplication.click();

        //тут сравнение надо сделать, чтобы удостовериться, что мы на нужной части.
        Assertions.assertEquals("Предварительное решение по ипотеке", driver.findElement(By.xpath("//span[contains(text(), 'Предварительное решение по ипотеке')]")).getAttribute("innerText"), "Заголовок отсутствует/не соответствует требуемому");

    }

    @DisplayName("Обычный тест")
//    @Disabled
    @Test
    void routineTest() {

        selectSex("Men");
        boolean russiaCitizen = false;
        selecCizitenShip(russiaCitizen, "Абхазия");

        String name = "Иванов Иван Иванович";
        String addressRegistration = "г Москва, Ломоносовский пр-кт, д 27";

        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "fullName"))), name);
        fillInputElement(fieldXPath2, name);
        fillInputData(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), "12.12.1995");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthPlace"))), "Москва");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "foreignSeries"))), "1111");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "foreignNumber"))), "11111");
        fillInputData(driver.findElement(By.xpath(String.format(fieldXPath, "foreignIssuedDate"))), "10.10.1993");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "foreignIssuedBy"))), "г.Москва");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "registrationAddress"))), addressRegistration);
        fillInputElement(fieldXPath2, addressRegistration);
        fillInputPhone(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), "(927) 999-99-90");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

//    @Disabled
    @DisplayName("Параметризированный тест")
    @ParameterizedTest
    @MethodSource("generateData")
    void method2(String fullName, String sex, String birthDate, String birthPlace,String isRussiaCitizen, String country, String foreignSeries, String foreignNumber,
                 String foreignIssuedDate, String foreignIssuedBy, String registrationAddress, String phone) {

        selectSex(sex);
        boolean russiaCitizen = Boolean.parseBoolean(isRussiaCitizen);
        selecCizitenShip(russiaCitizen, country);

        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "fullName"))), fullName);
        fillInputElement(fieldXPath2, fullName);
        fillInputData(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), birthDate);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthPlace"))), birthPlace);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "foreignSeries"))), foreignSeries);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "foreignNumber"))), foreignNumber);
        fillInputData(driver.findElement(By.xpath(String.format(fieldXPath, "foreignIssuedDate"))), foreignIssuedDate);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "foreignIssuedBy"))), foreignIssuedBy);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "registrationAddress"))), registrationAddress);
        fillInputElement(fieldXPath2, registrationAddress);
        fillInputPhone(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), phone);

    }

    static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of("Иванов Иван Иванович","Men", "12.12.1995", "Москва","false", "Абхазия", "1111", "11111", "10.10.1993",
                        "г.Москва", "г Москва, Ломоносовский пр-кт, д 27", "(927) 999-99-90"),
                Arguments.of("Сергеев Сергей Сергеевич","Men", "11.11.1990", "Казань","false", "Абхазия", "2222", "22222", "11.11.1993",
                        "г.Казань", "Респ Татарстан, Высокогорский р-н, А/Д Казань-Арск перекресток к нп Каменка", "(999) 999-99-99"),
                Arguments.of("Иванов Иван Иванович", "Men", "12.12.1995", "Владивосток","false", "Абхазия", "3333", "3333", "22.22.1993",
                        "г.Владивосток", "г Владивосток, Восточный пр-кт, д 3", "(111) 111-11-11")
        );
    }

    @AfterEach
    void AfterEach() {

        //Продолжить
        String continuation = "//button[contains(text(),'Продолжить')]";
        WebElement btnContinuation = driver.findElement(By.xpath(continuation));
        waitUtilElementBeClickable(btnContinuation);
        btnContinuation.click();

        driver.quit();
        System.out.println("AfterEach");

    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll");
    }

    // элемент кликабельный
    void waitUtilElementBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //видим ли элемент
    void waitUtilElementToBeVisibilityOfElementLocated(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //заполняем телефон
    private void fillInputField(WebElement element, String value) {
        moveToElement(element);
        element.click();
        element.clear();
        element.sendKeys(value);
        Assertions.assertTrue(wait.until(ExpectedConditions.attributeContains(element, "value", value)), "Поле было заполнено некорректно");
    }

    //заполняем дату
    private void fillInputData(WebElement element, String value) {
        Actions action = new Actions(driver);
        action.moveToElement(element).click().sendKeys(value).build().perform();
    }

    //перемещаемся к элементу
    private void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    //заполняем телефон
    private void fillInputPhone(WebElement element, String value) {
        Actions action = new Actions(driver);
        action.moveToElement(element).click().sendKeys(value).build().perform();
    }

    private void fillInputElement(String xpath, String value) {
        String temp = String.format(xpath, value);
        waitUtilElementToBeVisibilityOfElementLocated(By.xpath(temp));
        WebElement element = driver.findElement(By.xpath(temp));
        element.click();
    }

    //выбираем пол
    private void selectSex(String sex) {
        WebElement btnSex = null;
        if (sex.equals("Men")) {
            String xpathSex = "//span[contains(text(),'Мужской')]";
            btnSex = driver.findElement(By.xpath(xpathSex));
        } else if (sex.equals("Women")) {
            String xpathSex = "//span[contains(text(),'Женский')]";
            btnSex = driver.findElement(By.xpath(xpathSex));
        }
        moveToElement(btnSex);
        waitUtilElementBeClickable(btnSex);
        btnSex.click();
    }

    //гражданином какой страны являемся
    private void selecCizitenShip(boolean b, String country) {
        if (!b) {
            //не являемся гражданином РФ
            String RF = "//label[@data-marker-field='isResident']";
            WebElement btnRF = driver.findElement(By.xpath(RF));
            waitUtilElementBeClickable(btnRF);
            btnRF.click();
            //список стран
            String countries = "//div[@data-marker=\"Select.value.Selectarea\"]";
            WebElement btnCountries = driver.findElement(By.xpath(countries));
            moveToElement(btnCountries);

            String xpathCountry = String.format("//div[contains(text(), '%s')]", country);
            WebElement btnCountry = driver.findElement(By.xpath(xpathCountry));
            moveToElement(btnCountry);
        }
    }

}