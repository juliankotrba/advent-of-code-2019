package day17

import parse
import kotlin.math.pow

typealias Position = Pair<Int, Int>

fun Position.up() = this.copy(first, second + 1)
fun Position.down() = this.copy(first, second - 1)
fun Position.right() = this.copy(first + 1, second)
fun Position.left() = this.copy(first - 1, second)

fun main() {
    val input = parse("src/main/day17/input") {
        it[0].split(",").map { c -> c.toInt() }.toTypedArray()
    }

    val extendedInput = input + Array(10000) { 0 } // TODO: Dynamic length computation

    println(part1(extendedInput))
}

fun part1(input: Array<Int>): Int {
    val scaffoldPoints = mutableSetOf<Position>()
    var state = IntComputerState(false, input, 0, 0, -1)

    var line = 0 // y
    var column = 0 // x

    var done = false
    while (!done) {

        state = intComputer(state, -1)

        when (state.output) {
            35 -> {
                scaffoldPoints.add(Pair(column, line))
                column++
            }
            10 -> {
                line++
                column = 0
            }
            46 -> column++
        }
        done = state.done
    }

    println(scaffoldPoints.size)

    /*// Print
    for (y in 0 until line) {
        for (x in 0 until scaffoldPoints.map { it.first }.max()!!.plus(2)) {
            if (scaffoldPoints.contains(Pair(x,y))) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }*/

    return scaffoldPoints.filter {
        scaffoldPoints.contains(it.up())
                && scaffoldPoints.contains(it.right())
                && scaffoldPoints.contains(it.down())
                && scaffoldPoints.contains(it.left())
    }.map { it.first * it.second }.sum()
}

data class IntComputerState(
        val done: Boolean,
        val array: Array<Int>,
        val index: Int,
        val base: Int,
        val output: Int
)

fun intComputer(state: IntComputerState, input: Int): IntComputerState {
    var index = state.index
    var base = state.base
    val array = state.array


    while (array[index] != 99) {

        when (array[index] % 100) {
            1, 2 -> {

                val (parameter1, parameter2) = getParameters(array, index, base)
                val addr = getAddressParameter(array, index, base, 3)

                when (array[index] % 100) {
                    1 -> array[addr] = parameter1 + parameter2
                    2 -> array[addr] = parameter1 * parameter2
                }

                index += 4
            }

            3 -> {

                val addr = getAddressParameter(array, index, base, 1)
                println("Please enter input:")
                array[addr] = input // readLine()!!.toLong()
                index += 2
            }
            4 -> {

                val output = getParameter(array, index, base)
                index += 2
                return IntComputerState(false, array, index, base, output)
            }
            5, 6 -> {

                val (parameter1, parameter2) = getParameters(array, index, base)

                if (parameter1 != 0 && (array[index] % 100) == 5) {
                    index = parameter2.toInt()
                } else if (parameter1 == 0 && (array[index] % 100) == 6) {
                    index = parameter2.toInt()
                } else {
                    index += 3
                }
            }
            7, 8 -> {
                val (parameter1, parameter2) = getParameters(array, index, base)
                val addr = getAddressParameter(array, index, base, 3)

                if (parameter1 < parameter2 && (array[index] % 100) == 7) {
                    array[addr] = 1
                } else if (parameter1 == parameter2 && (array[index] % 100) == 8) {
                    array[addr] = 1
                } else {
                    array[addr] = 0
                }

                index += 4
            }

            9 -> {
                val parameter = getParameter(array, index, base)
                base += parameter
                index += 2
            }
        }
    }

    return IntComputerState(true, array, index, base, -1)
}


private fun getAddressParameter(array: Array<Int>, index: Int, base: Int, parameterCount: Int) =
        if ((array[index] / ((10.toFloat().pow(parameterCount)).times(10).toInt())) % 10 == 2) {
            array[index.plus(parameterCount)].plus(base)
        } else {
            array[index.plus(parameterCount)]
        }

private fun getParameter(array: Array<Int>, index: Int, base: Int): Int {
    val modeParam1 = array[index].div(100).rem(10)

    val op1 = array[index.plus(1)]

    return when (modeParam1) {
        0 -> array[op1]
        1 -> op1
        2 -> array[op1 + base]
        else -> throw RuntimeException("RIP")
    }
}

private fun getParameters(array: Array<Int>, index: Int, base: Int): Pair<Int, Int> {

    val parameter1 = getParameter(array, index, base)

    val modeParam2 = array[index].div(1000).rem(10)
    val op2 = array[index.plus(2)]

    val parameter2 = when (modeParam2) {
        0 -> array[op2]
        1 -> op2
        2 -> array[op2 + base]
        else -> throw RuntimeException("..")
    }

    return Pair(parameter1, parameter2)
}