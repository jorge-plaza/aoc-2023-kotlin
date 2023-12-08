import java.awt.Point

fun main() {
    fun part1(input: List<String>): Int {
        val (numbers, symbols) = parse(input)
        val symbolsLocations = symbols.map { it.location }.toSet()
        return numbers
            .filter { num -> num.isAdjacent(symbolsLocations) }
            .sumOf { num -> num.toInt() }
    }

    fun part2(input: List<String>): Int {
        val (numbers, symbols) = parse(input)
        return symbols.filter { it.char == '*' }
            .sumOf { s ->
                val neighbors = numbers.filter { it.isAdjacentTo(s.location) }
                if (neighbors.size == 2) neighbors.first().toInt() * neighbors.last().toInt()
                else 0
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun Point.neighbors(): Set<Point> {
    val x = this.x
    val y = this.y
    return mutableSetOf(
        Point(x - 1, y - 1),
        Point(x - 1, y),
        Point(x - 1, y + 1),
        Point(x, y - 1),
        Point(x, y + 1),
        Point(x + 1, y - 1),
        Point(x + 1, y),
        Point(x + 1, y + 1),
    )
}

class Num(
    private val location: MutableSet<Point> = mutableSetOf(),
    private val number: MutableList<Char> = mutableListOf()
) {
    fun addPart(c: Char, position: Point) {
        location.add(position)
        number.add(c)
    }

    fun isEmpty(): Boolean = number.size <= 0

    fun toInt() = number.joinToString("").toInt()

    fun isAdjacent(set: Set<Point>): Boolean {
        val neighbors = set.flatMap { it.neighbors() }.toSet()
        return location.intersect(neighbors).isNotEmpty()
    }

    fun isAdjacentTo(point: Point): Boolean {
        return point.neighbors().any { it in location }
    }
}

data class Symbol(
    val char: Char,
    val location: Point,
)

data class State(
    val numbers: Set<Num>,
    val symbols: Set<Symbol>,
)


fun parse(input: List<String>): State {
    val numbers = mutableSetOf<Num>()
    val symbols = mutableSetOf<Symbol>()
    var num = Num()

    input.forEachIndexed { x, line ->
        line.forEachIndexed { y, c ->
            if (c.isDigit()) {
                num.addPart(c, Point(x, y))
            } else { //Maybe is the end of a number
                if (!num.isEmpty()) { // Add to global numbers and reset
                    numbers.add(num)
                    num = Num()
                }
                if (c != '.') {
                    symbols.add(Symbol(c, Point(x, y)))
                }
            }
        }
        // End of line check if current number needs reset
        if (!num.isEmpty()) {
            numbers.add(num)
            num = Num()
        }
    }
    return State(numbers, symbols)
}