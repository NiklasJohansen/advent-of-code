package aoc2023

import readLines

fun main() {
    println("D1P1: ${day1Part1()}")
    println("D1P2: ${day1Part2()}")
}

fun day1Part1() = readLines("aoc2023/day01-1.txt")
    .sumOf { line ->
        val first = line.first { it.isDigit() }.digitToInt()
        val last = line.last { it.isDigit() }.digitToInt()
        first * 10 + last
    }

fun day1Part2() = readLines("aoc2023/day01-2.txt")
    .sumOf { line ->
        val first = digits.minBy { line.indexOf(it.key).takeIf { it >= 0 } ?: 1000 }.value
        val last = digits.maxBy { line.lastIndexOf(it.key) }.value
        first * 10 + last
    }

val digits = mapOf(
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)
