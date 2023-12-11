package aoc2023

import readLines

fun main() {
    println("D10P1: ${day10Part1()}")
    println("D10P2: ${day10Part2()}")
}

fun day10Part1() = readLines("aoc2023/day10.txt")
    .parseToPipeMap()
    .findPipesPartOfLoop()
    .sumOf { pipes -> pipes.count { it.isPartOfLoop } }
    .let { loopLength -> loopLength / 2 }

fun day10Part2() = readLines("aoc2023/day10.txt")
    .parseToPipeMap()
    .inflatePipeMap()
    .findPipesPartOfLoop()
    .floodFillPipeMap()
    .sumOf { pipes -> pipes.count { !it.isOutside && !it.isPartOfLoop && !it.isInflated } }

private fun List<String>.parseToPipeMap(): PipeMap =
    this.mapIndexed { y, row ->
        row.mapIndexed { x, type ->
            val up =    if (type == START_TYPE) (this[x, y - 1] in CONNECTS_DOWN)  else (type in CONNECTS_UP)
            val down =  if (type == START_TYPE) (this[x, y + 1] in CONNECTS_UP)    else (type in CONNECTS_DOWN)
            val left =  if (type == START_TYPE) (this[x - 1, y] in CONNECTS_RIGHT) else (type in CONNECTS_LEFT)
            val right = if (type == START_TYPE) (this[x + 1, y] in CONNECTS_LEFT)  else (type in CONNECTS_RIGHT)
            Pipe(type, x, y, up, down, left, right)
        }
    }

private fun PipeMap.findPipesPartOfLoop(): PipeMap {
    val startPipe = this.getStartPipe()
    var last: Pipe? = null
    var pipe = startPipe
    while (true) {
        pipe.isPartOfLoop = true
        val next = when {
            pipe.connectsUp    && pipe.y - 1 != last?.y -> this[pipe.x, pipe.y - 1]
            pipe.connectsDown  && pipe.y + 1 != last?.y -> this[pipe.x, pipe.y + 1]
            pipe.connectsLeft  && pipe.x - 1 != last?.x -> this[pipe.x - 1, pipe.y]
            pipe.connectsRight && pipe.x + 1 != last?.x -> this[pipe.x + 1, pipe.y]
            else -> break
        }
        last = pipe
        pipe = next
        if (next === startPipe)
            break
    }
    return this
}

private fun PipeMap.inflatePipeMap(): PipeMap {
    val width = this[0].size * 2 + 2
    val height = this.size * 2 + 2
    return (0 until height).map { y ->
        (0 until width).map { x ->
            val x0 = (x - 1) / 2
            val y0 = (y - 1) / 2
            if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                Pipe('.', x, y, isInflated = true)
            } else if (x % 2 == 0 && y % 2 == 0) {
                this[x0, y0].copy(x = x, y = y)
            } else if (x % 2 == 0 && this[x0, y0 - 1].connectsDown && this[x0, y0].connectsUp) {
                Pipe('|', x, y, connectsUp = true, connectsDown = true, isInflated = true)
            } else if (y % 2 == 0 && this[x0 - 1, y0].connectsRight && this[x0, y0].connectsLeft) {
                Pipe('-', x, y, connectsLeft = true, connectsRight = true, isInflated = true)
            } else {
                Pipe('.', x, y, isInflated = true)
            }
        }
    }
}

private fun PipeMap.floodFillPipeMap(): PipeMap {
    val pipes = mutableListOf(this[0, 0].apply { isOutside = true })
    while (pipes.isNotEmpty()) {
        val pipe = pipes.removeAt(0)
        val up = this[pipe.x - 1, pipe.y]
        val down = this[pipe.x + 1, pipe.y]
        val left = this[pipe.x, pipe.y - 1]
        val right = this[pipe.x, pipe.y + 1]
        if (!up.isOutside    && !up.isPartOfLoop)    pipes += up.apply { isOutside = true }
        if (!down.isOutside  && !down.isPartOfLoop)  pipes += down.apply { isOutside = true }
        if (!left.isOutside  && !left.isPartOfLoop)  pipes += left.apply { isOutside = true }
        if (!right.isOutside && !right.isPartOfLoop) pipes += right.apply { isOutside = true }
    }
    return this
}

private fun PipeMap.getStartPipe(): Pipe {
    val yStart = this.indexOfFirst { it.any { it.type == 'S' } }
    val xStart = this[yStart].indexOfFirst { it.type == 'S' }
    return this[xStart, yStart]
}

private operator fun PipeMap.get(x: Int, y: Int) =
    getOrNull(y)?.getOrNull(x) ?: Pipe('.', x, y, isInflated = true, isOutside = true)

private operator fun List<String>.get(x: Int, y: Int) =
    getOrNull(y)?.getOrNull(x)

private data class Pipe(
    val type: Char,
    val x: Int,
    val y: Int,
    val connectsUp: Boolean = false,
    val connectsDown: Boolean = false,
    val connectsLeft: Boolean = false,
    val connectsRight: Boolean = false,
    val isInflated: Boolean = false,
    var isOutside: Boolean = false,
    var isPartOfLoop: Boolean = false
)

private typealias PipeMap = List<List<Pipe>>

private val START_TYPE = 'S'
private val CONNECTS_UP = setOf('|', 'L', 'J')
private val CONNECTS_DOWN = setOf('|', '7', 'F')
private val CONNECTS_LEFT = setOf('-', '7', 'J')
private val CONNECTS_RIGHT = setOf('-', 'L', 'F')