import java.util.*

fun main() {
    println("D17P1: ${day17Part1()}")
    println("D17P2: ${day17Part2()}")
}

fun day17Part1() = readLines("day17.txt")
    .getWinds()
    .getTowerHeight(totalRocks = 2023L)

fun day17Part2() = readLines("day17.txt")
    .getWinds()
    .getTowerHeight(totalRocks = 1_000_000_000_001L)

fun List<String>.getWinds() = this.first().map { if (it == '<') -1L else 1L }

fun List<Long>.getTowerHeight(totalRocks: Long): Long {
    val rockMap = mutableSetOf<Pos>()
    var shapeIndex = 0
    var rockCount = 0L
    var lastRockNum = 0L
    var yStart = 0L
    var lastYStart = 0L
    var windIndex = 0
    var initialPattern: BitSet? = null

    while (rockCount < totalRocks) {
        val rockShape = shapes[shapeIndex]
        val shapeHeight = rockShape.size
        val rockPos = Pos(2, (yStart) - 3 - (shapeHeight - 1))
        var shouldFall = false

        while (true) {
            if (shouldFall) {
                if (rockCount == totalRocks - 1L)
                    return -yStart // Highest rock

                if (rockMap.canMoveTo(rockPos, rockShape, xDir = 0, yDir = 1)) {
                    rockPos.y++
                } else {
                    rockMap.insert(rockPos, rockShape)
                    if (rockPos.y - 1 < yStart)
                        yStart = rockPos.y - 1
                    break
                }
            } else {
                val wind = this[windIndex]
                if (rockMap.canMoveTo(rockPos, rockShape, xDir = wind, yDir = 0))
                    rockPos.x += wind
                windIndex = (windIndex + 1) % this.size
            }
            shouldFall = !shouldFall
        }

        if (rockCount == 1000L) {
            initialPattern = rockMap.getPatternSignature(yStart, yStart + 10)
            lastRockNum = rockCount
            lastYStart = yStart
        } else if (rockCount > 1000L) {
            val currentPattern = rockMap.getPatternSignature(yStart, yStart + 10)
            if (currentPattern == initialPattern) {
                val rockNumDelta = rockCount - lastRockNum
                val heightDelta = yStart - lastYStart
                val rockNumToJump = (totalRocks - rockCount) / rockNumDelta
                val heightJump = heightDelta * rockNumToJump
                rockCount += rockNumDelta * rockNumToJump
                yStart += heightJump
                val newMap = rockMap.map { Pos(it.x, it.y + heightJump)}
                rockMap.clear()
                rockMap.addAll(newMap)
            }
        }

        shapeIndex = (shapeIndex + 1) % shapes.size
        rockCount++
    }

    return -1
}

private fun MutableSet<Pos>.insert( pos: Pos, shape: List<String>) {
    for (y in 0 until shape.size) {
        for (x in 0 until shape[0].length) {
            if (shape[y][x] == '#')
                this.add(Pos(pos.x + x, pos.y + y))
        }
    }
}

private fun MutableSet<Pos>.canMoveTo(pos: Pos, shape: List<String>, xDir: Long, yDir: Long): Boolean {
    for (y in 0 until shape.size) {
        for (x in 0 until shape[0].length) {
            val p = Pos(pos.x + x + xDir, pos.y + y + yDir)
            if (shape[y][x] == '#' && (p in this || p.x < 0 || p.x >= 7L || p.y > 0))
                return false
        }
    }
    return true
}

private fun MutableSet<Pos>.getPatternSignature(yStart: Long, yEnd: Long): BitSet {
    val range = yEnd - yStart
    val bitSet = BitSet(range.toInt())
    var i = 0
    for (y in yStart until yEnd) {
        for (x in 0L until 7L) {
            bitSet.set(i++, Pos(x, y) in this)
        }
    }
    return bitSet
}

private data class Pos(var x: Long, var y: Long)

private val shapes = """
    ####

    .#.
    ###
    .#.

    ..#
    ..#
    ###

    #
    #
    #
    #

    ##
    ##
""".trimIndent().split("\n\n").map { it.split("\n") }