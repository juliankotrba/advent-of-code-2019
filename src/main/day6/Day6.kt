package day6

import parse

fun main() {
    println(part1(parse("src/main/day6/input") { it }))
    println(part2(parse("src/main/day6/input") { it }))
}

fun part1(input: List<String>): Int {
    val successorMap = mutableMapOf<String, String>()

    input.forEach {
        successorMap[it.split(")")[1]] = it.split(")")[0]
    }

    var allSteps = 0
    successorMap.keys.forEach { orbit ->
        var localSteps = 0
        var currentOrbit: String? = orbit
        while (currentOrbit != null) {
            currentOrbit = successorMap[currentOrbit]
            if (currentOrbit != null) {
                localSteps++
            }
        }
        allSteps += localSteps
    }
    return allSteps
}

fun part2(input: List<String>): Int {

    val successorMap = mutableMapOf<String, String>()

    input.forEach {
        successorMap[it.split(")")[1]] = it.split(")")[0]
    }

    val youSuccessors = getAllSuccessors("YOU", successorMap)
    val sanSuccessors = getAllSuccessors("SAN", successorMap)

    val sameOrbit = findFirstEqualOrbit(youSuccessors, sanSuccessors)

    return youSuccessors.subList(0, youSuccessors.indexOf(sameOrbit)).size +
            sanSuccessors.subList(0, sanSuccessors.indexOf(sameOrbit)).size
}

private fun getAllSuccessors(start: String, successorMap: Map<String, String>): List<String> {
    val allSuccessor = mutableListOf<String>()
    var currentOrbit: String? = start
    while (currentOrbit != null) {
        currentOrbit = successorMap[currentOrbit]
        if (currentOrbit != null) {
            allSuccessor.add(currentOrbit)
        }
    }
    return allSuccessor
}

private fun findFirstEqualOrbit(youList: List<String>, sanList: List<String>): String {
    val tmpSanSet = sanList.toSet()
    for (i in 0 until youList.size) {
        if (tmpSanSet.contains(youList[i])) {
            return youList[i]
        }
    }
    throw RuntimeException("There must be at least one equal orbit!")
}