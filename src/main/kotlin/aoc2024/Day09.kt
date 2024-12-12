package aoc2024

import readText
import kotlin.math.abs

fun main() {
    println("D9P1: ${day9Part1()} = 1928")
    println("D9P2: ${day9Part2()} = 2858")
}

fun day9Part1() = readText("aoc2024/day09.txt")
    .createDiskMap()
    .compactByBlock()
    .calculateChecksum()

fun day9Part2() = readText("aoc2024/day09.txt")
    .createDiskMap()
    .compactByFile()
    .calculateChecksum()

private fun String.createDiskMap() =
    this.flatMapIndexed { i, c -> (0 until c.digitToInt()).map { if (i % 2 == 0) i / 2L else FREE }}
        .toMutableList()

private fun MutableList<Long>.compactByBlock(): MutableList<Long> {
    while (true) {
        val start = indexOf(FREE)
        val end = indexOfLast { it != FREE }
        if (start >= end) break
        this[start] = this[end]
        this[end] = FREE
    }
    return this
}

private fun MutableList<Long>.compactByFile(): MutableList<Long> {
    var fileEnd = lastIndex
    while (fileEnd > 0) {

        // Find file start and end
        var fileStart = fileEnd
        while (fileStart > 0 && this[fileStart] == this[fileEnd]) fileStart--
        val fileSize = fileEnd - fileStart
        fileEnd = fileStart++

        // Find free start and end index
        var freeStart = indexOf(FREE)
        var freeEnd = freeStart
        while (freeStart < fileStart) {

            // Find free end
            while (freeEnd < fileStart && this[freeEnd] == FREE) freeEnd++
            if (freeEnd - freeStart >= fileSize)
                break // Found space for file

            // Search for next free start
            freeStart = freeEnd
            while (freeStart < fileStart && this[freeStart] != FREE) freeStart++

            freeEnd = freeStart
        }

        // Move file to free space
        if (fileSize <= freeEnd - freeStart) {
            for (i in 0 until fileSize) {
                this[freeStart + i] = this[fileStart + i]
                this[fileStart + i] = FREE
            }
        }
    }

    return this
}

private fun MutableList<Long>.calculateChecksum() = indices.sumOf { it * (if(this[it] == FREE) 0 else abs(this[it])) }

private const val FREE = -1L