package aoc2023

import readLines

fun main() {
    println("D13P1: ${day13Part1()}")
    println("D13P2: ${day13Part2()}")
}

fun day13Part1() = readLines("aoc2023/day13.txt", delimiter = "\n\n")
    .sumOf { note -> note.getColumns()?.times(100) ?: note.getRows() ?: 0 }

fun day13Part2() = readLines("aoc2023/day13.txt", delimiter = "\n\n")
    .sumOf { note ->
        val lastColumnCount = note.getColumns()
        val lastRowCount = note.getRows()
        note.indices.firstNotNullOfOrNull { i ->
            note.unsmudgeAt(i).let { it.getColumns(lastColumnCount)?.times(100) ?: it.getRows(lastRowCount) }
        } ?: lastColumnCount?.times(100) ?: lastRowCount ?: 0
    }

private fun String.getRows(lastCount: Int? = null) =
    this.split("\n").let { rows -> rows[0].indices.map { i -> rows.joinToString("") { "${it[i]}" } }.getReflectionCount(lastCount) }

private fun String.getColumns(lastCount: Int? = null) =
    this.split("\n").getReflectionCount(lastCount)

private fun String.unsmudgeAt(i: Int) =
    this.toCharArray().also { it[i] = if (it[i] == '.') '#' else if (it[i] == '#') '.' else it[i] }.let { String(it) }

private fun List<String>.getReflectionCount(lastCount: Int?): Int? {
    repeat(size - 1) { i ->
        var left = i
        var right = i + 1
        var reflection = true
        while (reflection && left >= 0 && right < size) {
            reflection = this[left--] == this[right++]
        }
        if (reflection && i + 1 != lastCount)
            return i + 1
    }
    return null
}