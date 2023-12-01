package aoc2022

import readLines

fun main() {
    println("D19P1: ${day19Part1()}")
    println("D19P2: ${day19Part2()}")
}

fun day19Part1() = readLines("aoc2022/day19.txt")
    .map { Blueprint.from(it) }
    .sumOf { blueprint -> blueprint.id * getMaxNumberOfGeodes(blueprint, totalMinutes = 24) }

fun day19Part2() = readLines("aoc2022/day19.txt")
    .take(3)
    .map { getMaxNumberOfGeodes(blueprint = Blueprint.from(it), totalMinutes = 32) }
    .reduce(Int::times)

private fun getMaxNumberOfGeodes(blueprint: Blueprint, totalMinutes: Int): Int {
    val states = ArrayDeque<State>().apply { add(State(oreRobots = 1)) }
    val completedStates = mutableSetOf<State>()
    val topGeodes = mutableMapOf<Int, Int>() // Minute -> geodes

    while (states.isNotEmpty()) {
        val state = states.removeFirst()
        val top = topGeodes[state.minute]
        if (top == null || top < state.geodes)
            topGeodes[state.minute] = state.geodes

        if (state.minute > totalMinutes || (top ?: 0) > state.geodes + state.geodeRobots || state in completedStates)
            continue

        val nextStates = listOfNotNull(
            state.withGeodeRobotOrNull(blueprint),
            state.withObsidianRobotOrNull(blueprint),
            state.withClayRobotOrNull(blueprint),
            state.withOreRobotOrNull(blueprint),
            state.nextMinute() // Buy nothing
        )

        states.addAll(nextStates)
        completedStates.add(state)
    }

    return topGeodes.values.max()
}

private data class State(
    var minute: Int = 1,
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geodes: Int = 0,
    var oreRobots: Int = 0,
    var clayRobots: Int = 0,
    var obsidianRobots: Int  = 0,
    var geodeRobots: Int = 0
) {

    fun nextMinute() = State(
        minute = minute + 1,
        ore = ore + oreRobots,
        clay = clay + clayRobots,
        obsidian = obsidian + obsidianRobots,
        geodes = geodes + geodeRobots,
        oreRobots = oreRobots,
        clayRobots = clayRobots,
        obsidianRobots = obsidianRobots,
        geodeRobots = geodeRobots
    )

    fun withGeodeRobotOrNull(bp: Blueprint) =
        if (!canBuyGeodeRobot(bp)) null
        else nextMinute().apply {
            ore -= bp.geodeRobotCost.first
            obsidian -= bp.geodeRobotCost.second
            geodeRobots++
        }

    fun withObsidianRobotOrNull(bp: Blueprint) =
        if (!canBuyObsidianRobot(bp)) null
        else nextMinute().apply {
            ore -= bp.obsidianRobotCost.first
            clay -= bp.obsidianRobotCost.second
            obsidianRobots++
        }

    fun withClayRobotOrNull(bp: Blueprint) =
        if (!canBuyClayRobot(bp)) null
        else nextMinute().apply {
            ore -= bp.clayRobotCost
            clayRobots++
        }

    fun withOreRobotOrNull(bp: Blueprint) =
        if (!canBuyOreRobot(bp)) null
        else nextMinute().apply {
            ore -= bp.oreRobotCost
            oreRobots++
        }

    fun canBuyGeodeRobot(bp: Blueprint) =
        ore >= bp.geodeRobotCost.first && obsidian >= bp.geodeRobotCost.second && obsidianRobots < bp.maxObsidianRobots

    fun canBuyObsidianRobot(bp: Blueprint) =
        ore >= bp.obsidianRobotCost.first && clay >= bp.obsidianRobotCost.second

    fun canBuyClayRobot(bp: Blueprint) =
        ore >= bp.clayRobotCost && clayRobots < bp.maxClayRobots

    fun canBuyOreRobot(bp: Blueprint) =
        ore >= bp.oreRobotCost && oreRobots < bp.maxOreRobots
}

private data class Blueprint(
    val id: Int,
    val oreRobotCost: Int, // Ore
    val clayRobotCost: Int, // Ore
    val obsidianRobotCost: Pair<Int, Int>, // Ore, Clay
    val geodeRobotCost: Pair<Int, Int> // Ore, Obsidian
) {
    val maxOreRobots = listOf(oreRobotCost, clayRobotCost, obsidianRobotCost.first, geodeRobotCost.first).max()
    val maxClayRobots = obsidianRobotCost.second
    val maxObsidianRobots = geodeRobotCost.second

    companion object {
        fun from(line: String) = line.split(" ").let { words ->
            Blueprint(
                id = words[1].extractNumber(),
                oreRobotCost = words[6].toInt(),
                clayRobotCost = words[12].toInt(),
                obsidianRobotCost = Pair(words[18].toInt(), words[21].toInt()),
                geodeRobotCost = Pair(words[27].toInt(), words[30].toInt())
            )
        }
    }
}

private fun String.extractNumber() = this.filter { it.isDigit() }.toInt()