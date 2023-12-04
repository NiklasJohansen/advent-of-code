package aoc2023

import readLines
import kotlin.math.pow

fun main() {
    println("D4P1: ${day4Part1()}")
    println("D4P2: ${day4Part2()}")
}

fun day4Part1() = readLines("aoc2023/day04.txt")
    .sumOf { 2.0.pow(it.toCard().winCount - 1).toInt() }

fun day4Part2(): Int {
    val cards = readLines("aoc2023/day04.txt").map { it.toCard() }.toMutableList()
    var index = 0
    while (index < cards.size) {
        val card = cards[index++]
        for (i in 0 until card.winCount)
            cards += cards[card.number + i]
    }
    return cards.size
}

private fun String.toCard() = this
    .split("|")
    .map { it.split(" ").mapNotNull { num -> num.trim().toIntOrNull() } }
    .let { (winNums, myNums) -> winNums.intersect(myNums).count() }
    .let { wins -> Card(number = substringBefore(":").substringAfterLast(" ").toInt(), winCount = wins) }

data class Card(val number: Int, val winCount: Int)