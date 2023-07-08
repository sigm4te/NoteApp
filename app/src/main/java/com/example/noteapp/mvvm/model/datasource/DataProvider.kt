package com.example.noteapp.mvvm.model.datasource

import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.data.entity.User
import kotlinx.coroutines.channels.ReceiveChannel

interface DataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<Result>

    suspend fun getCurrentUser(): User?
    suspend fun getNoteById(id: String): Note?
    suspend fun saveNote(note: Note): Note
    suspend fun deleteNote(id: String)
}