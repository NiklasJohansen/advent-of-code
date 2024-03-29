package aoc2023

import readLines

fun main() {
    println("D14P1: ${day14Part1()}")
    println("D14P2: ${day14Part2()}")
}

fun day14Part1() = readLines("aoc2023/day14.txt")
    .map { it.toCharArray() }
    .apply { tiltNorth() }
    .calculateLoad()

fun day14Part2() = readLines("aoc2023/day14.txt")
    .map { it.toCharArray() }
    .apply { cycleBoard() }
    .calculateLoad()

private fun List<CharArray>.calculateLoad(): Int =
    this.mapIndexed { i, row -> row.count { it == 'O' } * (this.size - i) }.sum()

private fun List<CharArray>.cycleBoard() {
    val boardHashes = mutableSetOf<String>()
    var cyclesLeft = 1_000_000_000
    while (cyclesLeft > 0) {
        this.tiltNorth()
        this.tiltWest()
        this.tiltSouth()
        this.tiltEast()
        val hash = this.joinToString("") { it.joinToString("") }
        if (hash in boardHashes) {
            val patternStartIndex = boardHashes.indexOf(hash)
            val patternLength = boardHashes.size - patternStartIndex
            cyclesLeft %= patternLength
        }
        boardHashes.add(hash)
        cyclesLeft--
    }
}

private fun List<CharArray>.tiltNorth() {
    for (y in indices) {
        for (x in this[y].indices) {
            if (this[y][x] != 'O') continue
            var y0 = y
            while (y0 - 1 >= 0 && this[y0 - 1][x] == '.') { y0-- }
            this[y][x] = '.'
            this[y0][x] = 'O'
        }
    }
}

private fun List<CharArray>.tiltWest() {
    for (x in 0 until this[0].size) {
        for (y in this.indices) {
            if (this[y][x] != 'O') continue
            var x0 = x
            while (x0 - 1 >= 0 && this[y][x0 - 1] == '.') { x0-- }
            this[y][x] = '.'
            this[y][x0] = 'O'
        }
    }
}

private fun List<CharArray>.tiltSouth() {
    for (y in this.lastIndex downTo  0) {
        for (x in this[y].indices) {
            if (this[y][x] != 'O') continue
            var y0 = y
            while (y0 + 1 < this.size && this[y0 + 1][x] == '.') { y0++ }
            this[y][x] = '.'
            this[y0][x] = 'O'
        }
    }
}

private fun List<CharArray>.tiltEast() {
    for (x in this[0].lastIndex downTo  0) {
        for (y in this.indices) {
            if (this[y][x] != 'O') continue
            var x0 = x
            while (x0 + 1 < this.size && this[y][x0 + 1] == '.') { x0++ }
            this[y][x] = '.'
            this[y][x0] = 'O'
        }
    }
}