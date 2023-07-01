package com.example.noteapp.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewmodel.MainViewModel
import com.example.noteapp.mvvm.viewstate.MainViewState
import com.example.noteapp.ui.adapter.NotesAdapter
import com.firebase.ui.auth.AuthUI
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    private lateinit var adapter: NotesAdapter
    private lateinit var binding: ActivityMainBinding

    override val viewModel: MainViewModel by viewModel()
    override val layout: View by lazy {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        binding.toolbarMain.inflateMenu(R.menu.main).let { true }

    private fun initViews() {
        setSupportActionBar(binding.toolbarMain)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> false
        }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout_dialog_title)
            .setMessage(R.string.logout_dialog_message)
            .setPositiveButton(R.string.dialog_ok) { _, _ -> onLogout() }
            .setNegativeButton(R.string.dialog_cancel) { d, _ -> d.dismiss() }
            .show()
    }

    private fun onLogout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }
}