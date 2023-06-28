package com.example.noteapp.mvvm.model.data

import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.datasource.DataProvider
import com.example.noteapp.mvvm.model.datasource.FirestoreProvider

object NotesRepository {

    private val dataProvider: DataProvider = FirestoreProvider()

    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun saveNote(note: Note) = dataProvider.saveNote(note)
}