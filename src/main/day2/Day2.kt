package day2

import parse
import kotlin.RuntimeException

fun main() {
    val input = parse("src/main/day2/input") {
        it[0].split(",").map { c -> c.toInt() }
    }

    println(part1(input.toTypedArray(), 12, 2)[0])
    println(part2(input.toTypedArray()))
}

fun part1(input: Array<Int>, r1: Int, r2: Int): Array<Int> {
    val array = input.copyOf()

    array[1] = r1
    array[2] = r2

    var index = 0
    while (array[index] != 99) {

        val op1 = array[index.plus(1)]
        val op2 = array[index.plus(2)]
        val loc = array[index.plus(3)]

        when (array[index]) {
            1 -> {
                array[loc] = array[op1] + array[op2]
            }
            2 -> {
                array[loc] = array[op1] * array[op2]
            }
        }
        index += 4
    }
    return array
}

fun part2(input: Array<Int>): Int {
    for (noun in 99 downTo 0) {
        for (verb in 99 downTo 0) {
            if (part1(input, noun, verb)[0] == 19690720) {
                return (100 * noun) + verb
            }
        }
    }

    throw RuntimeException("No verb, noun pair found!")
}