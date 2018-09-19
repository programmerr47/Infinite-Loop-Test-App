package com.github.programmerr47.infinitelooptestapp

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
        while (true) {
            saver.store(piGenerator.generateDigit(curIndex))
            curIndex++
        }
    }
}

class BackgroundInfiniteGenerator(
        private val origin: InfiniteGenerator
) : InfiniteGenerator {
    private val executor = Executors.newSingleThreadExecutor()

    override fun startGenerating() {
        executor.execute {
            origin.startGenerating()
        }
    }
}