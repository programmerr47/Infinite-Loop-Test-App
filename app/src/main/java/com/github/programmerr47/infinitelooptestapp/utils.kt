package com.github.programmerr47.infinitelooptestapp

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewTreeObserver
import java.util.concurrent.BlockingQueue

fun <T : View> Activity.bindable(@IdRes id: Int) = lazy { findViewById<T>(id) }

fun <T> BlockingQueue<T>.blockingDrainTo(collection: MutableCollection<T>) {
    collection.add(take())
    drainTo(collection)
}

inline fun <T : View> T.afterMeasured(crossinline action: T.() -> Unit) {
    afterMeasuredInner({ measuredWidth > 0 && measuredHeight > 0 }, action)
}

inline fun <T : View> T.afterMeasuredInner(crossinline predicate: T.() -> Boolean, crossinline action: T.() -> Unit) {
    if (!predicate()) {
        addSmartGlobalLayoutListener(predicate, action)
    } else {
        action()
    }
}

inline fun <T : View> T.addSmartGlobalLayoutListener(crossinline predicate: T.() -> Boolean, crossinline action: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (predicate()) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                action()
            }
        }
    })
}