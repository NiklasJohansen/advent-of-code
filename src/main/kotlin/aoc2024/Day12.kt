package aoc2024

import aoc2024.Side.*
import readLines

fun main() {
    println("D12P1: ${day12Part1()} = 1930")
    println("D12P2: ${day12Part2()} = 1206")
}

fun day12Part1() = readLines("aoc2024/day12.txt")
    .let { garden -> garden.calculateFencePrice { area, perimeters -> area * perimeters.size } }

fun day12Part2() = readLines("aoc2024/day12.txt")
    .let { garden -> garden.calculateFencePrice { area, perimeters -> area * perimeters.getSideCount(garden) } }

private fun List<String>.calculateFencePrice(priceFunction: (area: Int, perimeters: Set<Perimeter>) -> Int): Int {
    val plots = indices.flatMap { y -> this[y].indices.map { x -> Plot(x, y) } }
    val searchedPlots = mutableSetOf<Plot>()
    return plots.sumOf { plot ->
        val perimeters = mutableSetOf<Perimeter>()
        val area = getRegionAreaAndPerimeter(plot, getType(plot), searchedPlots, perimeters)
        priceFunction(area, perimeters)
    }
}

private fun List<String>.getRegionAreaAndPerimeter(plot: Plot, type: Char?, searchedPlots: MutableSet<Plot>, perimeters: MutableSet<Perimeter>): Int {
    if (getType(plot) != type || plot in searchedPlots)
        return 0

    searchedPlots += plot

    if (getType(plot.left())  != type) perimeters += Perimeter(plot.left(),  LEFT)
    if (getType(plot.right()) != type) perimeters += Perimeter(plot.right(), RIGHT)
    if (getType(plot.over())  != type) perimeters += Perimeter(plot.over(),  OVER)
    if (getType(plot.under()) != type) perimeters += Perimeter(plot.under(), UNDER)

    return getRegionAreaAndPerimeter(plot.left(),  type, searchedPlots, perimeters) +
           getRegionAreaAndPerimeter(plot.right(), type, searchedPlots, perimeters) +
           getRegionAreaAndPerimeter(plot.over(),  type, searchedPlots, perimeters) +
           getRegionAreaAndPerimeter(plot.under(), type, searchedPlots, perimeters) + 1
}

private fun Set<Perimeter>.getSideCount(garden: List<String>): Int {
    val width = garden[0].length
    val height = garden.size
    var sides = 0

    for (side in listOf(OVER, UNDER)) { // Count horizontal sides
        for (y in -1 .. height) {
            var last = getPerimeter(x = 0, y, side)
            for (x in 1 .. width) {
                val perimeter = getPerimeter(x, y, side)
                if (perimeter == null && last != null) sides++
                last = perimeter
            }
        }
    }

    for (side in listOf(LEFT, RIGHT)) { // Count vertical sides
        for (x in -1 .. width) {
            var last = getPerimeter(x, y = 0, side)
            for (y in 1 .. height) {
                val perimeter = getPerimeter(x, y, side)
                if (perimeter == null && last != null) sides++
                last = perimeter
            }
        }
    }

    return sides
}

private fun List<String>.getType(plot: Plot) = getOrNull(plot.y)?.getOrNull(plot.x)

private fun Set<Perimeter>.getPerimeter(x: Int, y: Int, side: Side) = Perimeter(Plot(x, y), side).takeIf { it in this }

private data class Perimeter(val plot: Plot, val side: Side)

private enum class Side { OVER, UNDER, LEFT, RIGHT }

private data class Plot(val x: Int, val y: Int) {
    fun left()  = Plot(x - 1, y)
    fun right() = Plot(x + 1, y)
    fun over()  = Plot(x, y - 1)
    fun under() = Plot(x, y + 1)
}