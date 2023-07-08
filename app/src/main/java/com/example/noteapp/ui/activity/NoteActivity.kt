package com.example.noteapp.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityNoteBinding
import com.example.noteapp.mvvm.model.data.entity.Color
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewmodel.NoteViewModel
import com.example.noteapp.utils.AndroidColorGetter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date
import java.util.Locale
import java.util.UUID

class NoteActivity : BaseActivity<Pair<Note?, Boolean>>() {

    companion object {
        private const val NOTE_KEY = "note"
        private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).apply {
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

    private var currentNote: Note? = null
    private var currentColor: Color = Color.WHITE
    private var editedFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra(NOTE_KEY)?.let { viewModel.loadNote(it) } ?: newNote()

        initViews()
        initListeners()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setToolbarColor(currentColor)
    }

    override fun renderData(data: Pair<Note?, Boolean>) {
        if (data.second) {
            finish()
            return
        }
        currentNote = data.first
        currentColor = currentNote?.color ?: Color.WHITE
        initNote()
    }

    private fun initNote() {
        currentNote?.let {
            binding.etNoteTitle.setTextKeepState(it.title)
            binding.etNoteText.setTextKeepState(it.text)
            setActionbarText(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it.lastChanged))
            setToolbarColor(it.color)
            editedFlag = false
        } ?: newNote()
    }

    private fun initListeners() = with(binding) {
        etNoteTitle.addTextChangedListener { editedFlag = true }
        etNoteText.addTextChangedListener { editedFlag = true }
        colorPicker.onColorClickListener = {
            currentColor = it
            setToolbarColor(currentColor)
            colorPicker.close()
            editedFlag = true
        }
    }

    private fun newNote() {
        setActionbarText(getString(R.string.new_note))
    }

    private fun setToolbarColor(color: Color) {
        binding.toolbarNote.setBackgroundColor(colorGetter.getColorInt(color))
    }

    private fun setActionbarText(text: String) {
        supportActionBar?.title = text
    }

    private fun saveNote() {
        with(binding) {
            launch {
                currentNote = currentNote?.copy(
                    title = etNoteTitle.text.toString(),
                    text = etNoteText.text.toString(),
                    lastChanged = Date(),
                    color = currentColor
                ) ?: Note(
                    UUID.randomUUID().toString(),
                    etNoteTitle.text.toString(),
                    etNoteText.text.toString(),
                    currentColor
                )
                currentNote?.let { viewModel.saveChanges(it) }
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

    private fun togglePalette() = with(binding.colorPicker) {
        if (isOpen) close() else open()
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_dialog_message)
            .setPositiveButton(R.string.dialog_ok) { _, _ -> viewModel.deleteNote() }
            .setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onBackPressed() {
        if (editedFlag) saveNote()
        super.onBackPressed()
    }
}