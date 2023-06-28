package com.example.noteapp.mvvm.model.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreProvider : DataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<Result> {
        val result = MutableLiveData<Result>()
        notesReference.addSnapshotListener { snapshot, error ->
            error?.let {
                result.value = Result.Error(it)
            } ?: snapshot?.let {
                val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                result.value = Result.Success(notes)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<Result> {
        val result = MutableLiveData<Result>()
        notesReference.document(id).get()
            .addOnSuccessListener { snapshot ->
                val note = snapshot.toObject(Note::class.java)
                result.value = Result.Success(note)
            }.addOnFailureListener { error ->
                result.value = Result.Error(error)
            }
        return result
    }

    override fun saveNote(note: Note): LiveData<Result> {
        val result = MutableLiveData<Result>()
        notesReference.document(note.id).set(note)
            .addOnSuccessListener {
                result.value = Result.Success(note)
            }.addOnFailureListener { error ->
                result.value = Result.Error(error)
            }
        return result
    }
}