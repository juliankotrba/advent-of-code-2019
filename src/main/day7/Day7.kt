package day7

import parse

typealias Program = Array<Int>
typealias PhaseSetting = Int
typealias Input = Int
typealias Output = Int

fun main() {

    val input = parse("src/main/day7/input") {
        it[0].split(",").map { c -> c.toInt() }
    }

    println(part1(input.toTypedArray()))
    println(part2(input.toTypedArray()))
}

fun part1(program: Program): Int {

    var currentMax = 0
    (0..44444).filter {
        // TODO: Find better way to find all allowed permutations
        var s = it.toString()
        if (s.length != 5) {
            s = "0$s"
        }
        s.toSet().size == 5
    }.filter {
        it.div(10000).rem(10) < 5 &&
                it.div(1000).rem(10) < 5 &&
                it.div(100).rem(10) < 5 &&
                it.div(10).rem(10) < 5 &&
                it.rem(10) < 5

    }.forEach { phases ->
        val phaseArray = arrayOf(
                phases.div(10000).rem(10),
                phases.div(1000).rem(10),
                phases.div(100).rem(10),
                phases.div(10).rem(10),
                phases.rem(10)
        )

        var input = 0
        phaseArray.forEach {
            input = intComputer(program.copyOf(), it, input, 0).first
        }

        if (input > currentMax) {
            currentMax = input
        }
    }
    return currentMax
}

fun part2(program: Program): Int {

    val allMax = mutableListOf<Int>()
    (55555..99999).filter {
        it.toString().toSet().size == 5
    }.filter {
        it.div(10000).rem(10) > 4 &&
                it.div(1000).rem(10) > 4 &&
                it.div(100).rem(10) > 4 &&
                it.div(10).rem(10) > 4 &&
                it.rem(10) > 4

    }.forEach { phases ->
        val phaseArray = arrayOf(
                phases.div(10000).rem(10),
                phases.div(1000).rem(10),
                phases.div(100).rem(10),
                phases.div(10).rem(10),
                phases.rem(10)
        )


        var input = 0
        var halted = false
        var max = 0

        val softwareMap = mutableMapOf<Int, Program>()
        val indexMap = mutableMapOf<Int, Int>()

        listOf(9,7,8,5,6).forEach {
            softwareMap[it] = program.copyOf()
            indexMap[it] = 0
        }

        while (!halted) {

            phaseArray.forEach {
                val p = softwareMap[it]!!
                val i = indexMap[it]!!
                val (newInput, newIndex) = intComputer(p, it, input, i)
                input = newInput
                indexMap[it] = newIndex
                softwareMap[it] = p
            }
            if (input == -1) {
                halted = true
            } else {
                if(input > max) {
                    max = input
                }
            }
        }

        allMax.add(max)
    }

    return allMax.max()!!
}

fun intComputer(program: Program, phaseSetting: PhaseSetting, input: Input, prevIndex: Int): Pair<Output, Int> {

    val inputs = if (prevIndex == 0) {
        mutableListOf(phaseSetting, input)
    } else {
        mutableListOf(input)
    }

    var index = prevIndex
    while (program[index] != 99) {

        when (program[index] % 100) {
            1, 2 -> {
                val (parameter1, parameter2) = getParameters(program, index)
                val loc = program[index.plus(3)]
                when (program[index] % 100) {
                    1 -> program[loc] = parameter1 + parameter2
                    2 -> program[loc] = parameter1 * parameter2
                }

                index += 4
            }

            3 -> {
                val loc = program[index.plus(1)]
                program[loc] = inputs.removeAt(0)
                index += 2
            }
            4 -> {
                return Pair(getParameter(program, index), index+2)
            }
            5, 6 -> {

                val (parameter1, parameter2) = getParameters(program, index)

                if (parameter1 != 0 && (program[index] % 100) == 5) {
                    index = parameter2
                } else if (parameter1 == 0 && (program[index] % 100) == 6) {
                    index = parameter2
                } else {
                    index += 3
                }
            }
            7, 8 -> {
                val (parameter1, parameter2) = getParameters(program, index)

                val loc = program[index.plus(3)]
                if (parameter1 < parameter2 && (program[index] % 100) == 7) {
                    program[loc] = 1
                } else if (parameter1 == parameter2 && (program[index] % 100) == 8) {
                    program[loc] = 1
                } else {
                    program[loc] = 0
                }

                index += 4
            }
        }

    }
    return Pair(-1, -1) // halted
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