package com.github.programmerr47.infinitelooptestapp

import java.util.*
import kotlin.math.abs

interface PiGenerator {
    fun generateDigit(index: Long): Int
}

class RandomPiGenerator : PiGenerator {
    private val rnd = Random()

    override fun generateDigit(index: Long) = abs(rnd.nextInt() % 10)
}