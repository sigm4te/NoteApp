package com.example.noteapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.R
import com.example.noteapp.mvvm.model.error.NoAuthException
import com.example.noteapp.mvvm.viewmodel.BaseViewModel
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<S> : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + Job() }
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job

    abstract val viewModel: BaseViewModel<S>
    abstract val layout: View?

    companion object {
        private const val RC_SIGN_IN = 333
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout?.let { setContentView(it) }
    }

    override fun onStart() {
        super.onStart()
        dataJob = launch {
            viewModel.getViewState().consumeEach { renderData(it) }
        }
        errorJob = launch {
            viewModel.getError().consumeEach { renderError(it) }
        }
    }

    abstract fun renderData(data: S)

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) { finish() }
    }

    override fun onStop() {
        dataJob.cancel()
        errorJob.cancel()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }
}