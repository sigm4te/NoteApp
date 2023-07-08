package com.example.noteapp.ui.activity

import android.annotation.SuppressLint
import android.os.Handler
import com.example.noteapp.mvvm.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<Boolean?>() {

    override val viewModel: SplashViewModel by viewModel()
    override val layout = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, 1000)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}