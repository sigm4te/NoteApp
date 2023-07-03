package com.example.noteapp.mvvm.viewstate

import com.example.noteapp.mvvm.model.data.entity.Note

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val deletionFlag: Boolean = false, val note: Note? = null)
}