package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

    private LoginPageObject loginPageObject;

    private SignupPageObject signupPageObject;

    private HomePageObject homePageObject;

    private NotePageObject notePageObject;

    private CredentialPageObject credentialPageObject;

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

    public void loginToPage() {

        String firstName = "Frank";
        String lastName = "Sinatra";
        String username = "fsinatra";
        String password = "guineaPig5";

        driver.get(baseURL + "/signup");

        signupPageObject = new SignupPageObject(driver);
        signupPageObject.addData(firstName, lastName, username, password);
        signupPageObject.submitSignup();

        driver.get(baseURL + "/login");

        loginPageObject = new LoginPageObject(driver);
        loginPageObject.addData(username, password);
        loginPageObject.submitLogin();
    }

    @Test
    @Transactional
    void contextLoads() {
    }

    //login and signup tests
    @Test
    @Transactional
    public void getLoginPage() {

        driver.get(baseURL + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Transactional
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
    @Transactional
    public void testSignupLoginLogout() {

        loginToPage();

        driver.get(baseURL + "/home");

        homePageObject = new HomePageObject(driver);
        homePageObject.logout();

        driver.get(baseURL + "/home");

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());
    }

    //note tests
    @Test
    @Transactional
    public void testCreateNote() {

        loginToPage();

        String noteTitle = "shopping list";
        String noteDescription = "avocado, banana, potatoes";

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

        Assertions.assertEquals(noteTitle, notePageObject.getDisplayNoteTitle().getText());
        Assertions.assertEquals(noteDescription, notePageObject.getDisplayNoteDescription().getText());
    }

    @Test
    @Transactional
    public void testEditNote() {

        loginToPage();

        String noteTitle = "shopping list";
        String noteDescription = "avocado, banana, potatoes";
        String newNoteTitle = "shopping list2";
        String newNoteDescription = "avocado, banana, potatoes, cheese";

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
        notePageObject.clickEditNote(0);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        notePageObject.editNote(newNoteTitle, newNoteDescription);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();
        notePageObject.setWait(driver);

        Assertions.assertEquals(newNoteTitle, notePageObject.getDisplayNoteTitle().getText());
        Assertions.assertEquals(newNoteDescription, notePageObject.getDisplayNoteDescription().getText());
    }

    @Test
    @Transactional
    public void testDeleteNote() {

        loginToPage();

        String noteTitle = "shopping list";
        String noteDescription = "avocado, banana, potatoes";

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
        notePageObject.deleteNote(0);

        driver.get(baseURL + "/home");

        homePageObject.goToNotes();

        Assertions.assertFalse(notePageObject.isNotePresent(driver));
    }

    //credential tests
    @Test
    @Transactional
    public void testCreateCredentials() {

        loginToPage();

        String credUrl = "https://www.udacity.com";
        String credUsername = "franksinatra";
        String credPassword = "toTheMoon";

        driver.get(baseURL + "/home");

        homePageObject = new HomePageObject(driver);
        homePageObject.goToCredentials();
        credentialPageObject = new CredentialPageObject(driver);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        credentialPageObject.clickAddCredential();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        credentialPageObject.addCredential(credUrl, credUsername, credPassword);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToCredentials();
        credentialPageObject.setWait(driver);

        Assertions.assertEquals(credUrl, credentialPageObject.getDisplayCredentialUrl().getText());
        Assertions.assertEquals(credUsername, credentialPageObject.getDisplayCredentialUsername().getText());
        Assertions.assertNotEquals(credPassword, credentialPageObject.getDisplayCredentialPassword().getText());

    }

    @Test
    @Transactional
    public void testEditCredentials() {

        loginToPage();

        String credUrl = "https://www.udacity.com";
        String credUsername = "franksinatra";
        String credPassword = "toTheMoon";
        String newCredUrl = "https://www.udacity.com";
        String newCredUsername = "franksinatra";
        String newCredPassword = "toTheMoon123";

        driver.get(baseURL + "/home");

        homePageObject = new HomePageObject(driver);
        homePageObject.goToCredentials();
        credentialPageObject = new CredentialPageObject(driver);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        credentialPageObject.clickAddCredential();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        credentialPageObject.addCredential(credUrl, credUsername, credPassword);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToCredentials();
        credentialPageObject.clickEditCredential(0);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        Assertions.assertTrue(driver.getPageSource().contains(credPassword));

        credentialPageObject.editCredential(newCredUrl, newCredUsername, newCredPassword);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToCredentials();
        credentialPageObject.setWait(driver);

        Assertions.assertEquals(newCredUrl, credentialPageObject.getDisplayCredentialUrl().getText());
        Assertions.assertEquals(newCredUsername, credentialPageObject.getDisplayCredentialUsername().getText());
        Assertions.assertNotEquals(newCredPassword, credentialPageObject.getDisplayCredentialPassword().getText());
    }

    @Test
    @Transactional
    public void testDeleteCredentials() {

        loginToPage();

        String credUrl = "https://www.udacity.com";
        String credUsername = "franksinatra";
        String credPassword = "toTheMoon";

        driver.get(baseURL + "/home");

        homePageObject = new HomePageObject(driver);
        homePageObject.goToCredentials();
        credentialPageObject = new CredentialPageObject(driver);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        credentialPageObject.clickAddCredential();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        credentialPageObject.addCredential(credUrl, credUsername, credPassword);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        driver.get(baseURL + "/home");

        homePageObject.goToCredentials();
        credentialPageObject.deleteCredential(0);

        driver.get(baseURL + "/home");

        homePageObject.goToCredentials();

        Assertions.assertFalse(credentialPageObject.isCredentialPresent(driver));

    }
}
