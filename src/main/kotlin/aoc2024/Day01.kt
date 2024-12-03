package aoc2024

import readLines
import kotlin.math.abs

fun main() {
    println("D1P1: ${day1Part1()} = 11")
    println("D1P2: ${day1Part2()} = 31")
}

fun day1Part1() = readLines("aoc2024/day01.txt")
    .parseNumbers()
    .let { (left, right) -> left.mapIndexed { i, num -> abs(num - right[i]) }.sum() }

fun day1Part2() = readLines("aoc2024/day01.txt")
    .parseNumbers()
    .let { (left, right) ->
        val rightNumCount = right.groupBy { it }.mapValues { it.value.size }
        left.sumOf { it * rightNumCount.getOrDefault(it, 0) }
    }

fun List<String>.parseNumbers() = Pair(
    map { it.substringBefore(" ").toInt() }.sorted(),
    map { it.substringAfterLast(" ").toInt() }.sorted()
)