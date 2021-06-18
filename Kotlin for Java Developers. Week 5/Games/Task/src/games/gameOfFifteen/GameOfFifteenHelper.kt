package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {
    val invertedPairs = with (permutation) {
        indices.asSequence()
            .flatMap { i -> (1 until size).map { j -> Pair(i, j) } }
            .filter { it.first < it.second }
            .filter { get(it.first) > get(it.second) }
    }

    return invertedPairs
        .count()
        .isEven()
}

private fun Int.isEven(): Boolean = this % 2 == 0
