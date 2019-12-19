package day19

import parse
import kotlin.math.pow

fun main() {
    val input = parse("src/main/day19/input") {
        it[0].split(",").map { c -> c.toInt() }.toTypedArray()
    }
    val extendedInput = input + Array(1000) { 0 } // TODO: Dynamic length computation

    println(part1(extendedInput).size)
    println(part2(extendedInput))
}

fun part1(input: Array<Int>): Set<Pair<Int, Int>> {

    val positions = mutableSetOf<Pair<Int, Int>>()

    for (y in 0 until 50) {
        for (x in 0 until 50) {

            val output = intComputer(IntComputerState(false, input.copyOf(), 0, 0, -1), x, y).output
            if (output == 1) {
                positions.add(Pair(x, y))
            }

        }
    }

    return positions
}

// half optimized solution
fun part2(input: Array<Int>): Int {
    val size = 100

    var x = size
    var y = size
    while (true) {

        var foundAffectedInLine = false
        while (true) {

            val current = intComputer(IntComputerState(false, input.copyOf(), 0, 0, -1), x, y).output

            if (current == 1) {
                foundAffectedInLine = true
                val o1 = intComputer(IntComputerState(false, input.copyOf(), 0, 0, -1), x.plus(size - 1), y).output
                val o2 = intComputer(IntComputerState(false, input.copyOf(), 0, 0, -1), x, y.plus(size - 1)).output

                if (o1 == 1 && o2 == 1) {
                    return x.times(10000).plus(y)
                }
                x +=1
            } else {
                x += size/2
            }

            if (foundAffectedInLine && current == 0) {
                // we can skip this line because there are no more affected points
                y += 1
                break
            }
        }
        x = size
    }
}

data class IntComputerState(
        val done: Boolean,
        val array: Array<Int>,
        val index: Int,
        val base: Int,
        val output: Int
)

fun intComputer(state: IntComputerState, xInput: Int, yInput: Int): IntComputerState {
    var xInputUsed = false

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
                //println("Please enter input:")
                array[addr] = if (!xInputUsed) {
                    //println("using $xInput")
                    xInputUsed = true
                    xInput
                } else {
                    //println("using $yInput")
                    yInput
                }//readLine()!!.toInt()
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