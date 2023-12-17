package aoc2023

import readLines

fun main() {
    println("D16P1: ${day16Part1()} = 7434")
    println("D16P2: ${day16Part2()} = 8183")
}

fun day16Part1() = readLines("aoc2023/day16.txt")
    .let { map -> getEnergizedTileCount(map, Beam(0, 0, 1, 0)) }

fun day16Part2() = readLines("aoc2023/day16.txt")
    .let { map -> map.getBorderBeams().maxOf { beam -> getEnergizedTileCount(map, beam) } }

private fun getEnergizedTileCount(map: List<String>, startBeam: Beam): Int {
    val beams = mutableListOf(startBeam)
    val energizedTiles = mutableSetOf<Pair<Int, Int>>()
    val tileCounts = Array(5) { 0 }
    var tileCountIndex = 0
    while (true) {
        for (i in 0 until beams.size) {
            val b = beams[i]
            val tile = map.getOrNull(b.y)?.getOrNull(b.x) ?: continue
            if (tile == '-' && b.xDir == 0) {
                b.setDir(-1, 0)
                beams += b.copy(xDir = 1)
            } else if (tile == '|' && b.yDir == 0) {
                b.setDir(0, -1)
                beams += b.copy(yDir = 1)
            } else if (tile == '/' || tile == '\\') {
                val d = if (tile == '/') 1 else -1
                when {
                    b.xDir ==  1 -> b.setDir( 0, -d)
                    b.xDir == -1 -> b.setDir( 0,  d)
                    b.yDir ==  1 -> b.setDir(-d,  0)
                    b.yDir == -1 -> b.setDir( d,  0)
                }
            }
            energizedTiles.add(Pair(b.x, b.y))
            b.move()
        }
        tileCounts[(tileCountIndex++) % tileCounts.size] = energizedTiles.size
        if (tileCounts.all { it == tileCounts[0] })
            return energizedTiles.size // No change in energized tile count last 5 iterations, we're done
    }
}

private fun List<String>.getBorderBeams() = listOf(
    IntRange(0, this.lastIndex).map    { y -> Beam(x = 0, y = y, xDir = 1, yDir = 0) },
    IntRange(0, this.lastIndex).map    { y -> Beam(x = this[0].lastIndex, y = y, xDir = -1, yDir = 0) },
    IntRange(0, this[0].lastIndex).map { x -> Beam(x = x, y = 0, xDir = 0, yDir = 1) },
    IntRange(0, this[0].lastIndex).map { x -> Beam(x = x, y = this.lastIndex, xDir = 0, yDir = -1) }
).flatten()

private data class Beam(var x: Int, var y: Int, var xDir: Int, var yDir: Int) {
    fun move() { x += xDir; y += yDir }
    fun setDir(xd: Int, yd: Int) { xDir = xd; yDir = yd }
}