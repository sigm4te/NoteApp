package com.example.noteapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.utils.AndroidColorGetter

class NotesAdapter(val onItemClick: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    inner class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        private val colorGetter by lazy { AndroidColorGetter(binding.root.context) }

        fun bind(note: Note) = with(binding) {
            tvItemTitle.text = note.title
            tvItemBody.text = note.text
            root.setCardBackgroundColor(colorGetter.getColorInt(note.color))
            root.setOnClickListener { onItemClick?.invoke(note) }
        }
    }
}