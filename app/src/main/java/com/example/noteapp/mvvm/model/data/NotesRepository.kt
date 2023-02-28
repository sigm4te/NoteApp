package com.example.noteapp.mvvm.model.data

import com.example.noteapp.mvvm.model.data.entity.Note

object NotesRepository {
    private val notes: List<Note> = listOf(
        Note("Note #1", "The first one.", 0xfff08080.toInt()),
        Note("Note #2", "The second one.", 0xff00ff7f.toInt()),
        Note("Note #3", "The third one", 0xffff8c00.toInt()),
        Note("Note #4", "The fourth one.", 0xffdda0dd.toInt()),
        Note("Note #5", "The fifth one.", 0xff00bfff.toInt()),
        Note("Note #6", "The sixth one.", 0xffffd700.toInt()),
    )

    fun getNotes() = notes
}