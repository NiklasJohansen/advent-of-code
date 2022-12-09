import kotlin.math.max

fun main() {
    println("D8P1: ${day8Part1()}")
    println("D8P2: ${day8Part2()}")
}

fun day8Part1() = readLines("day8.txt").let { map ->
    var count = 0
    for (y in 1 until map.lastIndex) {
        for (x in 1 until map[0].lastIndex) {
            if (map.visibleLeft(x, y) || map.visibleRight(x, y) || map.visibleUp(x, y) || map.visibleDown(x, y))
                count++
        }
    }
    count + 2 * (map.size + map[0].length - 2)
}

fun day8Part2() = readLines("day8.txt").let { map ->
    var maxVal = 0
    for (y in 1 until map.lastIndex) {
        for (x in 1 until map[0].lastIndex) {
            maxVal = max(maxVal, map.countLeft(x, y) * map.countRight(x, y) * map.countUp(x, y) * map.countDown(x, y))
        }
    }
    maxVal
}

fun List<String>.visibleLeft(x: Int, y: Int) = (x - 1 downTo  0).none { this[y][it] >= this[y][x] }

fun List<String>.visibleRight(x: Int, y: Int) = (x + 1 until this[y].length).none { this[y][it] >= this[y][x] }

fun List<String>.visibleUp(x: Int, y: Int) = (y - 1 downTo  0).none { this[it][x] >= this[y][x] }

fun List<String>.visibleDown(x: Int, y: Int) = (y + 1 until this.size).none { this[it][x] >= this[y][x] }

fun List<String>.countLeft(x: Int, y: Int) = (x - 1 downTo  0).takeWhile { this[y][it] < this[y][x] }
    .let { it.size + if (it.lastOrNull() != 0) 1 else 0 }

fun List<String>.countRight(x: Int, y: Int) = (x + 1 until  this[y].length).takeWhile { this[y][it] < this[y][x] }
    .let { it.size + if (it.lastOrNull() != this[y].length) 1 else 0 }

fun List<String>.countUp(x: Int, y: Int) = (y - 1 downTo  0).takeWhile { this[it][x] < this[y][x] }
    .let { it.size + if (it.lastOrNull() != 0) 1 else 0 }

fun List<String>.countDown(x: Int, y: Int) = (y + 1 until  this.lastIndex).takeWhile { this[it][x] < this[y][x] }
    .let { it.size + if (it.lastOrNull() != this.size) 1 else 0 }