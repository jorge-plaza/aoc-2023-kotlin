fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            val (_, numbers) = line.split(": ")
            val (wining, owned) = numbers
                .split(" | ")
                .map {
                    it.split(" ")
                        .filter { num -> num.isNotBlank() }
                        .map { num -> num.toInt() }
                        .toSet()
                }
            wining.intersect(owned).size
        }.sumOf {
            if (it == 0) return@sumOf 0
            var points = 1
            for (i in 1..<it) points *= 2
            points
        }
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Int, Int>()
        for (i in 1..input.size) map[i] = 1
        input.forEachIndexed { i, line ->
            val times = if (map[i + 1] == 0) 1 else map[i + 1]!!
            repeat(times) { _ -> updateMap(line, map) }
        }
        return map.values.sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun getWinnersByLine(line: String): Map<Int, Int> {
    val (card, numbers) = line.split(": ")
    val cardId = card.split("Card").last().trim().toInt()
    val (wining, owned) = numbers
        .split(" | ")
        .map {
            it.split(" ")
                .filter { num -> num.isNotBlank() }
                .map { num -> num.toInt() }
                .toSet()
        }
    val nWinners = wining.intersect(owned).size
    val map = mutableMapOf<Int, Int>()
    for (i in cardId + 1..cardId + nWinners) map[i] = 1
    return map
}

fun updateMap(line: String, map: MutableMap<Int, Int>) {
    getWinnersByLine(line).forEach { (id, v) ->
        map[id] = map[id]!! + v
    }
}