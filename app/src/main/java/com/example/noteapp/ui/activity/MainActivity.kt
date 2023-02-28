package com.example.noteapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.ui.adapter.NotesAdapter
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.mvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMain.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter()
        binding.rvMain.adapter = adapter

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNotesLiveData().observe(this, Observer { value ->
            value?.let { adapter.notes = it }
        })
    }
}