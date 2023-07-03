package com.example.noteapp.utils

import android.content.Context
import android.view.View

inline fun View.dip(value: Int): Float = context.dip(value)
inline fun Context.dip(value: Int): Float = resources.displayMetrics.density * value