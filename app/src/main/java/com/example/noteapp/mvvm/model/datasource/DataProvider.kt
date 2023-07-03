package com.example.noteapp.mvvm.model.datasource

import androidx.lifecycle.LiveData
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.data.entity.User

interface DataProvider {
    fun getCurrentUser() : LiveData<User?>
    fun subscribeToAllNotes() : LiveData<Result>
    fun getNoteById(id: String) : LiveData<Result>
    fun saveNote(note: Note) : LiveData<Result>
    fun deleteNote(id: String) : LiveData<Result>
}