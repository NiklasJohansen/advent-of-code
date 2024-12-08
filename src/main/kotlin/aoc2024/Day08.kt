package aoc2024

import readLines

fun main() {
    println("D8P1: ${day8Part1()} = 14")
    println("D8P2: ${day8Part2()} = 34")
}

fun day8Part1() = readLines("aoc2024/day08.txt")
    .countAntinodeLocations(resonantHarmonics = false)

fun day8Part2() = readLines("aoc2024/day08.txt")
    .countAntinodeLocations(resonantHarmonics = true)

private fun List<String>.countAntinodeLocations(resonantHarmonics: Boolean): Int {
    val locations = mutableSetOf<Pos>()
    for (a in findAntennas { it.frequency != '.' }) {
        for (b in findAntennas { b -> b.frequency == a.frequency && b.pos != a.pos }) {
            val delta = b.pos - a.pos
            var pos = delta + if (resonantHarmonics) a.pos else b.pos
            while (pos.x in 0 until this[0].length && pos.y in 0 until this.size) {
                locations.add(pos)
                pos += delta
                if (!resonantHarmonics) break
            }
        }
    }
    return locations.size
}

private fun List<String>.findAntennas(predicate: (Antenna) -> Boolean): List<Antenna> =
    indices.flatMap { y -> this[y].indices.mapNotNull { x -> this[y][x].let { Antenna(Pos(x,y),it).takeIf(predicate) } } }

private data class Antenna(val pos: Pos, val frequency: Char)
private data class Pos(val x: Int, val y: Int) {
    operator fun plus(p: Pos) = Pos(x + p.x, y + p.y)
    operator fun minus(p: Pos) = Pos(x - p.x, y - p.y)
}