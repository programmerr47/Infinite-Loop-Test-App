package com.github.programmerr47.infinitelooptestapp

import android.os.Process
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.*

class PeriodicStorageChecker(
        private val fixedBuffer: FixedBuffer,
        private val listener: GenListener,
        private val defThresholdMs: Long = DEF_THRESHOLD_MS
) {
    private val timer: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        timer.scheduleAtFixedRate({
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            listener.onNewPortion(fixedBuffer.popSnapshot())
        }, 0, defThresholdMs, MILLISECONDS)
    }

    fun stop() {
        timer.shutdown()
    }

    companion object {
        private const val DEF_THRESHOLD_MS = 40L
    }
}