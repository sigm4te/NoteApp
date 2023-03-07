package com.example.noteapp.ui.activity

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityNoteBinding
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewmodel.NoteViewModel
import java.util.*

class NoteActivity : AppCompatActivity() {

    companion object {
        private const val NOTE_KEY = "note"
        private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun start(context: Context, note: Note? = null) =
            Intent(context, NoteActivity::class.java).apply {
                putExtra(NOTE_KEY, note)
                context.startActivity(this)
            }
    }

    private lateinit var binding: ActivityNoteBinding
    private lateinit var viewModel: NoteViewModel

    private var note: Note? = null
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        note = intent.getParcelableExtra(NOTE_KEY)

        initViewModel()
        initViews()
        initListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        note?.let {
            binding.etNoteTitle.setText(it.title)
            binding.etNoteText.setText(it.text)
            supportActionBar?.title =
                SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it.lastChanged)
            val color = when (it.color) {
                Note.Color.WHITE -> R.color.white
                Note.Color.RED -> R.color.red
                Note.Color.GREEN -> R.color.green
                Note.Color.BLUE -> R.color.blue
                Note.Color.YELLOW -> R.color.yellow
                Note.Color.VIOLET -> R.color.violet
                Note.Color.PINK -> R.color.pink
            }
            binding.toolbarNote.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note)
        }
    }

    private fun initListeners() = with(binding) {
        etNoteTitle.addTextChangedListener(textWatcher)
        etNoteText.addTextChangedListener(textWatcher)
    }

    private fun saveNote() {
        with(binding) {
            if (etNoteText.length() < 3) return
            note = note?.copy(title = etNoteTitle.text.toString(), text = etNoteText.text.toString(), lastChanged = Date())
                ?: Note(UUID.randomUUID().toString(), etNoteTitle.text.toString(), etNoteText.text.toString())
            note?.let {
                viewModel.save(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}