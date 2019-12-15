package day15

import parse
import kotlin.math.pow

typealias Position = Pair<Int, Int>

const val NORTH = 1L
const val SOUTH = 2L
const val EAST = 4L
const val WEST = 3L

fun main() {
    val input = parse("src/main/day15/input") {
        it[0].split(",").map { c -> c.toLong() }.toTypedArray()
    }
    val extendedInput = input + Array(1000) { 0L } // TODO: Dynamic length computation

    val path = part1(extendedInput)
    println(path.size)

}

fun part1(input: Array<Long>): MutableList<Pair<Position, Long>> {

    val path = mutableListOf<Pair<Position, Long>>()
    var current = Pair(0, 0)
    val visited = mutableSetOf<Pair<Int, Int>>()
    visited.add(current)

    var state = IntComputerState(false, input, 0, 0, -1)
    while (true) {

        if (!visited.contains(current.up())) {

            state = intComputer(state, NORTH)
            if (!visited.contains(current.up()) && state.output != 0) {

                current = current.up()
                visited.add(current)
                path.add(Pair(current, NORTH))

                if (state.output == 2) {
                    return path
                }

                continue
            }
        }

        if (!visited.contains(current.right())) {
            state = intComputer(state, EAST)
            if (!visited.contains(current.right()) && state.output != 0) {

                current = current.right()
                visited.add(current)
                path.add(Pair(current, EAST))

                if (state.output == 2) {
                    return path
                }

                continue
            }
        }

        if (!visited.contains(current.down())) {

            state = intComputer(state, SOUTH)
            if (!visited.contains(current.down()) && state.output != 0) {

                current = current.down()
                visited.add(current)
                path.add(Pair(current, SOUTH))

                if (state.output == 2) {
                    return path
                }

                continue
            }
        }

        if (!visited.contains(current.left())) {
            state = intComputer(state, WEST)
            if (!visited.contains(current.left()) && state.output != 0) {

                current = current.left()
                visited.add(current)
                path.add(Pair(current, WEST))

                if (state.output == 2) {
                    return path
                }
                continue
            }

        }

        val back = path.removeAt(path.size - 1).second.opposite()
        current = path.last().first
        state = intComputer(state, back)

    }
}

data class IntComputerState(
        val done: Boolean,
        val array: Array<Long>,
        val index: Int,
        val base: Long,
        val output: Int
)

fun intComputer(state: IntComputerState, input: Long): IntComputerState {
    var index = state.index
    var base = state.base
    val array = state.array


    while (array[index] != 99L) {

        when (array[index] % 100) {
            1L, 2L -> {

                val (parameter1, parameter2) = getParameters(array, index, base)
                val addr = getAddressParameter(array, index, base, 3)

                when (array[index] % 100) {
                    1L -> array[addr.toInt()] = parameter1 + parameter2
                    2L -> array[addr.toInt()] = parameter1 * parameter2
                }

                index += 4
            }

            3L -> {

                val addr = getAddressParameter(array, index, base, 1)
                array[addr.toInt()] = input // readLine()!!.toLong()
                index += 2
            }
            4L -> {

                val output = getParameter(array, index, base).toInt()
                index += 2
                return IntComputerState(false, array, index, base, output)
            }
            5L, 6L -> {

                val (parameter1, parameter2) = getParameters(array, index, base)

                if (parameter1 != 0L && (array[index] % 100) == 5L) {
                    index = parameter2.toInt()
                } else if (parameter1 == 0L && (array[index] % 100) == 6L) {
                    index = parameter2.toInt()
                } else {
                    index += 3
                }
            }
            7L, 8L -> {
                val (parameter1, parameter2) = getParameters(array, index, base)
                val addr = getAddressParameter(array, index, base, 3)

                if (parameter1 < parameter2 && (array[index] % 100) == 7L) {
                    array[addr.toInt()] = 1
                } else if (parameter1 == parameter2 && (array[index] % 100) == 8L) {
                    array[addr.toInt()] = 1
                } else {
                    array[addr.toInt()] = 0
                }

                index += 4
            }

            9L -> {
                val parameter = getParameter(array, index, base)
                base += parameter
                index += 2
            }
        }
    }

    return IntComputerState(true, array, index, base, -1)
}


private fun getAddressParameter(array: Array<Long>, index: Int, base: Long, parameterCount: Int) =
        if ((array[index] / ((10.toFloat().pow(parameterCount)).times(10).toInt())) % 10 == 2L) {
            array[index.plus(parameterCount)].plus(base)
        } else {
            array[index.plus(parameterCount)]
        }

private fun getParameter(array: Array<Long>, index: Int, base: Long): Long {
    val modeParam1 = array[index].div(100).rem(10)

    val op1 = array[index.plus(1)]

    return when (modeParam1) {
        0L -> array[op1.toInt()]
        1L -> op1
        2L -> array[op1.toInt() + base.toInt()]
        else -> throw RuntimeException("..")
    }
}

private fun getParameters(array: Array<Long>, index: Int, base: Long): Pair<Long, Long> {

    val parameter1 = getParameter(array, index, base)

    val modeParam2 = array[index].div(1000).rem(10)
    val op2 = array[index.plus(2)]

    val parameter2 = when (modeParam2) {
        0L -> array[op2.toInt()]
        1L -> op2
        2L -> array[op2.toInt() + base.toInt()]
        else -> throw RuntimeException("..")
    }

    return Pair(parameter1, parameter2)
}

fun Position.up() = this.copy(first, second + 1)
fun Position.down() = this.copy(first, second - 1)
fun Position.right() = this.copy(first + 1, second)
fun Position.left() = this.copy(first - 1, second)

fun Long.opposite(): Long = when (this) {
    1L -> SOUTH
    2L -> NORTH
    3L -> EAST
    4L -> WEST
    else -> throw RuntimeException("Not supported")
}
