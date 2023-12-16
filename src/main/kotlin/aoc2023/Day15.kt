package aoc2023

import readLines
import readText

fun main() {
    println("D15P1: ${day15Part1()}")
    println("D15P2: ${day15Part2()}")
}

fun day15Part1() = readLines("aoc2023/day15.txt")
    .first().split(",").sumOf { it.getHashCode()  }

fun day15Part2() = readText("aoc2023/day15.txt")
    .getFocusingPower()

private fun String.getFocusingPower(): Int {
    val boxes = Array(256) { mutableListOf<Lens>() }
    for (step in this.split(",")) {
        val label = step.split("-", "=").first()
        val operation = step.first { it == '-' || it == '=' }
        val box = boxes[label.getHashCode()]
        when (operation) {
            '-' -> box.removeIf { it.label == label }
            '=' -> {
                val lens = Lens(label, focalLength = step.last().digitToInt())
                val index = box.indexOfFirst { it.label == label }
                if (index != -1) box[index] = lens else box.add(lens)
            }
        }
    }
    return boxes.mapIndexed { box, lenses -> lenses.mapIndexed { slot, lens -> (box + 1) * (slot + 1) * lens.focalLength }.sum() }.sum()
}

private fun String.getHashCode(): Int =
    this.fold(0) { hash, c -> (hash + c.code) * 17 % 256 }

data class Lens(val label: String, val focalLength: Int)