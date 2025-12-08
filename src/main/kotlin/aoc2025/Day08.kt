package aoc2025

import readLines

fun main() {
    println("D8P1: ${day8Part1()} = 40")
    println("D8P2: ${day8Part2()} = 25272")
}

fun day8Part1() = readLines("aoc2025/day08.txt", delimiter = "\n")
    .parseJunctionBoxes()
    .let { boxes ->
        repeat(10 /*1000*/) { boxes.findClosestPair().let { (a,b) -> a.connectTo(b) } }
        boxes.map { it.circuit }.distinct().sortedByDescending { it.size }.take(3).map { it.size }.reduce(Int::times)
    }

fun day8Part2() = readLines("aoc2025/day08.txt", delimiter = "\n")
    .parseJunctionBoxes()
    .let { boxes ->
        while (true) {
            val (a, b) = boxes.findClosestPair()
            a.connectTo(b)
            if (a.circuit.size == boxes.size)
                return@let a.x * b.x
        }
    }

fun List<String>.parseJunctionBoxes() =
    this.map { it.split(",").map { it.toLong() }.let { (x,y,z) -> JunctionBox(x, y, z) } }

fun List<JunctionBox>.findClosestPair(): Pair<JunctionBox, JunctionBox> {
    var aMin = this[0]
    var bMin = this[1]
    var minDist = Long.MAX_VALUE
    for (i in 0 until size - 1) {
        for (j in (i + 1) until size) {
            val a = this[i]
            val b = this[j]
            val dist = a.distanceTo(b)
            if (dist <= minDist && a !in b.directConnections) {
                minDist = dist
                aMin = a
                bMin = b
            }
        }
    }
    return aMin to bMin
}

data class JunctionBox(val x: Long, val y: Long, val z: Long) {
    var circuit = mutableSetOf<JunctionBox>(this)
    val directConnections = mutableSetOf<JunctionBox>()

    fun distanceTo(other: JunctionBox) =
        (x - other.x) * (x - other.x) +
        (y - other.y) * (y - other.y) +
        (z - other.z) * (z - other.z)

    fun connectTo(other: JunctionBox) {
        directConnections += other
        other.directConnections += this
        circuit += other.circuit
        circuit.forEach { it.circuit = circuit }
    }
}