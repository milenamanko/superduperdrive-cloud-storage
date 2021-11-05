package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class NoteController {

    public final NoteService noteService;

    public final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/home/note/newNote")
    public String uploadNote(Authentication authentication, Model model, Note note) throws IOException {

        String noteError = "";

        if (note.getNoteTitle().equals("")) {
            noteError = "Please specify note title.";
        }

        if (noteError.equals("")) {

            note.setUserId(userService.getUser(authentication.getName()).getUserId());
            int noteCount = noteService.addNote(note);
            if (noteCount < 1) {
                noteError = "An error has occurred while adding note. Please try again.";
            }
        }

        if (noteError.equals("")) {
            model.addAttribute("successMsg", "Note has been added successfully.");
        } else {
            model.addAttribute("errorMsg", noteError);
        }

        return "result";
    }

    @RequestMapping(value = "/home/note/delete/{noteId}", method = RequestMethod.GET)
    public String deleteNote(@PathVariable Integer noteId, Model model) {

        String noteError = "";

        if (noteService.getNoteById(noteId) == null) {
            noteError = "Note doesn't exist.";
        }

        if (noteError.equals("")) {

            int noteCount = noteService.deleteNote(noteId);
            if (noteCount < 1) {
                noteError = "An error has occurred while deleting. Please try again.";
            }
        }

        if (noteError.equals("")) {
            model.addAttribute("successMsg", "Note has been deleted successfully.");
        } else {
            model.addAttribute("errorMsg", noteError);
        }

        return "result";
    }

}
