package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	private LoginPageObject loginPageObject;

	private SignupPageObject signupPageObject;

	private HomePageObject homePageObject;

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

		driver.get(baseURL + "/home");

		homePageObject = new HomePageObject(driver);
		homePageObject.logout();

		driver.get(baseURL + "/home");

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Assertions.assertEquals(baseURL + "/login", driver.getCurrentUrl());
	}

}
