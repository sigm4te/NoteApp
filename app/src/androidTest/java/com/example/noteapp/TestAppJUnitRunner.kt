package com.example.noteapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.noteapp.application.TestApp

class TestAppJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}