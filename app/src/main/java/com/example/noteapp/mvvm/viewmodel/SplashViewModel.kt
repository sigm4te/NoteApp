package com.example.noteapp.mvvm.viewmodel

import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.error.NoAuthException
import kotlinx.coroutines.launch

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?>() {

    fun requestUser() = launch {
        notesRepository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }
}