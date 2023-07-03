package com.example.noteapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.noteapp.R
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.example.noteapp.mvvm.viewmodel.BaseViewModel
import com.example.noteapp.mvvm.viewstate.BaseViewState
import com.firebase.ui.auth.AuthUI

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 333
    }

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layout: View?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout?.let { setContentView(it) }
        viewModel.getViewState().observe(this, Observer { state ->
            state ?: return@Observer
            state.error?.let {
                renderError(it)
                return@Observer
            }
            renderData(state.data)
        })
    }

    private fun startLogin() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
        )
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.ic_launcher_foreground)
            .setTheme(R.style.Theme_NoteApp)
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }
}