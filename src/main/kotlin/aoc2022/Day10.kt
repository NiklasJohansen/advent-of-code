package aoc2022

import readLines

fun main() {
    println("D10P1: ${day10Part1()}")
    println("D10P2: ${day10Part2()}")
}

fun day10Part1() = getSignalStrengthSum(readLines("aoc2022/day10.txt"))

fun day10Part2() = getCrtImage(readLines("aoc2022/day10.txt"))

private fun getSignalStrengthSum(instructions: List<String>): Int {
    var signalStrengthSum = 0
    var xRegister = 1
    var cycle = 0

    for (instruction in instructions) {
        cycle++
        signalStrengthSum += getStrength(cycle, xRegister)
        if (instruction.startsWith("addx")) {
            cycle++
            signalStrengthSum += getStrength(cycle, xRegister)
            xRegister += instruction.substringAfter(" ").toInt()
        }
    }

    return signalStrengthSum
}

private fun getStrength(cycle: Int, x: Int) = if ((cycle - 20) % 40 == 0) x * cycle else 0

private fun getCrtImage(instructions: List<String>): String {
    var crtImage = ""
    var xRegister = 1
    var cycle = -1

    for (instruction in instructions) {
        cycle++
        crtImage += getPixel(cycle, xRegister)
        if (instruction.startsWith("addx")) {
            cycle++
            crtImage += getPixel(cycle, xRegister)
            xRegister += instruction.substringAfter(" ").toInt()
        }
    }

    return crtImage.chunked(40).joinToString(separator = "\n", prefix = "\n")
}

private fun getPixel(cycle: Int, x: Int) = (cycle % 40).let { if (it >= x - 1 && it <= x + 1) "#" else "." }