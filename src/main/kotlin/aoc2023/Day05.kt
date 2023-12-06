package aoc2023

import readText

fun main() {
    println("D5P1: ${day5Part1()}")
    println("D5P2: ${day5Part2()}")
}

fun day5Part1() = readText("aoc2023/day05.txt")
    .getAlmanac()
    .findLowestLocation()

fun day5Part2() = readText("aoc2023/day05.txt")
    .getAlmanac(seedsAsRanges = true)
    .findLowestLocation()

private fun String.getAlmanac(seedsAsRanges: Boolean = false): Almanac {
    val sections = split("\n\n")
    val seeds = sections.first().split(":", " ").drop(2).map { it.toLong() }
    val seedRanges =
        if (seedsAsRanges) seeds.chunked(2).map { (start, length) -> Range(start, start + length) }
        else seeds.map { seed -> Range(seed, seed) }
    val mappingRanges = sections.drop(1).map { section ->
        section.split("\n").drop(1).map { it.split(" ").map { it.toLong() }.let { (dst, src, length) ->
            Range(from = src, to = src + length, delta = dst - src) }
        }
    }
    return Almanac(seedRanges, mappingRanges)
}

private fun Almanac.findLowestLocation() = seedRanges
    .minOf { range ->
        LongRange(range.from, range.to).minOf { seed ->
            var number = seed
            for (ranges in mappingsRanges)
                number += ranges.find { number >= it.from && number < it.to }?.delta ?: 0
            number
        }
    }

private data class Range(val from: Long, val to: Long, val delta: Long = 0)
private data class Almanac(val seedRanges: List<Range>, val mappingsRanges: List<List<Range>>)