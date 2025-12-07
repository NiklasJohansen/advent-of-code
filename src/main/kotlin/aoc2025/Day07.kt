package aoc2025

import readLines

fun main() {
    println("D7P1: ${day7Part1()} = 21")
    println("D7P2: ${day7Part2()} = 40")
}

fun day7Part1() = readLines("aoc2025/day07.txt", delimiter = "\n")
    .let { map -> Counter(0).also { map.countTimelines(x = map[0].indexOf('S'), y = 1, it) }.numSplits }

fun day7Part2() = readLines("aoc2025/day07.txt", delimiter = "\n")
    .let { map -> map.countTimelines(x = map[0].indexOf('S'), y = 1, Counter()) }

fun List<String>.countTimelines(x: Int, y: Int, counter: Counter, cache: MutableMap<Int, Long> = mutableMapOf()): Long {
    if (y >= size) return 1 // Reached bottom

    val key = x * 1000 + y
    cache[key]?.let { return it }

    val numTimelines = when (this[y][x]) {
        '.' -> countTimelines(x, y + 1, counter, cache)
        '^' -> {
            counter.numSplits++
            countTimelines(x - 1, y + 1, counter, cache) + countTimelines(x + 1, y + 1, counter, cache)
        }
        else -> 0
    }

    return numTimelines.also { cache[key] = it }
}

data class Counter(var numSplits: Int = 0)