package com.example.noteapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.mvvm.viewmodel.MainViewModel
import com.example.noteapp.ui.adapter.NotesAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initViews()
        initListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNotesLiveData().observe(this, Observer { value ->
            value?.let { adapter.notes = it }
        })
    }

    private fun initViews() {
        binding.rvMain.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter {
            NoteActivity.start(this, it)
        }
        binding.rvMain.adapter = adapter
    }

    private fun initListeners() {
        binding.fabMain.setOnClickListener {
            NoteActivity.start(this)
        }
    }
}