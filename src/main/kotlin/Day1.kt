
fun main() {
    println("D1P1: ${day1Part1()}")
    println("D1P2: ${day1Part2()}")
}

fun day1Part1() = readLines("day1.txt", delimiter = "\n\n")
    .maxOf { it.sumLines() }

fun day1Part2() = readLines("day1.txt", delimiter = "\n\n")
    .map { it.sumLines()}
    .sortedDescending()
    .take(3)
    .sum()

private fun String.sumLines() = this.split("\n").sumOf { it.toInt() }