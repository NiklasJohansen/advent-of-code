package aoc2025

import readLines
import kotlin.math.max
import kotlin.math.pow

fun main() {
    println("D3P1: ${day3Part1()} = 357")
    println("D3P2: ${day3Part2()} = 3121910778619")
}

fun day3Part1() = readLines("aoc2025/day03.txt", delimiter = "\n")
    .sumOf { bank -> bank.getTotalJoltage(numBatteries = 2) }

fun day3Part2() = readLines("aoc2025/day03.txt", delimiter = "\n")
    .sumOf { bank -> bank.getTotalJoltage(numBatteries = 12) }

fun String.getTotalJoltage(numBatteries: Int, depth: Int = 0, cache: MutableMap<Int, Long> = mutableMapOf()): Long {
    if (numBatteries < 1 || depth + numBatteries > length)
        return 0

    val key = 100 * depth + numBatteries
    cache[key]?.let { return it }

    val thisBattery = this[depth].digitToInt() * 10.0.pow(numBatteries - 1).toLong()
    val maxTotalJoltage = max(
        getTotalJoltage(numBatteries - 1, depth + 1, cache) + thisBattery,
        getTotalJoltage(numBatteries,     depth + 1, cache)
    )

    return maxTotalJoltage.also { cache[key] = it }
}