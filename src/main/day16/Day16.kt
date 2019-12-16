package day16

import parse
import kotlin.math.abs

fun main() {
    val input = parse("src/main/day16/input") {
        it[0].toList().map {
            it.toString().toInt()
        }
    }

    //println(part1("80871224585914546619083218645595".toList().map { it.toString().toInt() }, 100))
    println(part1(input, 100))
}

fun part1(input: List<Int>, steps: Int): String {
    val patterns = createPatterns(input.size)

    var tmpInput = input

    repeat(steps) { _ ->

        val output = mutableListOf<Int>()
        repeat(tmpInput.size) { t ->

            val pattern = patterns[t + 1]!!
            output.add(
                    to10Digit(tmpInput.mapIndexed { index, element ->
                        element * pattern[(index + 1) % pattern.size]
                    }.sum())
            )
        }
        tmpInput = output.toList()
    }

    return tmpInput.subList(0, 8).joinToString("")
}

private fun to10Digit(int: Int) = abs(int % 10)

private fun createPatterns(inputCount: Int): Map<Int, List<Int>> {
    val base = listOf(0, 1, 0, -1)
    val map = mutableMapOf<Int, List<Int>>()

    repeat(inputCount) { c ->
        map[c + 1] = base.flatMap { elem -> MutableList(c + 1) { elem } }
    }
    return map
}