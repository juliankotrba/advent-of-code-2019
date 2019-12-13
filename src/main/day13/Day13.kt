package day13

import parse
import kotlin.math.pow

typealias IntCode = Array<Long>

fun main() {
    val input = parse("src/main/day13/input") {
        it[0].split(",").map { c -> c.toLong() }.toTypedArray()
    }
    val extendedInput = input + Array(1000) { 0L } // TODO: Dynamic length computation

    println(part1(extendedInput.copyOf()))
    println(part2(extendedInput.copyOf()))
}

fun part1(intCode: IntCode): Int {

    var blockCount = 0
    var state = IntComputerState(false, intCode, 0, 0L, -1, -1, -1, 0)
    var done = false

    while (!done) {

        state = intComputer(state, -1L)

        if (state.tile == 2) {
            blockCount++
        }

        done = state.done
    }

    return blockCount
}

fun part2(intCode: IntCode): Int {
    intCode[0] = 2

    // TODO: Just for visualizing the game
    val gameField = Array(22) {
        Array(40) { ' ' }
    }

    var state = IntComputerState(false, intCode, 0, 0L, -1, -1, -1, 0)

    val blocks = mutableSetOf<Pair<Int, Int>>()
    var ballPosition: Pair<Int, Int>? = null
    var paddlePosition: Pair<Int, Int>? = null

    var input = 0L
    var done = false
    while (!done) {

        if (paddlePosition != null) {
            when {
                state.x > paddlePosition.first -> input = 1L
                state.x < paddlePosition.first -> input = -1L
                state.x == paddlePosition.first -> input = 0L
            }
        }

        state = intComputer(state, input)

        when (state.tile) {
            2 -> {
                gameField[state.y][state.x] = 'B'
                blocks.add(Pair(state.x, state.y))
            }

            3 -> {
                if (paddlePosition != null) {
                    gameField[paddlePosition.second][paddlePosition.first] = ' '
                }
                gameField[state.y][state.x] = '-'
                paddlePosition = Pair(state.x, state.y)
            }

            4 -> {
                if (ballPosition != null) {
                    gameField[ballPosition.second][ballPosition.first] = ' '
                }

                ballPosition = Pair(state.x, state.y)
                gameField[state.y][state.x] = 'O'


                if (blocks.contains(Pair(state.x, state.y))) {
                    gameField[state.y][state.x] = ' '
                    blocks.remove(Pair(state.x, state.y))
                }
            }
        }

        done = state.done

        // printGame(gameField)
    }

    return state.score
}

data class IntComputerState(
        val done: Boolean,
        val array: Array<Long>,
        val index: Int,
        val base: Long,
        val x: Int,
        val y: Int,
        val tile: Int,
        val score: Int
)

fun intComputer(state: IntComputerState, input: Long): IntComputerState {
    var index = state.index
    var base = state.base
    val array = state.array
    var score = state.score

    var outputCount = 0
    var x = -1
    var y = -1

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

                when (outputCount) {
                    0 -> x = output
                    1 -> y = output
                    2 -> {
                        if (x == -1 && y == 0) {
                            score = output
                        }
                        return IntComputerState(false, array, index, base, x, y, output, score)
                    }

                }
                outputCount++
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

    return IntComputerState(true, array, index, base, -1, -1, -1, score)
}

private fun printGame(field: Array<Array<Char>>) {
    field.forEach {
        println(it.joinToString(""))
    }
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