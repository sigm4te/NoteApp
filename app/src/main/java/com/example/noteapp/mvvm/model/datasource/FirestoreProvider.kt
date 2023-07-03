package com.example.noteapp.mvvm.model.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.data.entity.User
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreProvider(private val auth: FirebaseAuth, private val store: FirebaseFirestore) : DataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser
    private val notesReference
        get() = currentUser?.let {
            store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }

    override fun subscribeToAllNotes(): LiveData<Result> = MutableLiveData<Result>().apply {
        try {
            notesReference.addSnapshotListener { snapshot, error ->
                error?.let {
                    value = Result.Error(it)
                } ?: snapshot?.let {
                    val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                    value = Result.Success(notes)
                }
            }
        } catch (e: Throwable) {
            value = Result.Error(e)
        }
    }

    override fun getNoteById(id: String): LiveData<Result> = MutableLiveData<Result>().apply {
        try {
            notesReference.document(id).get()
                .addOnSuccessListener { snapshot ->
                    val note = snapshot.toObject(Note::class.java)
                    value = Result.Success(note)
                }.addOnFailureListener { error ->
                    value = Result.Error(error)
                }
        } catch (e: Throwable) {
            value = Result.Error(e)
        }
    }

    override fun saveNote(note: Note): LiveData<Result> = MutableLiveData<Result>().apply {
        try {
            notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    value = Result.Success(note)
                }.addOnFailureListener { error ->
                    value = Result.Error(error)
                }
        } catch (e: Throwable) {
            value = Result.Error(e)
        }
    }

    override fun deleteNote(id: String): LiveData<Result> = MutableLiveData<Result>().apply {
        try {
            notesReference.document(id).delete()
                .addOnSuccessListener {
                    value = Result.Success(null)
                }.addOnFailureListener { error ->
                    value = Result.Error(error)
                }
        } catch (e: Throwable) {
            value = Result.Error(e)
        }
    }
}