package aoc2023

import readLines

fun main() {
    println("D3P1: ${day3Part1()}")
    println("D3P2: ${day3Part2()}")
}

fun day3Part1() = readLines("aoc2023/day03.txt")
    .getParts()
    .sumOf { it.number }

fun day3Part2() = readLines("aoc2023/day03.txt")
    .getParts()
    .filter { it.symbol == '*' }
    .groupBy { it.pos }.entries
    .sumOf { (_, gears) -> if (gears.size == 2) gears[0].number * gears[1].number else 0 }

private fun List<String>.getParts(): List<Part> =
    this.flatMapIndexed { y, line ->
        var partNum = ""
        var part: Part? = null
        val parts = mutableListOf<Part>()
        line.mapIndexedNotNull { x, char ->
            if (char.isDigit()) {
                partNum += char
                this.findPartAround(x, y)?.let { part = it }
            }
            if (line.getOrNull(x + 1)?.isDigit() != true) {
                part?.let { parts += it.apply { number = partNum.toInt() } }
                part = null
                partNum = ""
            }
        }
        parts
    }

private data class Part(val symbol: Char, val pos: String, var number: Int = 0)

private fun List<String>.findPartAround(x: Int, y: Int): Part? =
    directions.firstNotNullOfOrNull { (xd, yd) ->
        this.getOrNull(y+yd)?.getOrNull(x+xd)?.takeIf { it != '.' && !it.isDigit() }?.let { Part(it, "${x+xd}:${y+yd}") }
    }

private val directions = listOf(
    Pair(-1, -1), Pair(0, -1), Pair(1, -1), Pair(-1, 0), Pair(1, 0), Pair(-1, 1), Pair(0, 1), Pair(1, 1)
)