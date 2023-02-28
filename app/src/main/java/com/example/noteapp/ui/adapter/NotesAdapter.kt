package com.example.noteapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.databinding.ItemNoteBinding

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) = with(binding) {
            tvItemTitle.text = note.title
            tvItemBody.text = note.text
            binding.root.setBackgroundColor(note.color)
        }
    }
}