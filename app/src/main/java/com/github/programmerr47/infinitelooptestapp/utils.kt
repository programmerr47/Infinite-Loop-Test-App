package com.github.programmerr47.infinitelooptestapp

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import java.util.concurrent.BlockingQueue

fun <T : View> Activity.bindable(@IdRes id: Int) = lazy { findViewById<T>(id) }

fun <T> BlockingQueue<T>.blockingDrainTo(collection: MutableCollection<T>) {
    collection.add(take())
    drainTo(collection)
}