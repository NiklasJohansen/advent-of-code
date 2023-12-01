package aoc2022

import readLines
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    println("D9P1: ${day9Part1()}")
    println("D9P2: ${day9Part2()}")
}

fun day9Part1() = countVisitedPositions(moves = readLines("aoc2022/day09.txt"), nKnots = 2)

fun day9Part2() = countVisitedPositions(moves = readLines("aoc2022/day09.txt"), nKnots = 10)

private fun countVisitedPositions(moves: List<String>, nKnots: Int): Int {
    val visited = mutableSetOf("0:0")
    val knots = MutableList(nKnots) { Knot(0, 0) }
    for (move in moves) {
        val (direction, steps) = move.split(" ")
        repeat(steps.toInt()) {
            knots[0].move(direction)
            for (i in 1 until knots.size) {
                knots[i].moveAfter(knots[i - 1])
            }
            with(knots.last()) { visited.add("$x:$y") }
        }
    }
    return visited.size
}

private data class Knot(var x: Int, var y: Int) {
    fun move(direction: String) {
        when (direction) {
            "R" -> x++
            "L" -> x--
            "D" -> y++
            "U" -> y--
        }
    }
    fun moveAfter(knot: Knot) {
        val dx = knot.x - x
        val dy = knot.y - y
        if (abs(dx) > 1 || abs(dy) > 1) {
            x += dx.sign
            y += dy.sign
        }
    }
}