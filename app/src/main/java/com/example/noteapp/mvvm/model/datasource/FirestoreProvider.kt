package com.example.noteapp.mvvm.model.datasource

import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.data.entity.User
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreProvider(private val auth: FirebaseAuth, private val store: FirebaseFirestore) : DataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val currentUser
        get() = auth.currentUser
    private val notesCollection
        get() = currentUser?.let { store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION) }
            ?: throw NoAuthException()

    override fun subscribeToAllNotes(): ReceiveChannel<Result> = Channel<Result>(Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null
        try {
            registration = notesCollection.addSnapshotListener { snapshot, error ->
                val value = error?.let {
                    Result.Error(it)
                } ?: snapshot?.let {
                    val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                    Result.Success(notes)
                }
                value?.let { trySend(it) }
            }
        } catch (e: Throwable) {
            trySend(Result.Error(e))
        }
        invokeOnClose { registration?.remove() }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(currentUser?.let { User(it.displayName ?: "", it.email ?: "") })
    }

    override suspend fun getNoteById(id: String): Note? = suspendCoroutine { continuation ->
        try {
            notesCollection.document(id).get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(snapshot.toObject(Note::class.java))
                }.addOnFailureListener { error ->
                    continuation.resumeWithException(error)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            notesCollection.document(note.id).set(note)
                .addOnSuccessListener {
                    continuation.resume(note)
                }.addOnFailureListener { error ->
                    continuation.resumeWithException(error)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNote(id: String) = suspendCoroutine { continuation ->
        try {
            notesCollection.document(id).delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { error ->
                    continuation.resumeWithException(error)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }
}