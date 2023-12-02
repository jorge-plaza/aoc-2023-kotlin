fun main() {
    fun part1(input: List<String>): Int {
        val result = input.fold(0) { sum, line ->
            val digits = line.filter { it.isDigit() }
            val calibration = "" + digits.first() + digits.last()
            sum + calibration.toInt()
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val result = input.fold(0) { sum, line ->
            val spelledNumbers = SpelledNumber.entries.map { it.name }
            val firstSpelled = line.findAnyOf(spelledNumbers)
            val lastSpelled = line.findLastAnyOf(spelledNumbers)
            val firstInt = line.withIndex().firstOrNull { it.value.isDigit() }
            val lastInt = line.withIndex().lastOrNull { it.value.isDigit() }
            val first: String
            val last: String
            if (firstSpelled != null && firstInt != null) {
                first =
                    if (firstSpelled.first < firstInt.index) spelledToIntString(firstSpelled)!!
                    else firstInt.value.toString()
                last =
                    if (lastSpelled?.first!! > lastInt?.index!!) spelledToIntString(lastSpelled)!!
                    else lastInt.value.toString()
            } else {
                first = spelledToIntString(firstSpelled) ?: firstInt?.value.toString()
                last = spelledToIntString(lastSpelled) ?: lastInt?.value.toString()
            }

            val calibration = first + last
            sum + calibration.toInt()
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testInput2 = readInput("Day01_test_part2")
    check(part1(testInput) == 142)
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
enum class SpelledNumber(val int: Int) {
    one(1),
    two(2),
    three(3),
    four(4),
    five(5),
    six(6),
    seven(7),
    eight(8),
    nine(9)
}

fun spelledToIntString(firstSpelled: Pair<Int, String>?): String? {
    return if (firstSpelled == null) null
    else SpelledNumber.valueOf(firstSpelled.second).int.toString()
}