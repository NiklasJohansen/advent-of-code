package aoc2022

import readLines
import java.lang.Integer.min

fun main() {
    println("D13P1: ${day13Part1()}")
    println("D13P2: ${day13Part2()}")
}

fun day13Part1() = readLines("aoc2022/day13.txt", delimiter = "\n\n")
    .getPacketPairs()
    .mapIndexed { i, packetPair -> if (packetPair.isInRightOrder() == true) i + 1 else 0 }
    .sum()

fun day13Part2() = readLines("aoc2022/day13.txt", delimiter = "\n")
    .filterNot { it.isBlank() }
    .plus(listOf("[[2]]", "[[6]]"))
    .map { it.toPacketList() }
    .sortedWith { left, right -> PacketPair(left, right).isInRightOrder()?.let { if (it) -1 else 1 } ?: 0 }
    .map { it.toString() }
    .let { (it.indexOf("[[2]]") + 1) * (it.indexOf("[[6]]") + 1) }

private fun List<String>.getPacketPairs() =
    this.map { pair -> pair.split("\n").map { it.toPacketList() }.let { PacketPair(it[0], it[1]) } }

private fun String.toPacketList(): List<Any> {
    val packetLists = mutableListOf<Any>()
    var brackets = 0
    var text = ""
    for (c in this.removePrefix("[").removeSuffix("]")) {
        if (c == '[') {
            text += c
            brackets++
        } else if (c == ']') {
            text += c
            brackets--
            if (brackets == 0) {
                packetLists.add(text.toPacketList())
                text = ""
            }
        } else if (brackets > 0 || c.isDigit()) {
            text += c
        } else if (text.isNotEmpty()) {
            packetLists.add(text.toInt())
            text = ""
        }
    }

    if (text.isNotEmpty()) packetLists.add(text.toInt())

    return packetLists
}

private fun PacketPair.isInRightOrder(): Boolean? {
    for (i in 0 until min(leftPacket.size, rightPacket.size)) {
        val left = leftPacket[i]
        val right = rightPacket[i]
        if (left is Int && right is Int) {
            if (left < right)
                return true
            else if (left > right)
                return false
        } else {
            val l = if (left is Int) listOf(left) else left as List<Any>
            val r = if (right is Int) listOf(right) else right as List<Any>
            PacketPair(l, r).isInRightOrder()?.let { return it }
        }
    }
    return if (leftPacket.size != rightPacket.size) leftPacket.size < rightPacket.size else null
}

private data class PacketPair(val leftPacket: List<Any>, val rightPacket: List<Any>)