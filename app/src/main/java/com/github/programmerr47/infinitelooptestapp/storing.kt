package com.github.programmerr47.infinitelooptestapp

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class Storage(
        defaultCapacity: Int = 100
) {
    private val buffer: BlockingQueue<Int> = ArrayBlockingQueue(defaultCapacity)

    fun popSnapshot(): List<Int> =
            ArrayList<Int>().also { buffer.blockingDrainTo(it) }

    fun store(digit: Int) {
        buffer.put(digit)
    }
}