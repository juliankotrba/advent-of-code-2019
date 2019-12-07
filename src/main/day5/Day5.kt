package day5

import parse
import java.util.*

fun main() {
    val input = parse("src/main/day5/input") {
        it[0].split(",").map { c -> c.toInt() }
    }

    println(Arrays.toString(part1(input.toTypedArray())))
    println(Arrays.toString(part2(input.toTypedArray(), 5)))

}

fun part1(array: Array<Int>): Array<Int> {

    var index = 0
    while (array[index] != 99) {


        when (array[index] % 100) {
            1, 2 -> {
                val (parameter1, parameter2) = getParameters(array, index)
                val loc = array[index.plus(3)]

                when (array[index] % 100) {
                    1 -> array[loc] = parameter1 + parameter2
                    2 -> array[loc] = parameter1 * parameter2
                }

                index += 4
            }

            3 -> {
                val loc = array[index.plus(1)]
                val input = readLine()!!.toInt()
                array[loc] = input
                index += 2
            }
            4 -> {
                val loc = array[index.plus(1)]
                println(array[loc])
                index += 2
            }

        }

    }
    return array
}

fun part2(array: Array<Int>, input: Int): Array<Int> {

    var index = 0
    while (array[index] != 99) {

        when (array[index] % 100) {
            1, 2 -> {
                val (parameter1, parameter2) = getParameters(array, index)
                val loc = array[index.plus(3)]
                when (array[index] % 100) {
                    1 -> array[loc] = parameter1 + parameter2
                    2 -> array[loc] = parameter1 * parameter2
                }

                index += 4
            }

            3 -> {
                val loc = array[index.plus(1)]
                array[loc] = input
                index += 2
            }
            4 -> {
                println(getParameter(array, index))
                index += 2
            }
            5, 6 -> {

                val (parameter1, parameter2) = getParameters(array, index)

                if (parameter1 != 0 && (array[index] % 100) == 5) {
                    index = parameter2
                } else if (parameter1 == 0 && (array[index] % 100) == 6) {
                    index = parameter2
                } else {
                    index += 3
                }
            }
            7, 8 -> {
                val (parameter1, parameter2) = getParameters(array, index)

                val loc = array[index.plus(3)]
                if (parameter1 < parameter2 && (array[index] % 100) == 7) {
                    array[loc] = 1
                } else if (parameter1 == parameter2 && (array[index] % 100) == 8) {
                    array[loc] = 1
                } else {
                    array[loc] = 0
                }

                index += 4
            }
        }

    }
    return array
}

private fun getParameter(array: Array<Int>, index: Int): Int {
    val modeParam1 = array[index].div(100).rem(10)

    val op1 = array[index.plus(1)]

    return if (modeParam1 == 0) {
        array[op1]
    } else {
        op1
    }
}

private fun getParameters(array: Array<Int>, index: Int): Pair<Int, Int> {
    val modeParam1 = array[index].div(100).rem(10)
    val modeParam2 = array[index].div(1000).rem(10)

    val op1 = array[index.plus(1)]
    val op2 = array[index.plus(2)]


    val parameter1 = if (modeParam1 == 0) {
        array[op1]
    } else {
        op1
    }

    val parameter2 = if (modeParam2 == 0) {
        array[op2]
    } else {
        op2
    }

    return Pair(parameter1, parameter2)
}