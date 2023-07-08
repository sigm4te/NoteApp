package com.example.noteapp.mvvm.viewmodel

import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(private val notesRepository: NotesRepository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = notesRepository.getNotes()

    init {
        launch {
            notesChannel.consumeEach { result ->
                when (result) {
                    is Result.Success<*> -> setData(result.data as? List<Note>)
                    is Result.Error -> setError(result.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}