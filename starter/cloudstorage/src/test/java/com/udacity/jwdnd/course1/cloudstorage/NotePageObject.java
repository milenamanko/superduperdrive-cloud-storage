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

public class NotePageObject {

    @FindBy(id = "new-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "edit-note-button")
    private List<WebElement> editNoteButtons;

    @FindBy(id = "delete-note-button")
    private List<WebElement> deleteNoteButtons;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "save-note-button")
    private WebElement saveNoteButton;

    @FindBy(id = "note-title-display")
    private WebElement displayNoteTitle;

    @FindBy(id = "note-description-display")
    private WebElement displayNoteDescription;

    public NotePageObject(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void clickAddNote() {
        addNoteButton.click();
    }

    public void addNote(String noteTitle, String noteDescription) {
        inputNoteTitle.sendKeys(noteTitle);
        inputNoteDescription.sendKeys(noteDescription);
        saveNoteButton.click();
    }

    public WebElement getDisplayNoteTitle() {
        return displayNoteTitle;
    }

    public WebElement getDisplayNoteDescription() {
        return displayNoteDescription;
    }

    public void clickEditNote(Integer index) {
        editNoteButtons.get(index).click();
    }

    public void editNote(String newNoteTitle, String newNoteDescription) {
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(newNoteTitle);
        inputNoteDescription.clear();
        inputNoteDescription.sendKeys(newNoteDescription);
        saveNoteButton.click();
    }

    public void deleteNote(Integer index) {
        deleteNoteButtons.get(index).click();
    }

    public void setWait(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOfAllElements(displayNoteTitle, displayNoteDescription));
    }

    public boolean isNotePresent(WebDriver driver) {
        try {
            driver.findElement(By.id("note-description-display"));
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }
}
