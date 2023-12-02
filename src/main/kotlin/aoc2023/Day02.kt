package aoc2023

import readLines

fun main() {
    println("D2P1: ${day2Part1()}")
    println("D2P2: ${day2Part2()}")
}

fun day2Part1() = readLines("aoc2023/day02.txt")
    .sumOf { game ->
        val isPossible = game.getSets().none { cubes -> cubes.any { (color, count) -> count > bag[color]!! } }
        if (isPossible) game.getId() else 0
    }

fun day2Part2() = readLines("aoc2023/day02.txt")
    .sumOf { game ->
        val sets = game.getSets()
        bag.keys
            .map { color -> sets.maxOf { cubes -> cubes.maxOf { (clr, count) -> if (clr == color) count else 0 } } }
            .reduce(Int::times)
    }

private fun String.getSets() = this
    .substringAfter(":")
    .split(";")
    .map { set -> set.split(",").map { it.trim().split(" ").let { (count, color) -> color to count.toInt() } } }

private fun String.getId() = this.substring(5).substringBefore(":").toInt()

private val bag = mapOf("red" to 12, "green" to 13, "blue" to 14)