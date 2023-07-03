package com.example.noteapp.mvvm.model.datasource

import com.example.noteapp.mvvm.model.data.entity.Color

interface ColorGetter {
    fun getColors(): Array<Color>
    fun getColorInt(color: Color): Int
    fun getColorRes(color: Color): Int
}