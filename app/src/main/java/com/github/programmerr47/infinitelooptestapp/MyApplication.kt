package com.github.programmerr47.infinitelooptestapp

import android.app.Application
import android.os.Handler
import android.os.Looper

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        globalLocator = ServiceLocator()
    }

    companion object {
        lateinit var globalLocator: ServiceLocator private set
    }
}

class ServiceLocator {
    val uiHandler = Handler(Looper.getMainLooper())
    val piGenerator: PiGenerator by lazy { RandomPiGenerator() }
    val memoryStorage: MemoryStorage by lazy { MemoryStorage() }
}