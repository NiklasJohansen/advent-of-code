import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("D15P1: ${day15Part1() == 5147333}")
    println("D15P2: ${day15Part2() == 13734006908372L}")
}

fun day15Part1() = readLines("day15.txt")
    .parseMap()
    .get(2000000)!!
    .sumOf { it.xMax - it.xMin + 1 } - 1 // Subtract 1 to account for the one beacon inside the area

fun day15Part2() = readLines("day15.txt")
    .parseMap()
    .findTuningFrequencyOfBeacon()

private fun List<String>.parseMap(): MutableMap<Int, MutableList<Range>> {
    val map = mutableMapOf<Int, MutableList<Range>>()
    for (line in this) {
        val words = line.split(" ")
        val xSensor = words[2].filter { it.isDigit() }.toInt()
        val ySensor = words[3].filter { it.isDigit() }.toInt()
        val xBeacon = words[8].filter { it.isDigit() }.toInt()
        val yBeacon = words[9].filter { it.isDigit() }.toInt()
        val distance = abs(xSensor - xBeacon) + abs(ySensor - yBeacon) // Manhattan distance
        for (d in -distance until distance + 1) {
            val xStart = xSensor - distance + abs(d)
            val xEnd = xSensor + distance - abs(d)
            map.getOrPut(ySensor + d) { mutableListOf() }.add(Range(xStart, xEnd))
        }
    }
    return map.collapseOverlappingRanges()
}

private fun MutableMap<Int, MutableList<Range>>.collapseOverlappingRanges(): MutableMap<Int, MutableList<Range>> {
    for (ranges in this.values) {
        var i = 0
        while (i < ranges.size) {
            for (j in i + 1 until ranges.size) {
                val r0 = ranges[i]
                val r1 = ranges[j]
                if (r0.overlapsWith(r1)) {
                    r0.xMin = min(r0.xMin, r1.xMin)
                    r0.xMax = max(r0.xMax, r1.xMax)
                    ranges.remove(r1)
                    i = -1
                    break
                }
            }
            i++
        }
        ranges.sortBy { it.xMin }
    }
    return this
}

private fun MutableMap<Int, MutableList<Range>>.findTuningFrequencyOfBeacon(): Long {
    for ((y, ranges) in this) {
        var lastRange = ranges[0]
        for (thisRange in ranges.drop(1)) {
            if (lastRange.getOpensSpotsUntil(thisRange) == 1) {
                // Make sure the row over and under this is fully covered to make this a closed-in spot
                val x = lastRange.xMax + 1
                val isTopRowCovered = this[y - 1]?.any { it.xMin < x && it.xMax > x } == true
                val isBottomRowCovered = this[y + 1]?.any { it.xMin < x && it.xMax > x } == true
                if (isTopRowCovered && isBottomRowCovered)
                    return x * 4000000L + y
            }
            lastRange = thisRange
        }
    }
    return -1 // Found no distress beacon :(
}

private data class Range(var xMin: Int, var xMax: Int) {
    fun overlapsWith(range: Range) = (xMax >= range.xMin && xMin <= range.xMax)
    fun getOpensSpotsUntil(range: Range) = (range.xMin - xMax - 1)
}