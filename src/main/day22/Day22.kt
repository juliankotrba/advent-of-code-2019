package day22

import parse
import java.util.*
import kotlin.math.abs

fun main() {

    val commands = parse("src/main/day22/input") {
        it
    }

    println(part1(ArrayDeque<Int>((0..10006).toList()),commands))
}

fun part1(cardStack: Deque<Int>, commands: List<String>): Int {

    var cards = cardStack
    commands.forEach {

        when {
            it.startsWith("deal with increment ") -> {
                cards = cards.increment(it.split("deal with increment ")[1].toInt())
            }

            it.startsWith("cut") -> {
                cards = cards.cut(it.split(" ")[1].toInt())
            }

            it.startsWith("deal into new stack") -> {
                cards = cards.intoNewStack()
            }
        }
    }

    return cards.indexOf(2019)
}

private fun <T> Deque<T>.intoNewStack(): Deque<T> =
        ArrayDeque(this.reversed())

private fun <T> Deque<T>.cut(n: Int): ArrayDeque<T> {
    val tmpDeque = ArrayDeque(this)
    if (n > 0) {
        repeat(n) {
            tmpDeque.addLast(tmpDeque.removeFirst())
        }
    } else {
        repeat(abs(n)) {
            tmpDeque.addFirst(tmpDeque.removeLast())
        }
    }
    return tmpDeque
}

private inline fun <reified T> Deque<T>.increment(n: Int): Deque<T> {
    val incremented = Array(this.size) { this.first }
    this.forEachIndexed { i, e ->
        incremented[(i * n).rem(this.size)] =  e
    }
    return ArrayDeque<T>(incremented.toList())
}


