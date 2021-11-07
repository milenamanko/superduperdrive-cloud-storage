package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialPageObject {

    @FindBy(id = "new-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "edit-credential-button")
    private List<WebElement> editCredentialButtons;

    @FindBy(id = "delete-credential-button")
    private List<WebElement> deleteCredentialButtons;

    @FindBy(id = "credential-url")
    private WebElement inputCredentialUrl;

    @FindBy(id = "credential-username")
    private WebElement inputCredentialUsername;

    @FindBy(id = "credential-password")
    private WebElement inputCredentialPassword;

    @FindBy(id = "save-credential-button")
    private WebElement saveCredentialButton;

    @FindBy(id = "credential-url-display")
    private WebElement displayCredentialUrl;

    @FindBy(id = "credential-username-display")
    private WebElement displayCredentialUsername;

    @FindBy(id = "credential-password-display")
    private WebElement displayCredentialPassword;

    public CredentialPageObject(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public WebElement getDisplayCredentialUrl() {
        return displayCredentialUrl;
    }

    public WebElement getDisplayCredentialUsername() {
        return displayCredentialUsername;
    }

    public WebElement getDisplayCredentialPassword() {
        return displayCredentialPassword;
    }

    public void clickAddCredential() {
        addCredentialButton.click();
    }

    public void addCredential(String url, String username, String password) {
        inputCredentialUrl.sendKeys(url);
        inputCredentialUsername.sendKeys(username);
        inputCredentialPassword.sendKeys(password);
        saveCredentialButton.click();
    }

    public void clickEditCredential(Integer index) {
        editCredentialButtons.get(index).click();
    }

    public void editCredential(String newUrl, String newUsername, String newPassword) {
        inputCredentialUrl.clear();
        inputCredentialUrl.sendKeys(newUrl);
        inputCredentialUsername.clear();
        inputCredentialUsername.sendKeys(newUsername);
        inputCredentialPassword.clear();
        inputCredentialPassword.sendKeys(newPassword);
        saveCredentialButton.click();
    }

    public void deleteCredential(Integer index) {
        deleteCredentialButtons.get(index).click();
    }

    public void setWait(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElements(displayCredentialUrl, displayCredentialUsername, displayCredentialPassword));
    }

    public boolean isCredentialPresent(WebDriver driver) {
        try {
            driver.findElement(By.id("credential-url-display"));
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

}
