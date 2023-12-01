package aoc2022

import readLines

fun main() {
    println("D2P1: ${day2Part1()}")
    println("D2P2: ${day2Part2()}")
}

fun day2Part1() = readLines("aoc2022/day02.txt").sumOf { getShapeScore(it) + getRoundScore(it) }

fun day2Part2() = readLines("aoc2022/day02.txt").sumOf { getShapeScore2(it) + getRoundScore2(it) }

private fun getShapeScore(round: String) = when (round[2]) {
    'X' -> 1 // Rock
    'Y' -> 2 // Paper
    'Z' -> 3 // Scissor
    else -> 0
}

private fun getRoundScore(round: String) = when (round) {
    "A Y", "B Z", "C X" -> 6 // Win
    "A X", "B Y", "C Z" -> 3 // Draw
    "A Z", "B X", "C Y" -> 0 // Loss
    else -> 0
}

private fun getRoundScore2(round: String) = when (round[2]) {
    'Z' -> 6 // Win
    'Y' -> 3 // Draw
    'X' -> 0 // Loss
    else -> 0
}

private fun getShapeScore2(round: String) = when (round[2]) {
    'Z' -> when (round[0]) { // Win
        'A' -> 2 // Paper
        'B' -> 3 // Scissor
        'C' -> 1 // Rock
        else -> 0
    }
    'Y' -> when (round[0]) { // Draw
        'A' -> 1 // Rock
        'B' -> 2 // Paper
        'C' -> 3 // Scissor
        else -> 0
    }
    'X' -> when (round[0]) { // Loss
        'A' -> 3 // Scissor
        'B' -> 1 // Rock
        'C' -> 2 // Paper
        else -> 0
    }
    else -> 0
}