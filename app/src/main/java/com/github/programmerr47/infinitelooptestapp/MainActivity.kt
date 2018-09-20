package com.github.programmerr47.infinitelooptestapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), GenListener {
    private val locator: ServiceLocator by lazy { MyApplication.globalLocator }
    private var module = Module(locator, { this }, this)

    private val rvList: RecyclerView by bindable(R.id.rv_list)
    private val bStart: Button by bindable(R.id.b_start)
    private val bStop: Button by bindable(R.id.b_stop)
    private val tvCounter: TextView by bindable(R.id.tv_counter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepare()

        bStart.setOnClickListener {
            enableBStop()

            module.storageChecker.start()
            module.infiniteGenerator.startGenerating()
        }

        bStop.setOnClickListener {
            enableBStart()

            module.storageChecker.stop()
            module.infiniteGenerator.stopGenerating()

            module = Module(locator, { this }, this)
            prepare()
        }
    }

    private fun prepare() {
        disableButtons()
        tvCounter.text = "0"
        startDeterminingDigitCount {
            enableBStart()
        }
    }

    private inline fun startDeterminingDigitCount(crossinline endAction: () -> Unit) {
        module.lineDigitCountDeterminer.determine(rvList) {
            module.digitCountPerLine = it
            rvList.adapter = module.adapter
            endAction()
        }
    }

    private fun enableBStart() {
        bStart.isEnabled = true
        bStop.isEnabled = false
    }

    private fun enableBStop() {
        bStart.isEnabled = false
        bStop.isEnabled = true
    }

    private fun disableButtons() {
        bStart.isEnabled = false
        bStop.isEnabled = false
    }

    override fun onNewPortion(portion: List<Int>) {
        tvCounter.text = "" + module.memoryStorage.memStorage.length
    }

    private class Module(locator: ServiceLocator,
                         contextFactory: () -> Context,
                         genListener: GenListener) {
        var digitCountPerLine = 3 // some def value

        val memoryStorage: MemoryStorage by lazy { MemoryStorage() }
        val lineDigitCountDeterminer: LineDigitCountDeterminer by lazy {
            LineDigitCountDeterminer(contextFactory().resources.getDimension(R.dimen.text_size))
        }
        val adapter: PortionAdapter by lazy { PortionAdapter(memoryStorage, locator.uiHandler, lineDigitCountDeterminer, digitCountPerLine * 4) }
        val tvListener: GenListener by lazy { UiGenListener(genListener, locator.uiHandler) }
        val globalListener: GenListener by lazy { GenListenerCompositor(memoryStorage, UiGenListener(adapter, locator.uiHandler), tvListener) }
        val buffer: FixedBuffer by lazy { FixedBuffer() }
        val infiniteGenerator: InfiniteGenerator by lazy { BackgroundInfiniteGenerator(PiInfiniteGenerator(locator.piGenerator, buffer)) }
        val storageChecker: PeriodicStorageChecker by lazy { PeriodicStorageChecker(buffer, globalListener) }
    }
}


