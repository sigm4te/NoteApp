package com.example.noteapp.mvvm.viewmodel

import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.example.noteapp.mvvm.viewstate.SplashViewState

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}