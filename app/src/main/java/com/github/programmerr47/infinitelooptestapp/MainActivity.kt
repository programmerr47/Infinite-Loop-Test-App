package com.github.programmerr47.infinitelooptestapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), GenListener {
    private val locator: ServiceLocator by lazy { MyApplication.globalLocator }
    private val listener: GenListener by lazy { UiGenListener(LogGenListener(this), locator.uiHandler) }
    private val storage: Saver by lazy { TempStorage(Storage(), listener) }
    private val infiniteGenerator: InfiniteGenerator by lazy { BackgroundInfiniteGenerator(PiInfiniteGenerator(locator.piGenerator, storage)) }

    private val tvText: TextView by lazy { findViewById<TextView>(R.id.tv_text) }
    private val bStart: Button by lazy { findViewById<Button>(R.id.b_start) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvText.text = ""
        bStart.setOnClickListener {
            infiniteGenerator.startGenerating()
        }
    }

    override fun onNewPortion(portion: List<Int>) {
        val portionStr = portion.joinToString(separator = "")
        Log.v("MYGENERATOR", "[${Thread.currentThread()}] Portion string = $portionStr")
        tvText.text = tvText.text.toString() + portionStr
    }
}


