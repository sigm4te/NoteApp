package com.example.noteapp.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityNoteBinding
import com.example.noteapp.mvvm.model.data.entity.Color
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewmodel.NoteViewModel
import com.example.noteapp.mvvm.viewstate.NoteViewState
import com.example.noteapp.utils.AndroidColorGetter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date
import java.util.Locale
import java.util.UUID

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

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
    private val colorGetter by lazy { AndroidColorGetter(binding.root.context) }

    override val viewModel: NoteViewModel by viewModel()
    override val layout: View by lazy {
        binding = ActivityNoteBinding.inflate(layoutInflater)
        binding.root
    }

    private var note: Note? = null
    private lateinit var color: Color
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
        colorPicker.onColorClickListener = {
            color = it
            toolbarNote.setBackgroundColor(colorGetter.getColorInt(it))
            saveNote()
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.deletionFlag) {
            finish()
            return
        }
        this.note = data.note
        this.color = note?.color ?: Color.WHITE
        initNote()
    }

    private fun initNote() {
        note?.let {
            binding.etNoteTitle.setTextKeepState(it.title)
            binding.etNoteText.setTextKeepState(it.text)
            supportActionBar?.title = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it.lastChanged)
            binding.toolbarNote.setBackgroundColor(colorGetter.getColorInt(it.color))
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
                lastChanged = Date(),
                color = color
            )
                ?: Note(
                    UUID.randomUUID().toString(),
                    etNoteTitle.text.toString(),
                    etNoteText.text.toString(),
                    color
                )
            note?.let {
                viewModel.save(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = binding.toolbarNote.inflateMenu(R.menu.note).let { true }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.palette -> togglePalette().let { true }
            R.id.delete -> deleteNote().let { true }
            android.R.id.home -> onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }

    private fun togglePalette() {
        with(binding.colorPicker) {
            if (isOpen) { close() } else { open() }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_dialog_message)
            .setPositiveButton(R.string.dialog_ok) { _, _ -> viewModel.deleteNote() }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}