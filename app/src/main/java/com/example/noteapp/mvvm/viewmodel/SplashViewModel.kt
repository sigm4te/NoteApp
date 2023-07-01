package com.example.noteapp.mvvm.viewmodel

import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.example.noteapp.mvvm.viewstate.SplashViewState

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        notesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}