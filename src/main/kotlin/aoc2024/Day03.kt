package aoc2024

import readText

fun main() {
    println("D3P1: ${day3Part1()} = 161")
    println("D3P2: ${day3Part2()} = 48")
}

fun day3Part1() = readText("aoc2024/day03-1.txt")
    .parseInstructions()
    .sumOf { it.multiply() }

fun day3Part2() = readText("aoc2024/day03-2.txt")
    .parseInstructions()
    .let { instructions ->
        var enabled = 1
        instructions.sumOf { if ("mul" in it) enabled * it.multiply() else { enabled = if ("don't" in it) 0 else 1; 0 } }
    }

fun String.parseInstructions() = "(mul[(]\\d+,\\d+[)])|(do[(])|(don't)".toRegex().findAll(this).map { it.value }

fun String.multiply() = split("(", ",", ")").takeIf { it.size >= 3 }?.let { it[1].toInt() * it[2].toInt() } ?: 0