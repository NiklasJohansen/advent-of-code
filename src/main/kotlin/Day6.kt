
fun main() {
    println("D6P1: ${day6Part1()}")
    println("D6P2: ${day6Part2()}")
}
fun day6Part1() = readText("day6.txt").getProcessedCharacters(4)

fun day6Part2() = readText("day6.txt").getProcessedCharacters(14)

fun String.getProcessedCharacters(stride: Int) =
    this.windowed(stride, 1).indexOfFirst { it.toSet().size == stride } + stride
