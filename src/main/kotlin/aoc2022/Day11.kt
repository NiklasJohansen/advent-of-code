package aoc2022

import readLines

fun main() {
    println("D11P1: ${day11Part1()}")
    println("D11P2: ${day11Part2()}")
}

fun day11Part1() = readLines("aoc2022/day11.txt", delimiter = "\n\n")
    .map { Monkey.from(it) }
    .performMonkeyBusiness(rounds = 20, divideWorryLevel = true, limitRange = false)

fun day11Part2() = readLines("aoc2022/day11.txt", delimiter = "\n\n")
    .map { Monkey.from(it) }
    .performMonkeyBusiness(rounds = 10000, divideWorryLevel = false, limitRange = true)

private fun List<Monkey>.performMonkeyBusiness(rounds: Int, divideWorryLevel: Boolean, limitRange: Boolean): Long {
    val lcm = this.map { it.divider }.reduce { acc, divider -> acc * divider }
    repeat(rounds) {
        for (monkey in this) {
            for (item in monkey.items) {
                monkey.itemsInspected++
                monkey.operation.performOn(item)
                if (limitRange)
                    item.worryLevel %= lcm
                if (divideWorryLevel)
                    item.worryLevel /= 3
                val targetMonkey = if (item.worryLevel % monkey.divider == 0L) monkey.trueMonkey else monkey.falseMonkey
                this.find { it.name == targetMonkey }?.items?.add(item)
            }
            monkey.items.clear()
        }
    }

    return this.sortedByDescending { it.itemsInspected }.let { it[0].itemsInspected * it[1].itemsInspected }
}

private data class Item(var worryLevel: Long)

private data class Operation(val operator: String, val value: Long?) {
    fun getValue(item: Item) = value ?: item.worryLevel
    fun performOn(item: Item) =
        if (operator == "*") item.worryLevel *= getValue(item) else item.worryLevel += getValue(item)
}

private data class Monkey(
    val name: String,
    val items: MutableList<Item>,
    val operation: Operation,
    val divider: Long,
    val trueMonkey: String,
    val falseMonkey: String,
    var itemsInspected: Long = 0
) {
    companion object {
        fun from(definition: String): Monkey {
            val lines = definition.split("\n")
            return Monkey(
                name = lines[0].dropLast(1).lowercase(),
                items = lines[1].substringAfter("items: ").split(", ").map { Item(it.toLong()) }.toMutableList(),
                operation = lines[2].substringAfter("old ").split(" ").let { Operation(it[0], it[1].toLongOrNull()) },
                divider = lines[3].substringAfter("by ").toLong(),
                trueMonkey = lines[4].substringAfter("throw to "),
                falseMonkey = lines[5].substringAfter("throw to ")
            )
        }
    }
}