fun main() {
    fun part1(input: List<String>): Int {
        val races = parseInput(input)
        return races
            .map { getNWiningWays(it) }
            .reduce { result, i -> result * i }
    }

    fun part2(input: List<String>): Int {
        val race = parseInputPart2(input)
        return getNWiningWaysLong(race)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()

}

fun String.getValues() = this.substringAfter(":").split(" ").filter(CharSequence::isNotBlank).map(String::toInt)
fun parseInput(input: List<String>): List<Pair<Int, Int>> {
    val times = input.first().getValues()
    val distances = input.last().getValues()
    return times.zip(distances)
}

fun parseInputPart2(input: List<String>): Pair<Long, Long> {
    val time = input.first().getValues().joinToString("").toLong()
    val distance = input.last().getValues().joinToString("").toLong()
    return time to distance
}

fun getNWiningWays(race: Pair<Int, Int>): Int {
    // t = total time
    // p = sec pressed & speed
    // r = result distance
    // 1 sec press -> 6 sec travel at 1 m/s
    // 2 sec press ->
    val (t, record) = race
    var count = 0
    for (p in 0..t) {
        val r = (t - p) * p
        if (r > record) count++
    }
    return count
}

fun getNWiningWaysLong(race: Pair<Long, Long>): Int {
    // t = total time
    // p = sec pressed & speed
    // r = result distance
    // 1 sec press -> 6 sec travel at 1 m/s
    // 2 sec press ->
    val (t, record) = race
    var count = 0
    for (p in 0..t) {
        val r = (t - p) * p
        if (r > record) count++
    }
    return count
}
