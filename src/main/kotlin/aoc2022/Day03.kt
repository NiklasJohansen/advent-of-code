package aoc2022

import readLines

fun main() {
    println("D3P1: ${day3Part1()}")
    println("D3P2: ${day3Part2()}")
}

fun day3Part1() = readLines("aoc2022/day03-1.txt")
    .sumOf { rucksacks ->
        val (compartment1, compartment2) = rucksacks.chunked(rucksacks.length / 2)
        val duplicateItem = compartment1.find { item -> compartment2.any { it == item } }
        duplicateItem!!.getPriority()
    }

fun day3Part2() = readLines("aoc2022/day03-2.txt")
    .chunked(3)
    .sumOf { rucksacks ->
        val badge = rucksacks[0].find { item ->
            rucksacks[1].any { it == item } &&
            rucksacks[2].any { it == item }
        }
        badge!!.getPriority()
    }

private fun Char.getPriority() = if (code >= 97) code - 96 else code - 65 + 27