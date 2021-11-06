package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPageObject {

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-login-button")
    private WebElement submitLoginButton;

    public LoginPageObject(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addData(String username, String password) {
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
    }

    public void submitLogin() {
        submitLoginButton.click();
    }
}
