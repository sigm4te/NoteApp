package com.example.noteapp.mvvm.model.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.data.entity.Note.Color.*
import java.util.UUID

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private var notes = mutableListOf(
        Note(UUID.randomUUID().toString(), "Note #1", "The first one.", BLUE),
        Note(UUID.randomUUID().toString(), "Note #2", "The second one.", VIOLET),
        Note(UUID.randomUUID().toString(), "Note #3", "The third one", PINK),
        Note(UUID.randomUUID().toString(), "Note #4", "The fourth one.", RED),
        Note(UUID.randomUUID().toString(), "Note #5", "The fifth one.", YELLOW),
        Note(UUID.randomUUID().toString(), "Note #6", "The sixth one.", GREEN),
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes() : LiveData<List<Note>> = notesLiveData

    fun saveNote(note : Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note : Note) {
        for (i in 0 until notes.size) {
            if (note == notes[i]) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }
}