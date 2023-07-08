package com.example.noteapp.mvvm.viewmodel

import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.entity.Note
import kotlinx.coroutines.launch

class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Pair<Note?, Boolean>>() {

    private val currentNote: Note?
        get() = getViewState().tryReceive().getOrNull()?.first

    fun saveChanges(note: Note) {
        setData(Pair(note, false))
    }

    fun loadNote(noteId: String) = launch {
        try {
            notesRepository.getNoteById(noteId)?.let {
                setData(Pair(it, false))
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    fun deleteNote() = launch {
        try {
            currentNote?.let {
                setData(Pair(null, true))
                notesRepository.deleteNote(it.id)
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    override fun onCleared() {
        launch {
            currentNote?.let { notesRepository.saveNote(it) }
            super.onCleared()
        }
    }
}