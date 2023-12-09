import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input.first().split("seeds: ").last().split(" ").map { it.toLong() }
        val maps = parseI(input)
        return seeds.minOf { s ->
            maps.fold(s) { acc: Long, map: Map<LongRange, LongRange> ->
                // Do operation or keep the unmapped value
                map.entries.firstOrNull { it.value.contains(acc) }?.let { entry ->
                    entry.key.first + entry.value.indexOf(acc)
                } ?: acc
            }
        }
    }

    fun part2(input: List<String>): Long {
        val seeds = input.first().split("seeds: ").last().split(" ").map { it.toLong() }
            .chunked(2)
            .map { it.first()..(it.first()+it.last()) }

        val maps = parseI(input)
        for (i in 1 until 100_000_000L){
            var possibleSeed = i
            for (map in maps.reversed()){
                possibleSeed = getPrevious(map, possibleSeed)
            }
            if (seeds.any { it.contains(possibleSeed) }) return i
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    // For some reason my input doesn't produce the right answer
    // Tested for other inputs, and they are correct
    // correct -> 17729182
    // produced -> 17729183
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

fun parseI(input: List<String>): List<Map<LongRange, LongRange>> {
    return input.drop(2)
        .joinToString("\n")
        .split("\n\n")
        .map { map ->
            map
                .split("\n")
                .drop(1)
                .associate { it1 ->
                    it1.split(" ").map { it.toLong() }
                        .let { (dest, src, len) ->
                            dest..(dest + len) to src..(src + len)
                        }
                }
        }
}

fun getPrevious(map: Map<LongRange, LongRange>, source: Long): Long {
    return map.entries
        .firstOrNull { it.key.contains(source) }
        ?.let {
            it.value.first + (source - it.key.first).absoluteValue
        } ?: source
}
