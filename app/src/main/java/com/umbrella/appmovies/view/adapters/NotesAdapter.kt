package com.umbrella.appmovies.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.appmovies.databinding.ItemNoteBinding
import com.umbrella.appmovies.model.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {

    private var notes: List<Note> = ArrayList()
    private var onNoteLongClick: (Note) -> Unit = {}

    fun setOnNoteLongClickListener(onNoteLongClick: (Note) -> Unit) {
        this.onNoteLongClick = onNoteLongClick
    }

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class MyViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                textViewFilmTitle.text = note.filmTitle
                textViewDateOfWatching.text = note.dateOfWatching
                textViewReview.text = note.review
                root.setOnLongClickListener {
                    onNoteLongClick(note)
                    true
                }
            }
        }
    }
}