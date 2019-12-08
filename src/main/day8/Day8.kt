package day8

import parse

typealias Layer = List<Char>

fun main() {

    val input = parse("src/main/day8/input") {
        it[0].chunked(25 * 6)
    }.map { it.toList() }

    println(part1(input))
    part2(input).forEach {
        println(it.joinToString(""))
    }
}

fun part1(input: List<Layer>) =
        input.minBy { layer ->
            layer.count { c -> c == '0' }
        }?.let { line ->
            line.count { it == '1' } * line.count { it == '2' }
        } ?: 0

fun part2(input: List<Layer>): List<List<Char>> {
    val originalImage = mutableListOf<Char>()

    repeat(input[0].size) {
        originalImage.add(getPixelForLayers(it, input))
    }

    return originalImage.chunked(25)
}

private fun getPixelForLayers(pixelIndex: Int, layers: List<Layer>): Char {
    var layerIndex = 0
    while (true) {
        // Assumption: There is at least on black or white pixel in each stack of pixels
        when (layers[layerIndex][pixelIndex]) {
            '0' -> return ' '
            '1' -> return '#'
            '2' -> layerIndex++
        }
    }
}