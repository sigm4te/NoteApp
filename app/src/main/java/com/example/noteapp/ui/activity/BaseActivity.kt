package com.example.noteapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.noteapp.mvvm.viewmodel.BaseViewModel
import com.example.noteapp.mvvm.viewstate.BaseViewState

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        viewModel.getViewState().observe(this, Observer { state ->
            state ?: return@Observer
            state.error?.let {
                renderError(it)
                return@Observer
            }
            renderData(state.data)
        })
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        error.message?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }
}