package com.example.noteapp.mvvm.model.data

import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.datasource.DataProvider

class NotesRepository(private val dataProvider: DataProvider) {

    fun getCurrentUser() = dataProvider.getCurrentUser()
    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun saveNote(note: Note) = dataProvider.saveNote(note)
}