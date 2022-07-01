package de.app.ui.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

fun <T> LifecycleOwner.observe(data: MutableLiveData<T>, observer: T.() -> Unit){
    data.observe(this) { observer(it) }
}

fun <T> LifecycleOwner.observe(data: MutableLiveData<Result<T>>,
                               onSuccess: (T) -> Unit,
                               onFail: (Throwable) -> Unit){
    observe(data){ fold(onSuccess, onFail) }
}

class LifecycleObserver(
    private val onCreateObserver: (LifecycleOwner) -> Unit
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        onCreateObserver(owner)
    }
}