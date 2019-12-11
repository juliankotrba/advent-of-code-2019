package day10

import parse
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

typealias Map = List<List<Char>>
typealias MutableMap = List<MutableList<Char>>
typealias Asteroid = Pair<Int, Int>

fun main() {

    val input = parse("src/main/day10/input") { lines ->
        val rows = mutableListOf<MutableList<Char>>()
        lines.forEach { line ->
            rows.add(line.toMutableList())
        }
        rows.toList()
    }

    val result = part1(input)
    println(result.second)
    println(part2(input, result.first))

}

fun part1(input: Map): Pair<Asteroid, Int> {

    val asteroids = getAsteroids(input)
    val visibleMap = mutableMapOf<Asteroid, Int>()

    asteroids.forEach { selected ->
        val s = mutableSetOf<Double>()
        asteroids.forEach {
            if (selected.first != it.first || selected.second != it.second) {
                s.add(phi(selected, it))
            }
        }
        visibleMap[selected] = s.size
    }
    val result = visibleMap.maxBy { it.value }!!
    return Pair(result.key, result.value)
}


private fun part2(input: MutableMap, laser: Asteroid): Int {
    val asteroids = getAsteroids(input)
    val phis = asteroids.distinct().map { phi(laser, it) }.sorted()
    val indexOf90Degrees = phis.indexOf(-1.5707963267948966)

    val orderedPhis = phis.subList(indexOf90Degrees, phis.size - 1) + phis.subList(0, indexOf90Degrees - 1)

    var iterations = 0
    orderedPhis.distinct().forEach { cp ->

        val shootDest = asteroids.filter { phi(laser, it) == cp }.minBy { dist(laser, it) }!!
        input[shootDest.second][shootDest.first] = '.'
        iterations++
        if (iterations == 200) {
            return shootDest.first.times(100).plus(shootDest.second)
        }
    }

    throw RuntimeException("Must run at least 200 iterations")
}

private fun dist(asteroid1: Asteroid, asteroid2: Asteroid): Double =
        sqrt((asteroid1.first - asteroid2.first).toDouble().pow(2) + (asteroid1.second - asteroid2.second).toDouble().pow(2))

private fun phi(fromAsteroid: Asteroid, toAsteroid: Asteroid): Double =
        atan2(
                (toAsteroid.second - fromAsteroid.second).toDouble(),
                (toAsteroid.first - fromAsteroid.first).toDouble()
        )

private fun getAsteroids(input: List<List<Char>>): List<Asteroid> {
    val asteroids = mutableListOf<Asteroid>()

    input.forEachIndexed { y, _ ->
        input[0].forEachIndexed { x, _ ->
            if (input[y][x] == '#') {
                asteroids.add(Asteroid(x, y))
            }
        }
    }
    return asteroids
}
