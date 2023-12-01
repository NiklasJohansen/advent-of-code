package aoc2022

import readLines

fun main() {
    println("D5P1: ${day5Part1()}")
    println("D5P2: ${day5Part2()}")
}

fun day5Part1() = readStacksAndMoves("aoc2022/day05.txt")
    .also { (stacks, moves) -> moves.forEach { it.executeOn(stacks) } }
    .let { (stacks, _) -> stacks.joinToString("") { it.first().toString() } }

fun day5Part2() = readStacksAndMoves("aoc2022/day05.txt")
    .also { (stacks, moves) -> moves.forEach { it.executeOn(stacks, retainMoveOrder = true) } }
    .let { (stacks, _) -> stacks.joinToString("") { it.first().toString() } }

private fun readStacksAndMoves(path: String) =
    readLines(path, delimiter = "\n\n").let { (stacks, moves) -> Pair(stacks.toStackList(), moves.toMoveList()) }

private fun String.toStackList(): List<ArrayDeque<Char>> {
    val lines = this.split("\n").dropLast(1) // Drop last number row
    val numberOfStacks = this.substringAfterLast(" ").toInt() // Read last value in number row
    return MutableList(numberOfStacks) { stackIndex ->
        ArrayDeque<Char>().also { stack ->
            stack.addAll(lines.mapNotNull { it.getOrNull(1 + stackIndex * 4)?.takeIf { it != ' ' } })
        }
    }
}

private fun String.toMoveList(): List<Move> =
    this.split("\n").map { Move(
        quantity = it.substringAfter(" ").substringBefore(" ").toInt(),
        from = it.substringAfter("from ").substringBefore(" ").toInt() - 1,
        to = it.substringAfterLast(" ").toInt() - 1
    ) }

private data class Move(
    val quantity: Int,
    val from: Int,
    val to: Int
) {
    fun executeOn(stacks: List<ArrayDeque<Char>>, retainMoveOrder: Boolean = false) =
        repeat(quantity) { i -> stacks[to].add(if (retainMoveOrder) i else 0, stacks[from].removeFirst()) }
}