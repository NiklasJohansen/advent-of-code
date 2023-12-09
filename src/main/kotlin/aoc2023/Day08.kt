package aoc2023

import readLines

fun main() {
    println("D8P1: ${day8Part1()}")
    println("D8P2: ${day8Part2()}")
}

fun day8Part1() = readLines("aoc2023/day08.txt")
    .parseMap()
    .let { map -> map.nodes.first { it.label == "AAA" }.getStepsToNodeWithLabelEndingIn("ZZZ", map) }

fun day8Part2() = readLines("aoc2023/day08.txt")
    .parseMap()
    .let { map -> map.nodes
        .filter { it.label.endsWith("A") }
        .map { it.getStepsToNodeWithLabelEndingIn("Z", map) }
        .reduce { acc, steps -> lcm(acc, steps) }
    }

private fun Node.getStepsToNodeWithLabelEndingIn(value: String, map: Map): Long {
    var steps = 0L
    var node = this
    val nodeMap = map.nodes.associateBy { it.label }
    while (!node.label.endsWith(value)) {
        val instruction = map.instructions[(steps % map.instructions.length).toInt()]
        val nextNodeLabel = if (instruction == 'L') node.leftNodeLabel else node.rightNodeLabel
        node = nodeMap[nextNodeLabel]!!
        steps++
    }
    return steps
}

private fun List<String>.parseMap(): Map =
    Map(this[0], this.drop(2).map { line -> line.split(" ", "(", ",", ")").let { Node(it[0], it[3], it[5]) } } )

private fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

private data class Map(val instructions: String, val nodes: List<Node>)
private data class Node(val label: String, val leftNodeLabel: String, val rightNodeLabel: String)