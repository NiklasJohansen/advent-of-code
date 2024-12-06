package aoc2024

import readLines

fun main() {
    println("D6P1: ${day6Part1()} = 41")
    println("D6P2: ${day6Part2()} = 6")
}

fun day6Part1() = readLines("aoc2024/day06.txt")
    .getVisitedSpots().distinctBy { it.pos }.size

fun day6Part2() = readLines("aoc2024/day06.txt")
    .let { map ->
        map.indices.sumOf { y -> map[y].indices.count { x -> map.getVisitedSpots(obstruction = Vec(x,y)).isEmpty() } }
    }

private fun List<String>.getVisitedSpots(obstruction: Vec? = null): Set<Spot> {
    var pos = findStart()
    var dir = directions[0]
    val visited = mutableSetOf(Spot(pos, dir))
    while (true) {
        val cell = getCell(pos + dir, obstruction) ?: return visited // Outside
        if (cell == '#') {
            dir = directions[(directions.indexOf(dir) + 1) % 4]
            continue
        }
        pos += dir
        visited += Spot(pos, dir).takeIf { it !in visited } ?: return emptySet() // Loop
    }
}

private fun List<String>.findStart() = indexOfFirst { it.contains('^') }.let { y -> Vec(this[y].indexOf('^'), y) }

private fun List<String>.getCell(pos: Vec, obstruction: Vec?) = if (pos == obstruction) '#' else getOrNull(pos.y)?.getOrNull(pos.x)

private val directions = listOf(Vec(0, -1), Vec(1, 0), Vec(0, 1), Vec(-1, 0)) // ^ > v <

private data class Spot(val pos: Vec, val dir: Vec)

private data class Vec(var x: Int, var y: Int) {
    operator fun plus(v: Vec) = Vec(x + v.x, y + v.y)
}