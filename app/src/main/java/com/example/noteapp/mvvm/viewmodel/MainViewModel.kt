package com.example.noteapp.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.entity.Note

class MainViewModel : ViewModel() {

    private var notesLiveData = MutableLiveData<List<Note>>()

    init {
        notesLiveData.value = NotesRepository.getNotes()
    }

    fun getNotesLiveData(): LiveData<List<Note>> = notesLiveData
}