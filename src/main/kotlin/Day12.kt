
fun main() {
    println("D12P1: ${day12Part1()}")
    println("D12P2: ${day12Part2()}")
}

fun day12Part1(): Int {
    val lines = readLines("day12.txt", delimiter = "\n")
    val (xStart, yStart) = lines.getPositionOf('S')
    val (xEnd, yEnd) = lines.getPositionOf('E')
    val heightMap = lines.removeStartAndStopPoints()
    return heightMap.getShortestPathLength(xStart, yStart, xEnd, yEnd)
}

fun day12Part2(): Int {
    val lines = readLines("day12.txt", delimiter = "\n")
    val (xEnd, yEnd) = lines.getPositionOf('E')
    val heightMap = lines.removeStartAndStopPoints()
    return lines.flatMapIndexed { yStart, row ->
        row.mapIndexedNotNull { xStart, height ->
            if (height == 'a' || height == 'S') heightMap.getShortestPathLength(xStart, yStart, xEnd, yEnd) else null
        }
    }.filter { it > 0 }.min()
}

fun List<String>.getShortestPathLength(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int): Int {
    val mapWidth = this[0].length
    val mapHeight = this.size
    val visited = BooleanArray(mapWidth * mapHeight)
    val cells = ArrayDeque<Cell>().apply { add(Cell(xStart, yStart, 0)) }

    // Perform breadth first search
    while (cells.isNotEmpty()) {
        val cell = cells.removeFirst()
        if (cell.x == xEnd && cell.y == yEnd)
            return cell.pathLength // Found destination!

        if (visited[cell.y * mapWidth + cell.x])
            continue // Been there, done that

        for (dir in directions) {
            val (xDir, yDir) = dir
            val x = cell.x + xDir
            val y = cell.y + yDir
            if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight && this[y][x] - this[cell.y][cell.x] <= 1)
                cells.add(Cell(x, y, cell.pathLength + 1))
        }

        visited[cell.y * mapWidth + cell.x] = true
    }

    return -1 // End point not reachable
}

private fun List<String>.getPositionOf(c: Char): Pair<Int, Int> {
    val y = this.indexOfFirst { it.contains(c) }
    val x = this[y].indexOf(c)
    return Pair(x, y)
}

private fun List<String>.removeStartAndStopPoints(): List<String> =
    this.map { it.replace('S', 'a').replace('E', 'z') }

private class Cell(val x: Int, val y: Int, var pathLength: Int)

private val directions = listOf(
    Pair(1, 0),
    Pair(0, 1),
    Pair(0, -1),
    Pair(-1, 0)
)