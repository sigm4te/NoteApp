package com.example.noteapp.mvvm.viewmodel

import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewstate.NoteViewState

class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        val noteLiveData = notesRepository.getNoteById(noteId)
        noteLiveData.observeForever { result ->
            result ?: return@observeForever
            when (result) {
                is Result.Success<*> -> viewStateLiveData.value = NoteViewState(note = result.data as? Note)
                is Result.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
        }
    }

    override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }
}