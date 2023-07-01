package com.example.noteapp.application

import android.app.Application
import com.example.noteapp.di.appModule
import com.example.noteapp.di.mainModule
import com.example.noteapp.di.noteModule
import com.example.noteapp.di.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApp : Application() {

    companion object {
        lateinit var instance: NoteApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin() {
            androidContext(this@NoteApp)
            modules(listOf(appModule, splashModule, mainModule, noteModule))
        }
    }
}