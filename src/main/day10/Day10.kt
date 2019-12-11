package day10

import parse
import kotlin.math.atan2

typealias Map = List<List<Char>>
typealias Asteroid = Pair<Int, Int>

fun main() {
    val testinput = mutableListOf<List<Char>>()
    """
.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##
            """.trimIndent().lines().forEach { line ->
        testinput.add(line.toList())
    }


    val input = parse("src/main/day10/input") { lines ->
        val rows = mutableListOf<List<Char>>()
        lines.forEach { line ->
            rows.add(line.toList())
        }
        rows.toList()
    }

    println(part1(input))

}

fun part1(input: Map): Int {

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
    print(visibleMap.maxBy { it.value }?.key)
    return visibleMap.maxBy { it.value }?.value ?: 0
}

private fun phi(fromAsteroid: Asteroid, toAsteroid: Asteroid): Double =
        atan2(
                (toAsteroid.second - fromAsteroid.second).toDouble(),
                (toAsteroid.first - fromAsteroid.first).toDouble()
        )

private fun getAsteroids(input: List<List<Char>>): List<Asteroid> {
    val asteroids = mutableListOf<Asteroid>()

    input.forEachIndexed { y, _ ->
        input.forEachIndexed { x, _ ->
            if (input[y][x] == '#') {
                asteroids.add(Asteroid(x, y))
            }
        }
    }
    return asteroids
}
