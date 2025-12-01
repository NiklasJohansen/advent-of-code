package aoc2025

import readLines
import kotlin.math.abs
import kotlin.math.sign
import kotlin.text.drop

fun main() {
    println("D1P1: ${day1Part1()} = 3")
    println("D1P2: ${day1Part2()} = 6")
}

fun day1Part1() = readLines("aoc2025/day01.txt")
    .parseRotations()
    .getPassword(countTotalZeros = false)

fun day1Part2() = readLines("aoc2025/day01.txt")
    .parseRotations()
    .getPassword(countTotalZeros = true)

fun List<String>.parseRotations() = map { it.drop(1).toInt() * (if (it[0] == 'L') -1 else 1) }

fun List<Int>.getPassword(countTotalZeros: Boolean): Int {
    var dial = 50
    return sumOf { rotations ->
        val zeroCount = (0 until abs(rotations)).count {
            dial += rotations.sign
            dial += if (dial < 0) 100 else if (dial > 99) -100 else 0
            dial == 0
        }
        if (countTotalZeros) zeroCount else if (dial == 0) 1 else 0
    }
}