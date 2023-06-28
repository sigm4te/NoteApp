package com.example.noteapp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewmodel.MainViewModel
import com.example.noteapp.mvvm.viewstate.MainViewState
import com.example.noteapp.ui.adapter.NotesAdapter

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    private lateinit var adapter: NotesAdapter
    private lateinit var binding: ActivityMainBinding
    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layout: View by lazy {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.rvMain.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesAdapter {
            NoteActivity.start(this, it.id)
        }
        binding.rvMain.adapter = adapter
    }

    private fun initListeners() {
        binding.fabMain.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it }
    }
}