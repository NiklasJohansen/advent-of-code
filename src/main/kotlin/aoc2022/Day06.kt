package aoc2022

import readText

fun main() {
    println("D6P1: ${day6Part1()}")
    println("D6P2: ${day6Part2()}")
}
fun day6Part1() = readText("aoc2022/day06.txt").getProcessedCharacters(4)

fun day6Part2() = readText("aoc2022/day06.txt").getProcessedCharacters(14)

private fun String.getProcessedCharacters(stride: Int) =
    this.windowed(stride, 1).indexOfFirst { it.toSet().size == stride } + stride
