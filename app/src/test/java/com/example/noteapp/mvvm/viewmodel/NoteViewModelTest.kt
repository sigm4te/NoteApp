package com.example.noteapp.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import com.example.noteapp.mvvm.viewstate.NoteViewState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val noteLiveData = MutableLiveData<Result>()

    private lateinit var viewModel: NoteViewModel

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNoteById(testNotes[0].id) } returns noteLiveData
        every { mockRepository.deleteNote(testNotes[0].id) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote returns Success note`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, testNotes[0])

        viewModel.getViewState().observeForever { state ->
            result = state.data
        }
        viewModel.loadNote(testNotes[0].id)
        noteLiveData.value = Result.Success(data = testNotes[0])

        assertEquals(testData, result)
    }

    @Test
    fun `loadNote returns Error note`() {
        var result: Throwable? = null
        val testError = Throwable("TestError")

        viewModel.getViewState().observeForever { state ->
            result = state.error
        }
        viewModel.loadNote(testNotes[0].id)
        noteLiveData.value = Result.Error(error = testError)

        assertEquals(testError, result)
    }

    @Test
    fun `deleteNote returns Success note data with deletion flag`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true, null)

        viewModel.getViewState().observeForever { state ->
            result = state.data
        }
        viewModel.save(testNotes[0])
        viewModel.deleteNote()
        noteLiveData.value = Result.Success(data = null)

        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote returns Error`() {
        var result: Throwable? = null
        val testError = Throwable("TestError")

        viewModel.getViewState().observeForever { state ->
            result = state.error
        }
        viewModel.save(testNotes[0])
        viewModel.deleteNote()
        noteLiveData.value = Result.Error(error = testError)

        assertEquals(testError, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onClearedTesting()

        assertFalse(noteLiveData.hasObservers())
    }
}