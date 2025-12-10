package aoc2025

import readLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("D9P1: ${day9Part1()} = 50")
    println("D9P2: ${day9Part2()} = 24")
}

fun day9Part1() = readLines("aoc2025/day09.txt", delimiter = "\n")
    .parseTilePositions().getRectangles().maxBy { it.area() }.area()

fun day9Part2() = readLines("aoc2025/day09.txt", delimiter = "\n")
    .parseTilePositions()
    .let { positions ->
        val edges = (positions + positions.first()).zipWithNext { a, b -> Rect.of(a, b) }
        positions
            .getRectangles()
            .sortedByDescending { it.area() }
            .first { rect -> edges.none { edge -> edge.overlaps(rect) } }
            .area()
    }

private fun List<String>.parseTilePositions() =
    map { it.split(',').map(String::toInt).let { (x,y) -> Pos(x,y) } }

private fun List<Pos>.getRectangles() =
    indices.flatMap { j -> (j + 1 until this.size).map { Rect.of(this[j], this[it]) } }

data class Pos(val x: Int, val y: Int)

data class Rect(val x0: Int, val y0: Int, val x1: Int, val y1: Int) {
    fun area() = (x1 - x0 + 1L) * (y1 - y0 + 1L)
    fun overlaps(other: Rect) =
        max(x0, other.x0 + 1) <= min(x1, other.x1 - 1) &&
        max(y0, other.y0 + 1) <= min(y1, other.y1 - 1)
    companion object {
        fun of(a: Pos, b: Pos) = Rect(min(a.x, b.x), min(a.y, b.y), max(a.x, b.x), max(a.y, b.y))
    }
}