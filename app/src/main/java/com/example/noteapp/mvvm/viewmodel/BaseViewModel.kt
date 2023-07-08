package com.example.noteapp.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@OptIn(ObsoleteCoroutinesApi::class)
open class BaseViewModel<S> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Default + Job() }
    private val viewStateChannel = BroadcastChannel<S>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    fun getViewState(): ReceiveChannel<S> = viewStateChannel.openSubscription()

    fun getError(): ReceiveChannel<Throwable> = errorChannel

    protected fun setData(data: S) = launch {
        viewStateChannel.send(data)
    }

    protected fun setError(error: Throwable) = launch {
        errorChannel.send(error)
    }

    override fun onCleared() {
        viewStateChannel.cancel()
        errorChannel.close()
        coroutineContext.cancel()
        super.onCleared()
    }
}