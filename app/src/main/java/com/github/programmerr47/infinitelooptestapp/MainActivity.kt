package com.github.programmerr47.infinitelooptestapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), GenListener {
    private val locator: ServiceLocator by lazy { MyApplication.globalLocator }
    private val listener: GenListener by lazy { UiGenListener(LogGenListener(this), locator.uiHandler) }
    private val storage: Storage by lazy { Storage() }
    private val infiniteGenerator: InfiniteGenerator by lazy { BackgroundInfiniteGenerator(PiInfiniteGenerator(locator.piGenerator, storage)) }
    private val storageChecker: PeriodicStorageChecker by lazy { PeriodicStorageChecker(storage, listener) }

    private val tvText: TextView by bindable(R.id.tv_text)
    private val bStart: Button by bindable(R.id.b_start)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvText.text = ""
        bStart.setOnClickListener {
            storageChecker.start()
            infiniteGenerator.startGenerating()
        }
    }

    override fun onNewPortion(portion: List<Int>) {
        val portionStr = portion.joinToString(separator = "")
        tvText.append(portionStr)
    }
}


