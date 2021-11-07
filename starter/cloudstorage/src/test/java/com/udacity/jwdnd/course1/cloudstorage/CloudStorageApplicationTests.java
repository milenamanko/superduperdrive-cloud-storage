package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    private LoginPageObject loginPageObject;

    private SignupPageObject signupPageObject;

    private HomePageObject homePageObject;

    private NotePageObject notePageObject;

    //login data
    String firstName = "Frank";
    String lastName = "Sinatra";
    String username = "fsinatra";
    String password = "guineaPig5";

    @LocalServerPort
    private int port;

    public String baseURL;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        baseURL = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    void contextLoads() {
    }

    //login and signup tests
    @Test
    public void getLoginPage() {

        driver.get(baseURL + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUnauthorizedAccess() {

        driver.get(baseURL + "/home");

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());

        driver.get(baseURL + "/logout");

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());

        driver.get(baseURL + "/login");

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());

        driver.get(baseURL + "/signup");

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assertions.assertEquals(baseURL + "/signup", driver.getCurrentUrl());

    }

    @Test
    public void testSignupLoginLogout() {

        driver.get(baseURL + "/signup");

        signupPageObject = new SignupPageObject(driver);
        signupPageObject.addData(firstName, lastName, username, password);
        signupPageObject.submitSignup();

        driver.get(baseURL + "/login");

        loginPageObject = new LoginPageObject(driver);
        loginPageObject.addData(username, password);
        loginPageObject.submitLogin();

        driver.get(baseURL + "/home");

        homePageObject = new HomePageObject(driver);
        homePageObject.logout();

        driver.get(baseURL + "/home");

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());
    }

    //note tests

    @Test
    public void testCreateNote() {

        String noteTitle = "shopping list";
        String noteDescription = "avocado, banana, potatoes";

        driver.get(baseURL + "/signup");

        signupPageObject = new SignupPageObject(driver);
        signupPageObject.addData(firstName, lastName, username, password);
        signupPageObject.submitSignup();

        driver.get(baseURL + "/login");

        loginPageObject = new LoginPageObject(driver);
        loginPageObject.addData(username, password);
        loginPageObject.submitLogin();

        driver.get(baseURL + "/home");

        homePageObject = new HomePageObject(driver);
        homePageObject.goToNotes();
        notePageObject = new NotePageObject(driver);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        notePageObject.clickAddNote();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        notePageObject.addNote(noteTitle, noteDescription);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();
        notePageObject.setWait(driver);

        Assertions.assertEquals(noteTitle, driver.findElement(By.id("note-title-display")).getText());
        Assertions.assertEquals(noteDescription, driver.findElement(By.id("note-description-display")).getText());
    }

    @Test
    public void testEditNote() {

        String newNoteTitle = "shopping list2";
        String newNoteDescription = "avocado, banana, potatoes, cheese";

        testCreateNote();

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();
        notePageObject.clickEditNote(0);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        notePageObject.editNote(newNoteTitle, newNoteDescription);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();
        notePageObject.setWait(driver);

        Assertions.assertEquals(newNoteTitle, driver.findElement(By.id("note-title-display")).getText());
        Assertions.assertEquals(newNoteDescription, driver.findElement(By.id("note-description-display")).getText());
    }

    @Test
    public void testDeleteNote() {

        testCreateNote();

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();
        notePageObject.deleteNote(0);

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();

        Assertions.assertFalse(notePageObject.isNotePresent(driver));
    }

    //credential tests



}
