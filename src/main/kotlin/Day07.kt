
fun main() {
    println("D7P1: ${day7Part1()}")
    println("D7P2: ${day7Part2()}")
}

fun day7Part1(): Long {
    val folderSizes = mutableMapOf<String, Long>()
    readLines("day07.txt").unwrapToPaths().getFolderSizes(folderSizes)
    return folderSizes.values.sumOf { if (it <= 100000) it else 0 }
}

fun day7Part2(): Long {
    val folderSizes = mutableMapOf<String, Long>()
    val paths = readLines("day07.txt").unwrapToPaths()
    val usedSpace = paths.getFolderSizes(folderSizes)
    return folderSizes.filter { (70000000 - usedSpace + it.value) >= 30000000 }.minOf { it.value }
}

private fun List<String>.unwrapToPaths(): MutableMap<String, Int> {
    val paths = mutableMapOf<String, Int>()
    var path = ""
    for (line in this) {
        when {
            line.startsWith("$ cd") -> {
                when (val folder = line.substringAfterLast(" ")) {
                    ".." -> path = path.substringBeforeLast("/")
                    "/" -> path = ""
                    else -> path += "/$folder"
                }
            }
            line[0].isDigit() -> line.split(" ").let { (size, file) -> paths["$path/[$file]"] = size.toInt() }
        }
    }
    return paths
}

private fun MutableMap<String, Int>.getFolderSizes(folderSizes: MutableMap<String, Long>, basePath: String = ""): Long {
    var folderSize = 0L
    for ((filePath, size) in this) {
        if (!filePath.startsWith(basePath))
            continue
        val relativePath = filePath.removePrefix("$basePath/")
        if (relativePath.contains("/")) { // Has sub folders
            val subfolder = "$basePath/${relativePath.substringBefore("/")}"
            if (!folderSizes.containsKey(subfolder)) {
                folderSize += this.getFolderSizes(folderSizes, subfolder)
            }
        } else {
            folderSize += size
        }
    }

    folderSizes[basePath] = folderSize
    return folderSize
}