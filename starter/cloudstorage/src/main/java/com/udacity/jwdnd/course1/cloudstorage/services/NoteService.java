package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NotesMapper notesMapper;


    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public Note getNoteById(Integer noteId) {
        return notesMapper.getNote(noteId);
    }

    public List<Note> getAllNotesByUserId(Integer userId) {
        return notesMapper.getAllNotes(userId);
    }

    public int addNote(Note note) {
        return notesMapper.insertNote(note);
    }

    public int deleteNoteById(Integer noteId) {
        return notesMapper.deleteNote(noteId);
    }

    public int updateNote(Note note) {
        return notesMapper.updateNote(note);
    }
}
