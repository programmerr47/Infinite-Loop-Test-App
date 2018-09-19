package com.github.programmerr47.infinitelooptestapp

import android.util.Log

interface Saver {
    fun store(digit: Int)
}

class Storage : Saver {
    private val buffer: MutableList<Int> = ArrayList()

    @Synchronized
    fun popSnapshot(): List<Int> {
        val result = ArrayList(buffer)
        buffer.clear()
        return result
    }

    @Synchronized
    override fun store(digit: Int) {
        buffer.add(digit)
    }
}

class TempStorage(
        private val storage: Storage,
        private val listener: GenListener,
        private val defThresholdNs: Long = DEF_THRESHOLD_NS
) : Saver {
    private var curTimeNs = System.nanoTime()

    override fun store(digit: Int) {
        storage.store(digit)

        val newCurTime = System.nanoTime()
//        Log.v("MYGENERATOR", "threshold = ${newCurTime - curTimeNs} but def = $defThresholdNs")
        if (newCurTime - curTimeNs > defThresholdNs) {
//            Log.v("MYGENERATOR", "Hell yeah, we will get new snapshot")
            curTimeNs = newCurTime
            listener.onNewPortion(storage.popSnapshot())
        }
    }

    companion object {
        private const val DEF_THRESHOLD_NS = 20_000_000L //20ms
    }
}