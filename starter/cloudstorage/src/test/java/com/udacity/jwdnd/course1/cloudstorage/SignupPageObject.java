package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPageObject {

    @FindBy(id = "inputFirstName")
    private WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    private WebElement inputLastName;

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-signup-button")
    private WebElement submitSignupButton;

    public SignupPageObject (WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addData(String firstName, String lastName, String username, String password) {
        inputFirstName.sendKeys(firstName);
        inputLastName.sendKeys(lastName);
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
    }

    public void submitSignup() {
        submitSignupButton.click();
    }
}
