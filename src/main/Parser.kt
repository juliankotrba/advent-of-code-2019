import java.io.File

fun <T> parse(filePath: String, p: (List<String>) -> T): T = p(readFileLineByLine(filePath))

fun listOfInt(l: List<String>): List<Int> = l.map { it.toInt() }

fun listOfLong(l: List<String>): List<Long> = l.map { it.toLong() }

private fun readFileLineByLine(filePath: String): List<String> {
    val f = File(filePath)
    return f.readLines()
}