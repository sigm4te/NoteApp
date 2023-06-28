package com.example.noteapp.mvvm.viewstate

import com.example.noteapp.mvvm.model.data.entity.Note

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)