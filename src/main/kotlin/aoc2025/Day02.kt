package aoc2025

import readLines

fun main() {
    println("D2P1: ${day2Part1()} = 1227775554")
    println("D2P2: ${day2Part2()} = 4174379265")
}

fun day2Part1() = readLines("aoc2025/day02.txt", delimiter = ",")
    .parseIds()
    .sumOf { id ->
        val str = id.toString()
        val half = str.length / 2
        if (str.take(half) == str.drop(half)) id else 0
    }

fun day2Part2() = readLines("aoc2025/day02.txt", delimiter = ",")
    .parseIds()
    .sumOf { id ->
        val str = id.toString()
        val isInvalid = (1 until str.length).any { size ->
            (size until str.length step size).all { pos ->
                pos + size <= str.length && str.substring(pos, pos + size) == str.take(size)
            }
        }
        if (isInvalid) id else 0
    }

fun List<String>.parseIds() = flatMap { it.split("-").let { (from, to) -> from.toLong() .. to.toLong() } }