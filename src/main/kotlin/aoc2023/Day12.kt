package aoc2023

import readLines
import kotlin.math.min

fun main() {
    //println("D12P1: ${day12Part1()} = 7653")
    println("D12P2: ${day12Part2()}")


//    for (i in 1 .. 5)
//        println("$i: " + "???#.????#?? 1,1,1,1".unfold(i).getArrangements())
//    var total = BigDecimal(0)
//    readLines("aoc2023/temp.text").forEach {
//        total = total.add(it.split(" ")[1].toBigDecimal())
//    }
//    println(total)
}

fun day12Part1() = readLines("aoc2023/day12.txt")
    .sumOf { it.getArrangements() }

fun day12Part2() = readLines("aoc2023/day12.txt")
    .take(1)
    .sumOf {
        it.unfold(5).getArrangements()
    }

fun day12Part2Old() = readLines("aoc2023/day12.txt").let {

    var counter = 0
    it.sumOf {
        val arrangements0 = it.unfold(1).getArrangements()
        val arrangements1 = it.unfold(2).getArrangements()



        val multiplier = arrangements1 / arrangements0
        val multFloat = arrangements1.toDouble() / arrangements0.toDouble()
        counter++
        val result = arrangements0 * multiplier * multiplier * multiplier * multiplier
        val result2 = arrangements0.toDouble() * multFloat * multFloat * multFloat * multFloat
        println("$counter: $result ($multFloat = $result2)")
        result
    }
}

private fun String.getArrangements(): Long {

    val (brokenRecord, criteriaString) = this.split(" ")
    val criteria = criteriaString.split(",").map { it.toInt() }
    val brokenSprings = criteria.sum()
    val opSprings = brokenRecord.count { it == '.' }
//    val opSprings = brokenRecord.length - brokenSprings
    val arrangements = mutableSetOf<String>()
    val criteriaSum = criteria.indices.map { criteria.take(it + 1).sum() * 5 }.toIntArray()

    getArrangements(brokenRecord, 0, 0, criteria, criteriaSum, opSprings, brokenSprings, arrangements)

    return arrangements.size.toLong()
}

fun getArrangements(
    record: String,
    iStart: Int,
    criteriaIndex: Int,
    criteria: List<Int>,
    criteriaSum: IntArray,
    opSprings: Int,
    brokenSprings: Int,
    arrangements: MutableSet<String>
) {
    //print(record)

//    var maxBrokenSprings = 0
//    for (i in 0 until  criteriaIndex)
//        maxBrokenSprings += criteria[i]

//    if (record.count { it == '.' } != opSprings || (criteriaIndex > 0 && record.count { it == '#' } > maxBrokenSprings)) {
//        println(" NAH!")
//        return
//    }

//    if (record.count { it == '.' } != opSprings || record.count { it == '#' } > brokenSprings) {
//        //println(" NAH!")
//        return
//    }

//    if (criteriaIndex == criteria.size) {
//        if (record.isValidBasedOn(criteria, brokenSprings)) {
//            arrangements.add(record)
//            // println(" OK")
//        } else {
//            // println(" NOPE")
//        }
//        return
//    }

    // println(" GO ON!")

    val numSprings = criteria[criteriaIndex]
    val maxBroken = criteriaSum[criteriaIndex]
    val springs = "#".repeat(numSprings)
    for (i in iStart until record.length - numSprings + 1) {
        val c = record[i]
        if (c == '.') continue
        val newRecord = record.replaceRange(i, i + numSprings, springs)
//        if (newRecord.count { it == '#' } > maxBroken || newRecord.count { it == '.' } != opSprings) {
//            // println("$newRecord NEI!")
//            continue
//        }

        var numOpSprings = 0
        var numBrokenSprings = 0
        for (j in 0 until newRecord.length) {
            if (newRecord[j] == '.') numOpSprings++
            if (newRecord[j] == '#') numBrokenSprings++
        }
        if (numOpSprings != opSprings || numBrokenSprings > maxBroken) {
            // println("$newRecord NEI!")
            continue
        }
        if (criteriaIndex == criteria.size - 1) {
            if (numBrokenSprings == brokenSprings && record.isValidBasedOn2(criteria)) {
                arrangements.add(record)
                // println(" OK")
            } else {
                // println(" NOPE")
            }
            continue
        }

        getArrangements(newRecord, i + 2, criteriaIndex + 1, criteria, criteriaSum, opSprings, brokenSprings, arrangements)
    }
}

private fun String.isValidBasedOn2(criteria: List<Int>) =
    this.replace('.', '?')
        .replace("??", "?")
        .split("?")
        .mapNotNull { if (it.isNotEmpty()) it.length else null }
        .let { it == criteria }

private fun String.isValidBasedOn(criteria: List<Int>, brokenSprings: Int) =
    this.count { it == '#' } == brokenSprings &&
    this.replace('.', '?')
        .replace("??", "?")
        .split("?")
        .mapNotNull { if (it.isNotEmpty()) it.length else null }
        .let { it == criteria }




private fun String.unfold(times: Int): String =
    this.split(" ").let { (record, criteria) -> ("$record?").repeat(times).dropLast(1) + " " + ("$criteria,").repeat(times).dropLast(1) }

//println(".###.##.#...".isValidBasedOn(listOf(3, 2, 1)))
//println(".###.##..#..".isValidBasedOn(listOf(3, 2, 1)))
//println(".###.##...#.".isValidBasedOn(listOf(3, 2, 1)))
//println(".###.##....#".isValidBasedOn(listOf(3, 2, 1)))
//println(".###..##.#..".isValidBasedOn(listOf(3, 2, 1)))
//println(".###..##..#.".isValidBasedOn(listOf(3, 2, 1)))
//println(".###..##...#".isValidBasedOn(listOf(3, 2, 1)))
//println(".###...##.#.".isValidBasedOn(listOf(3, 2, 1)))
//println(".###...##..#".isValidBasedOn(listOf(3, 2, 1)))
//println(".###....##.#".isValidBasedOn(listOf(3, 2, 1)))