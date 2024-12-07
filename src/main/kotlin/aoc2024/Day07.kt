package aoc2024

import readLines
import kotlin.collections.drop
import kotlin.collections.map
import kotlin.text.toLong

fun main() {
    println("D7P1: ${day7Part1()} = 3749")
    println("D7P2: ${day7Part2()} = 11387")
}

fun day7Part1() = readLines("aoc2024/day07.txt")
    .parseEquations()
    .filter { (testValue, equation) -> equation.isTrueFor(testValue) }
    .sumOf { it.first }

fun day7Part2() = readLines("aoc2024/day07.txt")
    .parseEquations()
    .filter { (testValue, equation) -> equation.isTrueFor(testValue, concatenate = true) }
    .sumOf { it.first }

fun List<String>.parseEquations() = map { it.split(" ", ":").let { it[0].toLong() to it.drop(2).map { it.toLong() } } }

fun List<Long>.isTrueFor(testValue: Long, i: Int = 0, operator: String? = null, total: Long = 0, concatenate: Boolean = false): Boolean {
    val newTotal = when (operator) {
        "*"  -> this[i] * total
        "+"  -> this[i] + total
        "||" -> "$total${this[i]}".toLong()
        else -> this[i]
    }

    if (i == lastIndex)
        return newTotal == testValue // Solution found if true

    return isTrueFor(testValue, i + 1, "*",  newTotal, concatenate) ||
           isTrueFor(testValue, i + 1, "+",  newTotal, concatenate) ||
           isTrueFor(testValue, i + 1, "||", newTotal, concatenate) && concatenate
}