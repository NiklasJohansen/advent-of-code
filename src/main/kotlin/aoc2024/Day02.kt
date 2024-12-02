package aoc2024

import readLines
import kotlin.math.abs

fun main() {
    println("D2P1: ${day2Part1()}")
    println("D2P2: ${day2Part2()}")
}

fun day2Part1() = readLines("aoc2024/day02.txt")
    .parseReports()
    .count { it.isSafe() }

fun day2Part2() = readLines("aoc2024/day02.txt")
    .parseReports()
    .count { it.isSafe() || it.indices.any { i -> it.toMutableList().apply { removeAt(i) }.isSafe() } }

fun List<String>.parseReports() = map { it.split(" ").map { level -> level.toInt() } }

fun List<Int>.isSafe() = zipWithNext().let {
    (it.all { (a,b) -> a > b } || it.all { (a,b) -> a < b }) &&
    it.all { (a,b) -> abs(a - b) in 1..3 }
}