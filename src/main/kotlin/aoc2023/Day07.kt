package aoc2023

import readLines
import kotlin.math.pow

fun main() {
    println("D7P1: ${day7Part1()}")
    println("D7P2: ${day7Part2()}")
}

fun day7Part1() = readLines("aoc2023/day07.txt")
    .parseHands()
    .getTotalWinnings()

fun day7Part2() = readLines("aoc2023/day07.txt")
    .parseHands()
    .getTotalWinnings(hasWildCards = true)

private fun List<String>.parseHands(): List<Hand> =
    this.map { it.trim().split(" ").let { (cards, bid) -> Hand(cards, bid.toInt()) } }

private fun List<Hand>.getTotalWinnings(hasWildCards: Boolean = false) =
    this.sortedBy { hand -> hand.getStrength(hasWildCards) }
        .onEachIndexed { rank, hand -> hand.score = hand.bid * (rank + 1L) }
        .sumOf { it.score }

private fun Hand.getStrength(hasWildCards: Boolean): Long {
    val cardTypes = if (hasWildCards) CARDS_TYPES_WITH_WILDCARD else CARDS_TYPES
    val totalCardStrength = (0 until 5).sumOf { i -> cardTypes.indexOf(cards[i]) * STRENGTH_RANGES[i] }
    val handTypeStrength = getHandTypeStrength(cards, hasWildCards) * 10_000_000_000
    return totalCardStrength + handTypeStrength
}

private fun getHandTypeStrength(cards: String, hasWildCards: Boolean): Int {
    if (!hasWildCards) return HAND_TYPES.indexOf(getHandType(cards))
    val baseCards = cards.replace("J", "")
    val numWildcards = cards.count { it == 'J' }
    val numCardTypes = CARDS_TYPES.length.toDouble()
    val numPermutations = numCardTypes.pow(numWildcards.toDouble()).toInt()
    return (0 .. numPermutations).maxOf { i ->
        var wildcards = ""
        repeat(numWildcards) { j -> wildcards += CARDS_TYPES[(i / numCardTypes.pow(j) % numCardTypes).toInt()] }
        HAND_TYPES.indexOf(getHandType(baseCards + wildcards))
    }
}

private fun getHandType(cards: String) =
    cards.groupBy { it }.entries
        .sortedByDescending { (_, group) -> group.size }
        .joinToString("") { (_, group) -> group.size.toString() }

private val CARDS_TYPES = "23456789TJQKA"
private val CARDS_TYPES_WITH_WILDCARD = "J23456789TQKA"
private val HAND_TYPES = arrayOf("11111", "2111", "221", "311", "32", "41", "5")
private val STRENGTH_RANGES = arrayOf(100_000_000, 1_000_000, 10_000, 100, 1)

private data class Hand(val cards: String, val bid: Int, var score: Long = 0)