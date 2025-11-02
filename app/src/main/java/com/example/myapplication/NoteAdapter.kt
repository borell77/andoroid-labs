package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.domain.Note

class NoteAdapter(
    private val onItemClicked: (Note) -> Unit,
    private val onDeleteClicked: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notes: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    fun submitList(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewNoteTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewNoteDescription)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteNote)

        fun bind(note: Note) {
            titleTextView.text = note.title
            descriptionTextView.text = note.description

            // Обработчик для кнопки удаления
            deleteButton.setOnClickListener {
                onDeleteClicked(note)
            }

            // Обработчик для редактирования
            itemView.setOnClickListener {
                onItemClicked(note)
            }
        }
    }
}
