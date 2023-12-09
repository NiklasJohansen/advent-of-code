package aoc2023

import readLines

fun main() {
    println("D9P1: ${day9Part1()}")
    println("D9P2: ${day9Part2()}")
}

fun day9Part1() = readLines("aoc2023/day09.txt")
    .sumOf { it.findExtrapolatedValue() }

fun day9Part2() = readLines("aoc2023/day09.txt")
    .sumOf { it.findExtrapolatedValue(backwards = true) }

private fun String.findExtrapolatedValue(backwards: Boolean = false): Int {
    val sequences = mutableListOf(split(" ").map { it.toInt() })
    while (sequences.last().any { it != 0 })
        sequences += sequences.last().zipWithNext { first, second -> second - first }
    return sequences
        .asReversed()
        .map { values -> if (backwards) values.first() else values.last() }
        .reduce { lastExtrapolatedValue, seqValue -> seqValue + lastExtrapolatedValue * (if (backwards) -1 else 1) }
}