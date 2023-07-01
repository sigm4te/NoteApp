package com.example.noteapp.di

import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.datasource.DataProvider
import com.example.noteapp.mvvm.model.datasource.FirestoreProvider
import com.example.noteapp.mvvm.viewmodel.MainViewModel
import com.example.noteapp.mvvm.viewmodel.NoteViewModel
import com.example.noteapp.mvvm.viewmodel.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<DataProvider> { FirestoreProvider(get(), get()) }
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}