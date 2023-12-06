package aoc2023

import readLines

fun main() {
    println("D6P1: ${day6Part1()}")
    println("D6P2: ${day6Part2()}")
}

fun day6Part1() = readLines("aoc2023/day06.txt")
    .parseRecordSheet()
    .getNumberOfWinsMultiplied()

fun day6Part2() = readLines("aoc2023/day06.txt")
    .parseRecordSheet(singleRace = true)
    .getNumberOfWinsMultiplied()

private fun RecordSheet.getNumberOfWinsMultiplied(): Long =
    this.times.mapIndexed { i, totalTime ->
        LongRange(0, totalTime).sumOf { holdTime ->
            val timeLeft = totalTime - holdTime
            val distance = holdTime * timeLeft
            if (distance > recordDistances[i]) 1L else 0L
        }
    }.reduce(Long::times)

private fun List<String>.parseRecordSheet(singleRace: Boolean = false): RecordSheet =
    this.map { if (singleRace) it.replace(" ", "") else it }
        .let { (times, distances) -> RecordSheet(times.toLongArray(), distances.toLongArray()) }

private fun String.toLongArray(): LongArray =
    this.split(":", " ").mapNotNull { it.toLongOrNull() }.toLongArray()

data class RecordSheet(val times: LongArray, val recordDistances: LongArray)