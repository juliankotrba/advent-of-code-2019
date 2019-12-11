package day11

import parse
import kotlin.math.pow

typealias Position = Pair<Int, Int>

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun main() {
    val input = parse("src/main/day11/input") {
        it[0].split(",").map { c -> c.toLong() }.toTypedArray()
    }
    val extendedInput = input + Array(1000) { 0L } // TODO: Dynamic length computation

    println(part1(extendedInput))
}

fun part1(input: Array<Long>): Int {
    var currentPosition = Position(0, 0)
    var currentDirection = Direction.UP
    val blackSet = mutableSetOf(currentPosition)
    val touched = mutableSetOf<Position>()
    var state = IntComputerState(false, input, 0, 0, -1, -1)


    while (true) {

        val currInput = if (blackSet.contains(currentPosition) || !touched.contains(currentPosition)) {
            0L
        } else {
            1L
        }

        state = intComputer(currInput, state)
        if (state.done) {
            return touched.size
        }


        if (state.color == 0) {


            blackSet.add(currentPosition)

        } else {

            blackSet.remove(currentPosition)
        }
        touched.add(currentPosition)
        currentDirection = newDirection(currentDirection, state.direction)
        currentPosition = move(currentPosition, currentDirection)

    }


}

private fun move(position: Position, currentDirection: Direction): Position {
    return when (currentDirection) {
        Direction.UP -> Position(position.first, position.second + 1)
        Direction.DOWN -> Position(position.first, position.second - 1)
        Direction.LEFT -> Position(position.first - 1, position.second)
        Direction.RIGHT -> Position(position.first + 1, position.second)
    }
}

private fun newDirection(currentDirection: Direction, direction: Int): Direction =
        when (currentDirection) {
            Direction.UP -> if (direction == 0) Direction.LEFT else Direction.RIGHT
            Direction.DOWN -> if (direction == 0) Direction.RIGHT else Direction.LEFT
            Direction.LEFT -> if (direction == 0) Direction.DOWN else Direction.UP
            Direction.RIGHT -> if (direction == 0) Direction.UP else Direction.DOWN
        }

data class IntComputerState(
        val done: Boolean,
        val array: Array<Long>,
        val index: Int,
        val base: Long,
        val color: Int,
        val direction: Int
)

fun intComputer(input: Long, state: IntComputerState): IntComputerState {
    val array = state.array
    var index = state.index
    var base = state.base

    var color = -1
    val direction: Int

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


                array[addr.toInt()] = input
                index += 2
            }
            4L -> {
                if (color == -1) {
                    color = getParameter(array, index, base).toInt()
                    index += 2
                } else {
                    direction = getParameter(array, index, base).toInt()
                    index += 2
                    return IntComputerState(false, array, index, base, color, direction)
                }
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

    return IntComputerState(true, array, index, base, -1, -1)
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