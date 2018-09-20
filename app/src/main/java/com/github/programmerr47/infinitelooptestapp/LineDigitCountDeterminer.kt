package com.github.programmerr47.infinitelooptestapp

import android.graphics.Typeface
import android.text.TextPaint
import android.view.View

class LineDigitCountDeterminer(
        private val textSize: Float
) {
    private val textPaint: TextPaint by lazy {
        TextPaint().also {
            it.textSize = textSize
            it.typeface = Typeface.MONOSPACE
        }
    }

    private val approximateSymbolWidth: Float by lazy { textPaint.measureText("0") }
    val typeface: Typeface? get() = textPaint.typeface

    fun determine(view: View, listener: (Int) -> Unit) {
        view.afterMeasured { determine(view.measuredWidth, listener) }
    }

    private fun determine(globalWidth: Int, listener: (Int) -> Unit) {
        val count = globalWidth.toFloat() / approximateSymbolWidth
        listener(count.toInt())
    }
}