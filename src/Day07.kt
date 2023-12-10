fun main() {
    fun part1(input: List<String>): Int {
        val hands = parse(input)
        return hands
            .map { (hand, bid) ->
                Hand(hand, hand.groupingBy { it }.eachCount(), bid, ::matchHand, ::compare1)
            }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val hands = parse(input)
        return hands
            .map { (hand, bid) ->
                Hand(hand, hand.groupingBy { it }.eachCount(), bid, ::matchHand2, ::compare2)
            }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun parse(input: List<String>): List<Pair<CharSequence, Int>> {
    return input.map { it.split(" ") }.associate { it.first() to it.last().toInt() }.map { it.toPair() }
}

private fun matchHand(hand: Map<Char, Int>): Int {
    if (hand.any { it.value == 5 }) return 7
    if (hand.any { it.value == 4 }) return 6
    if (hand.count { it.value == 3 } == 1 && hand.count { it.value == 2 } == 1) return 5
    if (hand.any { it.value == 3 }) return 4
    if (hand.count { it.value == 2 } == 2) return 3
    if (hand.any { it.value == 2 }) return 2
    if (hand.values.all { it == 1 }) return 1
    throw Exception("no hand type handled")
}

private fun matchHand2(hand: Map<Char, Int>): Int {
    val jCount = hand['J'] ?: 0
    if (hand.any { it.value == 5 }) return S.FIVE_OF_A_KIND.v
    if (hand.any { it.value == 4 }) return when (jCount) {
        1 -> S.FIVE_OF_A_KIND.v
        4 -> S.FIVE_OF_A_KIND.v
        else -> S.FOUR_OF_A_KIND.v
    }
    if (hand.count { it.value == 3 } == 1 && hand.count { it.value == 2 } == 1) return when (jCount) {
        2 -> S.FIVE_OF_A_KIND.v
        1 -> S.FOUR_OF_A_KIND.v
        3 -> S.FIVE_OF_A_KIND.v
        else -> S.FULL_HOUSE.v
    }
    if (hand.any { it.value == 3 }) return when (jCount) {
        1 -> S.FOUR_OF_A_KIND.v
        2 -> S.FULL_HOUSE.v
        3 -> S.FOUR_OF_A_KIND.v
        else -> S.THREE_OF_A_KIND.v
    }
    if (hand.count { it.value == 2 } == 2) return when (jCount) {
        1 -> S.FULL_HOUSE.v
        2 -> S.FOUR_OF_A_KIND.v
        else -> S.TWO_PAIRS.v
    }
    if (hand.any { it.value == 2 }) return when (jCount) {
        1 -> S.THREE_OF_A_KIND.v
        2 -> S.THREE_OF_A_KIND.v
        else -> S.ONE_PAIR.v
    }
    if (hand.values.all { it == 1 }) return when (jCount) {
        1 -> S.ONE_PAIR.v
        2 -> S.ONE_PAIR.v
        else -> S.HIGH_CARD.v
    }
    throw Exception("no hand type handled")
}

private fun compare1(h1: CharSequence, h2: CharSequence): Int {
    for (i in h1.indices) {
        val v1 = if (h1[i].isDigit()) h1[i].digitToInt() else Card.valueOf(h1[i].toString()).value
        val v2 = if (h2[i].isDigit()) h2[i].digitToInt() else Card.valueOf(h2[i].toString()).value
        if (v1 == v2) continue
        return if (v1 > v2) 1
        else -1
    }
    return 0
}

private fun compare2(h1: CharSequence, h2: CharSequence): Int {
    for (i in h1.indices) {
        val v1 = if (h1[i].isDigit()) h1[i].digitToInt() else Card2.valueOf(h1[i].toString()).value
        val v2 = if (h2[i].isDigit()) h2[i].digitToInt() else Card2.valueOf(h2[i].toString()).value
        if (v1 == v2) continue
        return if (v1 > v2) 1
        else -1
    }
    return 0
}

class Hand(
    val h: CharSequence,
    val grouped: Map<Char, Int>,
    val bid: Int,
    val match: (Map<Char, Int>) -> Int,
    val compare: (h1: CharSequence, h2: CharSequence) -> Int,
    val streght: Int = match(grouped)
) : Comparable<Hand> {

    override fun compareTo(other: Hand): Int {
        if (match(grouped) > match(other.grouped)) return 1
        else if (match(grouped) < match(other.grouped)) return -1
        return compare(h, other.h)
    }
}

private enum class Card(val value: Int) {
    A(14),
    K(13),
    Q(12),
    J(11),
    T(10),
}

private enum class Card2(val value: Int) {
    A(14),
    K(13),
    Q(12),
    T(10),
    J(1),
}

private enum class S(val v: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIRS(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}
