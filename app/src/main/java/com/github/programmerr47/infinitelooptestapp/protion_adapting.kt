package com.github.programmerr47.infinitelooptestapp

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MemoryStorage : GenListener {

    @Volatile
    var memStorage = ""
        private set

    @Synchronized
    override fun onNewPortion(portion: List<Int>) {
        memStorage += portion.joinToString(separator = "")
    }
}

class PortionAdapter(
        private val memoryStorage: MemoryStorage,
        private val uiHandler: Handler,
        private val determiner: LineDigitCountDeterminer,
        private val defStrMaxLength: Int = 150
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), GenListener{
    private var list = mutableListOf("")
    private var lastIndex = 0

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as? TextView)?.run {
            text = list[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PortionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_portion, parent, false).apply {
            (this as? TextView)?.typeface = determiner.typeface
        })
    }

    override fun onNewPortion(portion: List<Int>) {
        val addedStr = if (lastIndex > 0) memoryStorage.memStorage.substring(lastIndex) else memoryStorage.memStorage

        val stored = list[list.lastIndex]
        this.lastIndex += addedStr.length

        if (stored.length < defStrMaxLength) {
            if (stored.length + addedStr.length <= defStrMaxLength) {
                change(stored + addedStr)
            } else {
                val splitIndex = defStrMaxLength - stored.length
                val leftPart = addedStr.substring(0, splitIndex)
                val rightPart = addedStr.substring(splitIndex)

                change(stored + leftPart)
                addParts(rightPart)
            }
        } else {
            addParts(addedStr)
        }
    }

    private fun change(newStr: String) {
        list[list.lastIndex] = newStr
        notifyItemChanged(list.lastIndex)
    }

    private fun addParts(str: String) {
        val startIndex = list.size
        val newStrs = str.chunked(defStrMaxLength)
        list.addAll(newStrs)
        notifyItemRangeInserted(startIndex, newStrs.size)
    }

    private fun postOnUi(block: () -> Unit) = uiHandler.post(block)

    private class PortionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}