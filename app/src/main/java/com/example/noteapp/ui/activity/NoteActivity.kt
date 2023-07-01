package com.example.noteapp.ui.activity

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityNoteBinding
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewmodel.NoteViewModel
import com.example.noteapp.mvvm.viewstate.NoteViewState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date
import java.util.Locale
import java.util.UUID

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private const val NOTE_KEY = "note"
        private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun start(context: Context, noteId: String? = null) =
            Intent(context, NoteActivity::class.java).apply {
                putExtra(NOTE_KEY, noteId)
                context.startActivity(this)
            }
    }

    private lateinit var binding: ActivityNoteBinding

    override val viewModel: NoteViewModel by viewModel()
    override val layout: View by lazy {
        binding = ActivityNoteBinding.inflate(layoutInflater)
        binding.root
    }

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

        val noteId = intent.getStringExtra(NOTE_KEY)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: newNote()

        initViews()
        initListeners()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initListeners() = with(binding) {
        etNoteTitle.addTextChangedListener(textWatcher)
        etNoteText.addTextChangedListener(textWatcher)
    }

    override fun renderData(data: Note?) {
        this.note = data
        initNote()
    }

    private fun initNote() {
        note?.let {
            binding.etNoteTitle.setTextKeepState(it.title)
            binding.etNoteText.setTextKeepState(it.text)
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
        } ?: newNote()
    }

    private fun newNote() {
        supportActionBar?.title = getString(R.string.new_note)
    }

    private fun saveNote() {
        with(binding) {
            if (etNoteText.length() < 3) return
            note = note?.copy(
                title = etNoteTitle.text.toString(),
                text = etNoteText.text.toString(),
                lastChanged = Date()
            )
                ?: Note(
                    UUID.randomUUID().toString(),
                    etNoteTitle.text.toString(),
                    etNoteText.text.toString()
                )
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