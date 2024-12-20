package aoc2024

import readLines

fun main() {
    println("D4P1: ${day4Part1()} = 18")
    println("D4P2: ${day4Part2()} = 9")
}

fun day4Part1() = readLines("aoc2024/day04.txt")
    .sumOfEachCell { grid, x, y ->
        directions.count { (dx, dy) -> (0..3).all { i -> "XMAS"[i] == grid[y + dy * i, x + dx * i] } }
    }

fun day4Part2() = readLines("aoc2024/day04.txt")
    .sumOfEachCell { grid, x, y ->
        patterns.count { pattern -> (0..4).all { i -> pattern[i] == grid[(y-1 + i*2 / 3), (x-1 + i*2 % 3)] } }
    }

fun List<String>.sumOfEachCell(action: (grid: List<String>, x: Int, y: Int) -> Int) =
    indices.sumOf { y -> this[y].indices.sumOf { x -> action(this, x, y) } }

private operator fun List<String>.get(y: Int, x: Int) = getOrNull(y)?.getOrNull(x)

private val directions = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0, 1 to 1, -1 to -1, -1 to 1, 1 to -1)
private val patterns = arrayOf("MSAMS", "MMASS", "SSAMM", "SMASM")