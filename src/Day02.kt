fun main() {
    val red = 12
    val green = 13
    val blue = 14
    fun part1(input: List<String>): Int {
        return input.map { parse(it) }
            .filter { game ->
                game.red <= red &&
                game.green <= green &&
                game.blue <= blue
            }
            .sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.map { parse(it) }.sumOf { it.red*it.green*it.blue }
    }

    // test if implementation meets criteria from the description, like:
    // 12 red cubes, 13 green cubes, and 14 blue cubes
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Game(
    val id: Int,
    var red: Int = 0,
    var green: Int = 0,
    var blue: Int = 0,
)

fun parse(input: String): Game {
    val splitContent = input.split("Game ", ": ").filterNot { it.isBlank() }
    val id = splitContent.first().toInt()
    val sets = splitContent.last()
        .split("; ")
        .map { set ->
            val playGame = Game(id)
            set.split(", ")
                .map { it.split(" ") }
                .onEach {
                    when (it.last()) {
                        "red" -> playGame.red = it.first().toInt()
                        "green" -> playGame.green = it.first().toInt()
                        "blue" -> playGame.blue = it.first().toInt()
                    }
                }
            playGame
        }
    val red = sets.maxOf { it.red }
    val green = sets.maxOf { it.green }
    val blue = sets.maxOf { it.blue }
    return Game(id, red, green, blue)
}
