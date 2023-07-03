package com.example.noteapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.noteapp.R
import com.example.noteapp.mvvm.model.datasource.ColorGetter
import com.example.noteapp.mvvm.model.data.entity.Color

class AndroidColorGetter(private val context: Context) : ColorGetter {

    override fun getColors(): Array<Color> = Color.values()
    override fun getColorInt(color: Color): Int = ContextCompat.getColor(context, getColorRes(color))
    override fun getColorRes(color: Color): Int = when (color) {
        Color.WHITE -> R.color.white
        Color.RED -> R.color.red
        Color.GREEN -> R.color.green
        Color.BLUE -> R.color.blue
        Color.YELLOW -> R.color.yellow
        Color.VIOLET -> R.color.violet
        Color.PINK -> R.color.pink
    }
}