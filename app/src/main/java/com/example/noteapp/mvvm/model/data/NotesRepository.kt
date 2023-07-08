package com.example.noteapp.mvvm.model.data

import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.datasource.DataProvider

class NotesRepository(private val dataProvider: DataProvider) {

    fun getNotes() = dataProvider.subscribeToAllNotes()

    suspend fun getCurrentUser() = dataProvider.getCurrentUser()
    suspend fun getNoteById(id: String) = dataProvider.getNoteById(id)
    suspend fun saveNote(note: Note) = dataProvider.saveNote(note)
    suspend fun deleteNote(id: String) = dataProvider.deleteNote(id)
}