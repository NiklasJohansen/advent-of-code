package aoc2024

import readLines

typealias Rule = List<Int>
typealias Update = List<Int>

fun main() {
    println("D5P1: ${day5Part1()} = 143")
    println("D5P2: ${day5Part2()} = 123")
}

fun day5Part1() = readLines("aoc2024/day05.txt", delimiter = "\n\n")
    .parseRulesAndUpdates()
    .let { (rules, updates) -> updates.filter { it.hasCorrectOrder(rules) }.sumOf { it[it.size / 2] } }

fun day5Part2() = readLines("aoc2024/day05.txt", delimiter = "\n\n")
    .parseRulesAndUpdates()
    .let { (rules, updates) -> updates.filter { !it.hasCorrectOrder(rules) }.sumOf { it.fixOrder(rules)[it.size / 2] } }

fun List<String>.parseRulesAndUpdates() = Pair(
    this[0].split("\n").map { it.split("|").map { it.toInt() } }, // Rules
    this[1].split("\n").map { it.split(",").map { it.toInt() } }  // Updates
)

fun Update.fixOrder(rules: List<Rule>) = sortedWith { a, b -> if (listOf(a, b).hasCorrectOrder(rules)) -1 else 1 }

fun Update.hasCorrectOrder(rules: List<Rule>): Boolean =
    indices.all { a ->
        indices.all { b ->
            rules.none { (left, right) ->
                (a < b && this[a] == right && this[b] == left) ||
                (a > b && this[a] == left  && this[b] == right)
            }
        }
    }