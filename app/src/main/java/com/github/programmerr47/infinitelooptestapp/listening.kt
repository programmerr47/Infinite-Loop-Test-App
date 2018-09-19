package com.github.programmerr47.infinitelooptestapp

import android.os.Handler
import android.util.Log

interface GenListener {
    fun onNewPortion(portion: List<Int>)
}

class UiGenListener(
        private val origin: GenListener,
        private val uiHandler: Handler
) : GenListener {
    override fun onNewPortion(portion: List<Int>) {
        uiHandler.post { origin.onNewPortion(portion) }
    }
}

class LogGenListener(
        private val origin: GenListener
) : GenListener {
    override fun onNewPortion(portion: List<Int>) {
        val startMarker = System.nanoTime()
        origin.onNewPortion(portion)
        val diffNano = System.nanoTime() - startMarker
        Log.v("MYGENERATOR", "LogGenListener.onNewPortion is finished. Length (ms) = ${diffNano / 1_000_000}")
    }
}