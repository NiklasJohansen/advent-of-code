
fun main() {
    println("D4P1: ${day4Part1()}")
    println("D4P2: ${day4Part2()}")
}

fun day4Part1() = readLines("day04.txt")
    .count { line ->
        val (a, b) = line.toPairs()
        a.contains(b) || b.contains(a)
    }

fun day4Part2() = readLines("day04.txt")
    .count { line ->
        val (a, b) = line.toPairs()
        a.overlaps(b)
    }

private fun String.toPairs() =
    this.split(",").map { Pair(it.substringBefore("-").toInt(), it.substringAfter("-").toInt()) }

private fun Pair<Int, Int>.contains(other: Pair<Int, Int>) =
    (first <= other.first && second >= other.second)

private fun Pair<Int, Int>.overlaps(other: Pair<Int, Int>) =
    !(first > other.second || second < other.first)
