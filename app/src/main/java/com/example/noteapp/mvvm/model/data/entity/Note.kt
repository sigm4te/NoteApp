package com.example.noteapp.mvvm.model.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    var id: String = "",
    var title: String = "",
    var text: String = "",
    var color: Color = Color.WHITE,
    var lastChanged: Date = Date()
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (id != (other as Note).id) return false
        return true
    }
}