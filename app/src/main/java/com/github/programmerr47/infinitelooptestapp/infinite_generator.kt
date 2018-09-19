package com.github.programmerr47.infinitelooptestapp

import android.os.Process
import java.util.concurrent.Executors

interface InfiniteGenerator {
    fun startGenerating()
}

class PiInfiniteGenerator(
        private val piGenerator: PiGenerator,
        private val saver: Saver
) : InfiniteGenerator {
    private var curIndex = 0L

    override fun startGenerating() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                saver.store(piGenerator.generateDigit(curIndex))
                curIndex++
                Thread.sleep(1)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
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
}