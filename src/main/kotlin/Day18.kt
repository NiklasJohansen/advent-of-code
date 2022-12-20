import Type.BLOCK
import Type.WATER

fun main() {
    println("D18P1: ${day18Part1()}")
    println("D18P2: ${day18Part2()}")
}

fun day18Part1(): Int {
    val cubes = readLines("day18.txt").getCubes()
    return cubes.entries.sumOf { (pos, _) -> 6 - cubes.countNeighboursOfType(pos, BLOCK) }
}

fun day18Part2(): Int {
    val cubes = readLines("day18.txt")
        .getCubes()
        .floodFillWith(WATER)
    return cubes.entries.filter { it.value == BLOCK }.sumOf { cubes.countNeighboursOfType(it.key, WATER) }
}

private fun List<String>.getCubes(): MutableMap<Vec3, Type> = this
    .associate { it.split(",").map { it.toInt() }.let { (x, y, z) -> Vec3(x, y, z) to BLOCK } }
    .toMutableMap()

private fun MutableMap<Vec3, Type>.countNeighboursOfType(pos: Vec3, type: Type = BLOCK): Int =
    neighbourPositions.count { this[Vec3(pos.x + it.x, pos.y + it.y, pos.z + it.z)] == type }

private fun MutableMap<Vec3, Type>.floodFillWith(type: Type): MutableMap<Vec3, Type> {
    val xMin = this.keys.minOf { it.x } - 1
    val xMax = this.keys.maxOf { it.x } + 1
    val yMin = this.keys.minOf { it.y } - 1
    val yMax = this.keys.maxOf { it.y } + 1
    val zMin = this.keys.minOf { it.z } - 1
    val zMax = this.keys.maxOf { it.z } + 1

    this[Vec3(xMin, yMin, zMin)] = type
    var blocksOfTargetType = 1

    while (true) {
        for (x in xMin..xMax) {
            for (y in yMin..yMax) {
                for (z in zMin..zMax) {
                    val pos = Vec3(x, y, z)
                    if (pos !in this && this.countNeighboursOfType(pos, type) > 0)
                        this[pos] = type // Spread when in contact
                }
            }
        }

        val count = this.values.count { it == type }
        if (count == blocksOfTargetType)
            return this

        blocksOfTargetType = count
    }
}

private enum class Type { BLOCK, WATER }
private data class Vec3(val x: Int, val y: Int, val z: Int)
private val neighbourPositions = listOf(
    Vec3(-1, 0, 0),
    Vec3(1, 0, 0),
    Vec3( 0, -1, 0),
    Vec3(0, 1, 0),
    Vec3(0, 0, -1),
    Vec3(0, 0, 1)
)