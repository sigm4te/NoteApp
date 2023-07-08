package com.example.noteapp.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.mvvm.model.data.NotesRepository
import com.example.noteapp.mvvm.model.data.Result
import com.example.noteapp.mvvm.model.data.entity.Note
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
/*
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val notesLiveData = MutableLiveData<Result>()

    private lateinit var viewModel: MainViewModel

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Success notes`() {
        var result: List<Note>? = null

        viewModel.getViewState().observeForever { state ->
            result = state.data
        }
        notesLiveData.value = Result.Success(data = testNotes)

        assertEquals(testNotes, result)
    }

    @Test
    fun `should return Error`() {
        var result: Throwable? = null
        val testError = Throwable("TestError")

        viewModel.getViewState().observeForever { state ->
            result = state.error
        }
        notesLiveData.value = Result.Error(error = testError)

        assertEquals(testError, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onClearedTesting()

        assertFalse(notesLiveData.hasObservers())
    }
*/
}