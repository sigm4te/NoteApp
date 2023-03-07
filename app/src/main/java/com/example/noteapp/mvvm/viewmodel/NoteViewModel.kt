package com.example.noteapp.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.entity.Note

class NoteViewModel : ViewModel() {

    private var pendingNote : Note? = null

    fun save(note : Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}