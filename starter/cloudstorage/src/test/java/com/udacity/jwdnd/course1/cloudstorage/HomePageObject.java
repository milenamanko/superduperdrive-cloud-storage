package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePageObject {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(xpath = ("//div[@id = 'contentDiv']//div[@id = 'nav-tab']//a[@id = 'nav-notes-tab']"))
    private WebElement navNotesTab;

    @FindBy(xpath = ("//div[@id = 'contentDiv']//div[@id = 'nav-tab']//a[@id = 'nav-credentials-tab']"))
    private WebElement navCredentialTab;


    public HomePageObject(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void goToNotes() {
        navNotesTab.click();
    }

    public void goToCredentials() {
        navCredentialTab.click();
    }
}
