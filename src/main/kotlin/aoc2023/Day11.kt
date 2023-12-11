package aoc2023

import readLines
import kotlin.math.abs

fun main() {
    println("D11P1: ${day11Part1()}")
    println("D11P2: ${day11Part2()}")
}

fun day11Part1() = readLines("aoc2023/day11.txt")
    .sumOfGalaxyLengths(expansionRate = 1)

fun day11Part2() = readLines("aoc2023/day11.txt")
    .sumOfGalaxyLengths(expansionRate = 999_999)

private fun List<String>.sumOfGalaxyLengths(expansionRate: Long): Long {
    val galaxies = this.flatMapIndexed { y, row -> row.mapIndexedNotNull { x, g -> Galaxy(x, y).takeIf { g == '#' } } }

    (0 until this[0].length)
        .filter { x -> galaxies.none { it.x == x } }
        .forEach { x -> galaxies.filter { it.x > x }.forEach { it.xExpanded += expansionRate } }

    (0 until this.size)
        .filter { y -> galaxies.none { it.y == y } }
        .forEach { y -> galaxies.filter { it.y > y }.forEach { it.yExpanded += expansionRate } }

    return galaxies
        .flatMap { g0 -> galaxies.map { g1 -> g0 to g1 } }
        .sumOf { (g0, g1) -> abs(g0.xExpanded - g1.xExpanded) + abs(g0.yExpanded - g1.yExpanded) } / 2
}

data class Galaxy(var x: Int, var y: Int, var xExpanded: Long = x.toLong(), var yExpanded: Long = y.toLong())