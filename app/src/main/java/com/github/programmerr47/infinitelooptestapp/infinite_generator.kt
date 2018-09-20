package com.github.programmerr47.infinitelooptestapp

import android.os.Process
import java.util.concurrent.Executors

interface InfiniteGenerator {
    fun startGenerating()
    fun stopGenerating()
}

class PiInfiniteGenerator(
        private val piGenerator: PiGenerator,
        private val buffer: FixedBuffer
) : InfiniteGenerator {
    private var curIndex = 0L

    override fun startGenerating() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                buffer.store(piGenerator.generateDigit(curIndex))
                curIndex++
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun stopGenerating() {
        Thread.currentThread().interrupt()
    }
}

class BackgroundInfiniteGenerator(
        private val origin: InfiniteGenerator
) : InfiniteGenerator {
    private val executor = Executors.newSingleThreadExecutor()

    override fun startGenerating() {
        executor.execute {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            origin.startGenerating()
        }
    }

    override fun stopGenerating() {
        executor.shutdown()
    }
}