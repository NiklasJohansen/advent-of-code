package aoc2025

import readLines

fun main() {
    println("D4P1: ${day4Part1()} = 13")
    println("D4P2: ${day4Part2()} = 43")
}

fun day4Part1() = readLines("aoc2025/day04.txt", delimiter = "\n")
    .map { it.toCharArray() }
    .let { map -> map.countEachCell { x, y -> map.isPaperRoll(x, y) && map.getAdjacentPaperRolls(x, y) < 4 } }

fun day4Part2() = readLines("aoc2025/day04.txt", delimiter = "\n")
    .map { it.toCharArray() }
    .let { map ->
        var runs = 2
        var totalRemoved = 0
        while (runs > 0) {
            val numRemoved = map.countEachCell { x, y ->
                if (map[y][x] == 'x') map[y][x] = '.'
                (map.isPaperRoll(x, y) && map.getAdjacentPaperRolls(x, y) < 4).also { if (it) map[y][x] = 'x' }
            }
            if (numRemoved == 0) runs-- else totalRemoved += numRemoved
        }
        totalRemoved
    }

fun List<CharArray>.countEachCell(predicate: (x: Int, y: Int) -> Boolean) =
    indices.sumOf { y -> this[0].indices.count { x -> predicate(x, y) } }

fun List<CharArray>.isPaperRoll(x: Int, y: Int) =
    (y in 0 until size) && (x in 0 until first().size) && (this[y][x] == '@' || this[y][x] == 'x')

fun List<CharArray>.getAdjacentPaperRolls(x: Int, y: Int): Int =
    (-1 .. 1).sumOf { yi -> (-1 .. 1).count { xi -> (xi != 0 || yi != 0) && isPaperRoll(x + xi, y + yi) } }