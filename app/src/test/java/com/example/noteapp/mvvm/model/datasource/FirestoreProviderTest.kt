package com.example.noteapp.mvvm.model.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTest {
/*
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockResultCollection = mockk<CollectionReference>()

    private val provider: FirestoreProvider = FirestoreProvider(mockAuth, mockDb)

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))
    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    @Before
    fun setup() {
        clearAllMocks()
        every { mockUser.uid } returns ""
        every { mockAuth.currentUser } returns mockUser
        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockResultCollection
        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if not auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null

        provider.subscribeToAllNotes().observeForever {
            result = (it as? Result.Error)?.error
        }

        assertTrue(result is NoAuthException)
    }

    @Test
    fun `saveNote calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        provider.saveNote(testNotes[0])

        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote return Success note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? Result.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)

        assertEquals(testNotes[0], result)
    }

    @Test
    fun `subscribeToAllNotes returns Success notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? Result.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns Error`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? Result.Error)?.error
        }
        slot.captured.onEvent(null, testError)

        assertEquals(testError, result)
    }
*/
}