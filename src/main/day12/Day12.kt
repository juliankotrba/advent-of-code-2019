package day12

import java.util.*
import kotlin.math.abs

typealias Moon = Pair<Array<Int>, Array<Int>>
typealias Moons = Array<Moon>

fun main() {

    val t1 = arrayOf(
            Pair(arrayOf(-1, 0, 2), arrayOf(0, 0, 0)),
            Pair(arrayOf(2, -10, -7), arrayOf(0, 0, 0)),
            Pair(arrayOf(4, -8, 8), arrayOf(0, 0, 0)),
            Pair(arrayOf(3, 5, -1), arrayOf(0, 0, 0))
    )

    val t2 = arrayOf(
            Pair(arrayOf(-8, -10, 0), arrayOf(0, 0, 0)),
            Pair(arrayOf(5, 5, 10), arrayOf(0, 0, 0)),
            Pair(arrayOf(2, -7, 3), arrayOf(0, 0, 0)),
            Pair(arrayOf(9, -8, -3), arrayOf(0, 0, 0))
    )

    val input = arrayOf(
            Pair(arrayOf(8, 0, 8), arrayOf(0, 0, 0)),
            Pair(arrayOf(0, -5, -10), arrayOf(0, 0, 0)),
            Pair(arrayOf(16, 10, -5), arrayOf(0, 0, 0)),
            Pair(arrayOf(19, -10, -7), arrayOf(0, 0, 0))
    )

    print(part1(input, 1000))
}

fun part1(moons: Moons, steps: Int): Int {

    repeat(steps) {

        moons.forEachIndexed { i, m ->
            adjustGravity(m, moons, i)
        }

        applyVelocity(moons)
    }

    return moons.map { it.energy() }.sum()
}


fun applyVelocity(moons: Moons) {
    moons.forEach {
        it.position()[0] = it.position()[0] + it.velocity()[0]
        it.position()[1] = it.position()[1] + it.velocity()[1]
        it.position()[2] = it.position()[2] + it.velocity()[2]
    }
}

fun adjustGravity(moon: Moon, moons: Moons, index: Int) {
    moons.filterIndexed { i, _ -> i != index }
            .forEach { m ->
                for (p in 0 until 3) {
                    if (moon.position()[p] < m.position()[p]) {

                        moon.velocity()[p] = moon.velocity()[p] + 1

                    } else if (moon.position()[p] > m.position()[p]) {
                        moon.velocity()[p] = moon.velocity()[p] - 1
                    }
                }
            }

}

fun Moon.position() = this.first
fun Moon.velocity() = this.second

fun Moon.energy() = (this.position().map { abs(it) }.sum()) * (this.velocity().map { abs(it) }.sum())

fun Array<Int>.x() = this[0]
fun Array<Int>.y() = this[1]
fun Array<Int>.z() = this[2]

fun Moon.prettyPrint() {
    println("${Arrays.toString(this.position())} - ${Arrays.toString(this.velocity())}")
}

