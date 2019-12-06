package day4

fun main() {
    val input = (353096..843212)
    println(part1(input))
}

fun part1(input: IntRange) = input.filter { isValid(it.toString()) }.size

fun isValid(s: String): Boolean {
    val intPairs = s.map { it.toInt() }.zipWithNext()
    return intPairs.any { (x, y) -> x == y } && intPairs.all { (x, y) -> x <= y }
}