
fun main() {
    println("D16P1: ${day16Part1()}")
    println("D16P2: ${day16Part2()}")
}

fun day16Part1(): Int {
    val valves = readLines("day16.txt").parseValves()
    val startValve = valves.first { it.name == "AA" }
    val shortestValvePaths = valves.getShortestPathsBetweenWorkingValves(startValve)
    val pathsWithPressure = startValve.findHighestPressurePaths(shortestValvePaths, minutesLeft = 30)
    return pathsWithPressure.values.max()
}

fun day16Part2(): Int {
    val valves = readLines("day16.txt").parseValves()
    val startValve = valves.first { it.name == "AA" }
    val shortestValvePaths = valves.getShortestPathsBetweenWorkingValves(startValve)
    val pathsWithPressure = startValve.findHighestPressurePaths(shortestValvePaths, minutesLeft = 26)
    return pathsWithPressure.findHighestPressureSumAmongstTwoSeparatePaths()
}

private fun List<String>.parseValves(): List<Valve> {
    val lineWords = this.map { it.split(" ") }
    val valves = lineWords.map { words -> Valve(name = words[1], flowRate = words[4].filter { it.isDigit() }.toInt()) }
    lineWords.forEach { words ->
        val connectedValveNames = words.drop(9).map { it.take(2) }
        valves.find { it.name == words[1] }?.connectsTo?.addAll(valves.filter { it.name in connectedValveNames })
    }
    return valves
}

private fun List<Valve>.getShortestPathsBetweenWorkingValves(startValve: Valve): List<Path> {
    val workingValves = this.filter { it.flowRate > 0 }
    return workingValves
        .plus(startValve)
        .flatMap { start -> workingValves.filter { it !== start }.mapNotNull { end -> findShortestPath(start, end) } }
}

private fun findShortestPath(from: Valve, to: Valve): Path? {
    data class Node(val valves: List<Valve>)
    val visited = mutableSetOf<Valve>()
    val nodes = ArrayDeque<Node>().apply { add(Node(valves = listOf(from))) }
    while (nodes.isNotEmpty()) {
        val node = nodes.removeFirst()
        val toValve = node.valves.last()
        when {
            toValve === to -> return Path(node.valves.first(), toValve, length = node.valves.size - 1)
            toValve in visited -> continue
        }
        nodes.addAll(toValve.connectsTo.map { Node(node.valves + it) })
        visited.add(toValve)
    }
    return null // No path
}

private fun Valve.findHighestPressurePaths(
    valvePaths: List<Path>,
    minutesLeft: Int,
    openedValvePath: String = "",
    currentPressure: Int = 0,
    foundPaths: MutableMap<String, Int> = mutableMapOf()
): MutableMap<String, Int> {
    val pressure = currentPressure + this.flowRate * minutesLeft
    if (pressure > (foundPaths[openedValvePath] ?: 0))
        foundPaths[openedValvePath] = pressure

    if (minutesLeft <= 0)
        return foundPaths

    val relevantPaths = valvePaths.filter { this === it.fromValve && it.toValve.name !in openedValvePath }
    for (path in relevantPaths) {
        path.toValve.findHighestPressurePaths(
            valvePaths = valvePaths,
            minutesLeft = minutesLeft - (path.length + 1), // travel time + open valve time
            openedValvePath = openedValvePath + path.toValve.name,
            currentPressure = pressure,
            foundPaths = foundPaths
        )
    }

    return foundPaths
}

private fun Map<String, Int>.findHighestPressureSumAmongstTwoSeparatePaths(): Int {
    val pathResults = this
        .map { (path, pressure) -> PathResult(path.chunked(2).toSet(), pressure)}
        .sortedByDescending { it.totalPressure }

    var maxPressure = 0
    for ((i, aPath) in pathResults.withIndex()) {
        for (j in i + 1 until pathResults.size) {
            val bPath = pathResults[j]
            val pressureSum = aPath.totalPressure + bPath.totalPressure
            if (pressureSum <= maxPressure) {
                break
            } else if (aPath.valveNames.none { it in bPath.valveNames }) {
                // A path visits none of B path (elf and elephant tok totally separate routes)
                maxPressure = pressureSum
                break
            }
        }
    }

    return maxPressure
}

private data class Path(val fromValve: Valve, val toValve: Valve, val length: Int)
private data class PathResult(val valveNames: Set<String>, var totalPressure: Int = 0)
private data class Valve(val name: String, val flowRate: Int) {
    val connectsTo: MutableList<Valve> = mutableListOf()
}