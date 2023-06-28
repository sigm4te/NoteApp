package com.example.noteapp.mvvm.viewmodel

import androidx.lifecycle.Observer
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewstate.MainViewState

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<Result> { result ->
        result ?: return@Observer
        when(result){
            is Result.Success<*> -> viewStateLiveData.value = MainViewState(notes = result.data as? List<Note>)
            is Result.Error -> viewStateLiveData.value = MainViewState(error = result.error)
        }
    }
    private val notesRepository = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        notesRepository.observeForever(notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        notesRepository.removeObserver(notesObserver)
    }
}