package com.example.noteapp.mvvm.viewstate

import com.example.noteapp.mvvm.model.data.entity.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)