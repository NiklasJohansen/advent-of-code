package aoc2024

import readText
import kotlin.math.log10

fun main() {
    println("D11P1: ${day11Part1()} = 55312")
    println("D11P2: ${day11Part2()} = 65601038650482")
}

fun day11Part1() = readText("aoc2024/day11.txt")
    .parseStoneArrangement()
    .sumOf { stone -> getStoneCount(stone, blinks = 25) }

fun day11Part2() = readText("aoc2024/day11.txt")
    .parseStoneArrangement()
    .sumOf { stone -> getStoneCount(stone, blinks = 75) }

private fun String.parseStoneArrangement() = split(" ").map { it.toLong() }

private fun getStoneCount(stone: Long, blinks: Int, cache: MutableMap<Key, Long> = mutableMapOf<Key, Long>()): Long {
    if (blinks == 0)
        return 1

    val key = Key(stone, blinks)
    cache[key]?.let { return it }

    val count = if (stone == 0L) {
        getStoneCount(1L, blinks - 1, cache)
    } else if ((log10(stone.toDouble()).toLong() + 1L) % 2L == 0L) {  // Even digits
        val num = stone.toString()
        val left = num.take(num.length / 2).toLong()
        val right = num.drop(num.length / 2).toLong()
        getStoneCount(left, blinks - 1, cache) + getStoneCount(right, blinks - 1, cache)
    } else {
        getStoneCount(stone * 2024, blinks - 1, cache)
    }

    return count.also { cache[key] = it }
}

private data class Key(val stone: Long, val blinks: Int)