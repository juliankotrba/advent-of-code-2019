package day1

import listOfLong
import parse
import kotlin.math.floor

typealias Mass =  Long

fun main() {
    val input = parse("src/main/day1/input", ::listOfLong)
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<Mass>): Double {
    return input.map { floor(it.div(3).toDouble()).minus(2) }.sum()
}

fun part2(input: List<Mass>): Double {
    return input.map { calculateFuel(it) }.sum()
}

private fun calculateFuel(mass: Mass): Double {
    var sum = 0.0
    var c = floor(mass.div(3).toDouble()).minus(2)
    while(c > 0) {
        sum += c
        c = floor(c.div(3)).minus(2)
    }
    return sum
}

