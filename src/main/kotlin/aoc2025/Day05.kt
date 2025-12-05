package aoc2025

import readLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("D5P1: ${day5Part1()} = 3")
    println("D5P2: ${day5Part2()} = 14")
}

fun day5Part1() = readLines("aoc2025/day05.txt", delimiter = "\n\n")
    .let { (freshIngredients, ingredientIds) ->
        val freshIds = freshIngredients.parseIdRanges()
        ingredientIds.split("\n").map { it.toLong() }.count { id -> freshIds.any { id in it } }
    }

fun day5Part2() = readLines("aoc2025/day05.txt", delimiter = "\n\n")
    .let { (freshIngredients, _) ->
        var i = 0
        val ranges = freshIngredients.parseIdRanges().toMutableList()
        while (i < ranges.size - 1) {
            val a = ranges[i]
            for (j in i + 1 until ranges.size) {
                val b = ranges[j]
                if (a.last >= b.first && a.first <= b.last) { // Do ranges overlap?
                    ranges[i] = LongRange(min(a.first, b.first), max(a.last, b.last)) // Collapse ranges
                    ranges.removeAt(j)
                    i--
                    break
                }
            }
            i++
        }
        ranges.sumOf { it.last - it.first + 1 }
    }

fun String.parseIdRanges() = split("\n").map { it.split("-").let { it[0].toLong() .. it[1].toLong() } }