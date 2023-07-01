package com.example.noteapp.ui.activity

import android.annotation.SuppressLint
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.mvvm.viewmodel.SplashViewModel
import com.example.noteapp.mvvm.viewstate.SplashViewState

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }
    override val layout = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({viewModel.requestUser()}, 1000)

    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}