
package aoc2024

import readLines

fun main() {
    println("D10P1: ${day10Part1()} = 36")
    println("D10P2: ${day10Part2()} = 81")
}

fun day10Part1() = readLines("aoc2024/day10.txt")
    .parseMap()
    .let { map -> map.getTrailHeads().sumOf { trailHead -> map.getScoreFor(trailHead, trailEnds = mutableSetOf()) } }

fun day10Part2() = readLines("aoc2024/day10.txt")
    .parseMap()
    .let { map -> map.getTrailHeads().sumOf { trailHead -> map.getScoreFor(trailHead) } }

private fun List<List<Int>>.getScoreFor(pos: TrailPos, lastHeight: Int = -1, trailEnds: MutableSet<TrailPos>? = null): Int {
    val height = getOrNull(pos.y)?.getOrNull(pos.x)

    if (height == null || height != lastHeight + 1 || trailEnds?.contains(pos) == true)
        return 0 // Not the way

    if (height == 9) {
        trailEnds?.add(pos)
        return 1 // End found
    }

    return getScoreFor(pos.left(), height, trailEnds) +
           getScoreFor(pos.right(), height, trailEnds) +
           getScoreFor(pos.up(), height, trailEnds) +
           getScoreFor(pos.down(), height, trailEnds)
}

private fun List<String>.parseMap() = map { it.map { it.digitToInt() } }

private fun List<List<Int>>.getTrailHeads(): List<TrailPos> =
    flatMapIndexed { y, row -> row.mapIndexedNotNull { x, height -> TrailPos(x, y).takeIf { height == 0 } } }

private data class TrailPos(val x: Int, val y: Int) {
    fun left()  = TrailPos(x - 1, y)
    fun right() = TrailPos(x + 1, y)
    fun up()    = TrailPos(x, y - 1)
    fun down()  = TrailPos(x, y + 1)
}