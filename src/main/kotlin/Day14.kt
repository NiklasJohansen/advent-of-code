import Block.*

fun main() {
    println("D14P1: ${day14Part1()}")
    println("D14P2: ${day14Part2()}")
}

fun day14Part1() = readLines("day14.txt", delimiter = "\n")
    .parseMap()
    .simulate(enableFloor = false, stopWhen = { floorLevel, sand -> sand.y > floorLevel })

fun day14Part2() = readLines("day14.txt", delimiter = "\n")
    .parseMap()
    .simulate(enableFloor = true, stopWhen = { _, sand -> sand.x == 500 && sand.y == 0 })

private fun List<String>.parseMap(): MutableMap<Position, Block> {
    val map = mutableMapOf<Position, Block>()
    for (path in this) {
        val points = path.split(" -> ").map { it.split(",").let { (x, y) -> Pair(x.toInt(), y.toInt()) } }
        for (i in 1 until points.size) {
            val (x0, y0) = points[i - 1]
            val (x1, y1) = points[i]
            val xRange = if (x0 < x1) x0..x1 else x1..x0
            val yRange = if (y0 < y1) y0..y1 else y1..y0
            yRange.forEach { y -> xRange.forEach { x -> map[Position(x, y)] = ROCK } }
        }
    }
    return map
}

private fun MutableMap<Position, Block>.simulate(
    enableFloor: Boolean,
    stopWhen: (floorLevel: Int, sand: Position) -> Boolean
): Int {
    val floorLevel = this.keys.maxOf { it.y } + if (enableFloor) 2 else 0

    while (true) {
        val sand = Position(500, 0)
        var simulate = true
        while (simulate) {
            if (enableFloor && sand.y + 1 == floorLevel) {
                this[sand] = SAND
                simulate = false
            } else if (isSpaceDown(sand)) {
                sand.y++
            } else if (isSpaceDownLeft(sand)) {
                sand.x--
                sand.y++
            } else if (isSpaceDownRight(sand)) {
                sand.x++
                sand.y++
            } else {
                this[sand] = SAND
                simulate = false
            }

            if (stopWhen(floorLevel, sand))
                return this.values.count { it == SAND }
        }
    }
}

private enum class Block { SAND, ROCK }
private data class Position(var x: Int, var y: Int)

private fun Map<Position, Block>.isSpaceDown(p: Position) = Position(p.x, p.y + 1) !in this
private fun Map<Position, Block>.isSpaceDownLeft(p: Position) = Position(p.x - 1, p.y + 1) !in this
private fun Map<Position, Block>.isSpaceDownRight(p: Position) = Position(p.x + 1, p.y + 1) !in this