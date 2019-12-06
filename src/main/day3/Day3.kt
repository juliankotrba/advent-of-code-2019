package day3

import parse
import kotlin.math.absoluteValue

typealias Direction = Char
typealias Steps = Int
typealias Position = Pair<Int, Int>
typealias Wire = List<Pair<Direction, Steps>>
typealias Wires = Pair<Wire, Wire>

fun Position.up() = this.copy(first, second + 1)
fun Position.down() = this.copy(first, second - 1)
fun Position.right() = this.copy(first + 1, second)
fun Position.left() = this.copy(first - 1, second)

fun main() {
    val input = parse("src/main/day3/input") {
        Wires(
                it[0].split(",").map { s -> toDirectionStepPair(s) },
                it[1].split(",").map { s -> toDirectionStepPair(s) }
        )
    }

    println(part1(input))
    println(part2(input))
}

fun part1(wires: Wires): Int {

    val visitedWire1 = runWire(wires.first)
    val visitedWire2 = runWire(wires.second)

    return visitedWire1.keys.intersect(visitedWire2.keys).map { it.first.absoluteValue + it.second.absoluteValue }.min()!!
}

fun part2(wires: Wires): Int {

    val visitedWire1 = runWire(wires.first)
    val visitedWire2 = runWire(wires.second)

    return visitedWire1.keys.intersect(visitedWire2.keys)
            .map { visitedWire1.getOrElse(it) { 0 } + visitedWire2.getOrElse(it) { 0 } }
            .min() ?: 0
}

private fun runWire(wire: Wire): Map<Position, Steps> {
    val visited = mutableMapOf<Position, Int>()
    var position = Position(0, 0)
    var stepCount = 0
    wire.forEach { w ->

        when (w.first) {
            'U' -> position = walkAndSave(position, w.second, ::walkUp, visited, stepCount)
            'D' -> position = walkAndSave(position, w.second, ::walkDown, visited, stepCount)
            'R' -> position = walkAndSave(position, w.second, ::walkRight, visited, stepCount)
            'L' -> position = walkAndSave(position, w.second, ::walkLeft, visited, stepCount)
        }

        stepCount += w.second
    }
    return visited
}

private fun walkAndSave(position: Position, times: Int, walk: (Position) -> Position, visited: MutableMap<Position, Int>, previousSteps: Int): Position {
    var tmpPosition = position
    var tmpStepCount = previousSteps
    repeat(times) {
        tmpPosition = walk(tmpPosition)
        tmpStepCount += 1
        visited[tmpPosition] = tmpStepCount
    }
    return tmpPosition
}

private fun walkUp(position: Position) = position.up()
private fun walkDown(position: Position) = position.down()
private fun walkLeft(position: Position) = position.left()
private fun walkRight(position: Position) = position.right()

private fun toDirectionStepPair(input: String): Pair<Direction, Steps> =
        Pair(input[0], input.substring(1).toInt())