package com.github.programmerr47.infinitelooptestapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity(), GenListener {
    private val locator: ServiceLocator by lazy { MyApplication.globalLocator }

    private val lineDigitCountDeterminer: LineDigitCountDeterminer by lazy {
        LineDigitCountDeterminer(resources.getDimension(R.dimen.text_size))
    }
    private val adapter: PortionAdapter by lazy { PortionAdapter(locator.memoryStorage, locator.uiHandler, lineDigitCountDeterminer, digitCountPerLine * 4) }
    private val tvListener: GenListener by lazy { UiGenListener(this, locator.uiHandler) }
    private val globalListener: GenListener by lazy { GenListenerCompositor(locator.memoryStorage, UiGenListener(adapter, locator.uiHandler)) }
    private val buffer: FixedBuffer by lazy { FixedBuffer() }
    private val infiniteGenerator: InfiniteGenerator by lazy { BackgroundInfiniteGenerator(PiInfiniteGenerator(locator.piGenerator, buffer)) }
    private val storageChecker: PeriodicStorageChecker by lazy { PeriodicStorageChecker(buffer, globalListener) }

    private val rvList: RecyclerView by bindable(R.id.rv_list)
    private val bStart: Button by bindable(R.id.b_start)

    private var digitCountPerLine = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startDeterminingDigitCount()

        bStart.setOnClickListener {
            storageChecker.start()
            infiniteGenerator.startGenerating()
        }
    }

    private fun startDeterminingDigitCount() {
        bStart.isEnabled = false
        lineDigitCountDeterminer.determine(rvList) {
            digitCountPerLine = it
            rvList.adapter = adapter
            bStart.isEnabled = true
        }
    }

    override fun onNewPortion(portion: List<Int>) {

    }
}


