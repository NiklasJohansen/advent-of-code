package aoc2025

import readLines

fun main() {
    println("D6P1: ${day6Part1()} = 4277556")
    println("D6P2: ${day6Part2()} = 3263827")
}

fun day6Part1() = readLines("aoc2025/day06.txt", delimiter = "\n")
    .let { rows ->
        val numbers = rows.dropLast(1).map { it.split(' ').mapNotNull { num -> num.toLongOrNull() } }
        val operators = rows.last().split(' ').filter { it.isNotBlank() }
        (0 until numbers[0].size).sumOf { x ->
            var total = numbers[0][x]
            for (y in 1 until numbers.size)
                total = numbers[y][x].let { if (operators[x] == "+") total + it else total * it }
            total
        }
    }

fun day6Part2() = readLines("aoc2025/day06.txt", delimiter = "\n")
    .let { rows ->
        var total = 0L
        var subTotal = 0L
        var operation = rows.last()[0]
        for (x in 0 until rows[0].length) {
            val number = rows.indices.fold(0L) { n, y -> rows[y][x].digitToIntOrNull()?.let { n * 10L + it } ?: n }
            if (number == 0L) {
                total += subTotal
                subTotal = 0
            } else {
                operation = rows.last()[x].takeIf { it != ' ' } ?: operation
                subTotal = if (operation == '+' || subTotal == 0L) subTotal + number else subTotal * number
            }
        }
        total + subTotal
    }