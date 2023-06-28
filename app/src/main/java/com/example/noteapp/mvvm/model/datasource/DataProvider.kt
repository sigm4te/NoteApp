package com.example.noteapp.mvvm.model.datasource

import androidx.lifecycle.LiveData
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note

interface DataProvider {
    fun subscribeToAllNotes() : LiveData<Result>
    fun getNoteById(id: String) : LiveData<Result>
    fun saveNote(note: Note) : LiveData<Result>
}