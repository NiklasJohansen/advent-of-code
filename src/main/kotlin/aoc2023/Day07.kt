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
    .getTotalWinnings(hasJoker = true)

private fun List<String>.parseHands(): List<Hand> =
    this.map { it.trim().split(" ").let { (cards, bid) -> Hand(cards, bid.toInt()) } }

private fun List<Hand>.getTotalWinnings(hasJoker: Boolean = false) =
    this.sortedBy { it.getStrength(hasJoker) }.mapIndexed { rank, hand -> (rank + 1) * hand.bid }.sum()

private fun Hand.getStrength(hasJokers: Boolean): Long {
    val cardTypes = if (hasJokers) CARDS_TYPES_WITH_JOKER else CARDS_TYPES
    val totalCardStrength = (0 until 5).sumOf { i -> cardTypes.indexOf(cards[i]) * STRENGTH_RANGES[i] }
    val handTypeStrength = getHandTypeStrength(cards, hasJokers) * 10_000_000_000
    return totalCardStrength + handTypeStrength
}

private fun getHandTypeStrength(cards: String, hasJokers: Boolean): Int {
    if (!hasJokers) return HAND_TYPES.indexOf(getHandType(cards))
    val baseCards = cards.replace("J", "")
    val numJokers = cards.count { it == 'J' }
    val numCardTypes = CARDS_TYPES.length.toDouble()
    val numPermutations = numCardTypes.pow(numJokers.toDouble()).toInt()
    return (0 .. numPermutations).maxOf { i ->
        var wildcards = ""
        repeat(numJokers) { j -> wildcards += CARDS_TYPES[(i / numCardTypes.pow(j) % numCardTypes).toInt()] }
        HAND_TYPES.indexOf(getHandType(baseCards + wildcards))
    }
}

private fun getHandType(cards: String) =
    cards.groupBy { it }.entries
        .sortedByDescending { (_, group) -> group.size }
        .joinToString("") { (_, group) -> group.size.toString() }

private val CARDS_TYPES = "23456789TJQKA"
private val CARDS_TYPES_WITH_JOKER = "J23456789TQKA"
private val HAND_TYPES = arrayOf("11111", "2111", "221", "311", "32", "41", "5")
private val STRENGTH_RANGES = arrayOf(100_000_000, 1_000_000, 10_000, 100, 1)

private data class Hand(val cards: String, val bid: Int)