package day9

import parse
import kotlin.math.pow

fun main() {
    val input = parse("src/main/day9/input") {
        it[0].split(",").map { c -> c.toLong() }.toTypedArray()
    }

    val extendedInput = input + Array(1000) { 0L } // TODO: Dynamic length computation

    println(part1(extendedInput, 1))
    println(part1(extendedInput, 2))
}

fun part1(array: Array<Long>, input: Long): Long {
    return intComputer(array, input)
}

fun intComputer(array: Array<Long>, input: Long): Long {
    var index = 0
    var base = 0L
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
                return getParameter(array, index, base)
                //index += 2
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

    return -1L
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