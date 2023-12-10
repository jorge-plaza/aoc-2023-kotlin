fun main() {
    fun part1(input: List<String>): Int {
        val dirs = parseDirections(input)
        val nodes = parseNodes(input)
        return run(dirs, nodes)
    }

    fun part2(input: List<String>): Long {
        val dirs = parseDirections(input)
        val nodes = parseNodes(input)
        return run2(dirs, nodes)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput) == 2)
    check(part2(testInput2).toInt() == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private fun parseDirections(input: List<String>): String {
    return input.first()
}

private fun parseNodes(input: List<String>): Map<String, Pair<String, String>> {
    return input.drop(2)
        .associate { line ->
            val (id, dirs) = line.split(" = ")
            val (l, r) = dirs.replace("(", "").replace(")", "").split(", ")
            id to Pair(l, r)
        }
}

private fun run(dirs: String, nodes: Map<String, Pair<String, String>>): Int {
    var position = "AAA"
    var count = 0
    while (position != "ZZZ") {
        for (i in dirs) {
            val (l, r) = nodes[position]!!
            position = if (i == 'L') l else r
            count++
        }
    }
    return count
}

private fun run2(dirs: String, nodes: Map<String, Pair<String, String>>): Long {
     return nodes.filter { it.key.endsWith('A') }
        .map {
        var position = it.key
        var count = 0L
        while (!position.endsWith('Z')) {
            for (i in dirs) {
                val (l, r) = nodes[position]!!
                position = if (i == 'L') l else r
                count++
            }
        }
        count
    }.reduce { acc, i -> leastCommonMultiple(acc, i) }
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    return a * b / greatestCommonDivisor(a, b)
}
tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) a
    else greatestCommonDivisor(b, a % b)
}
