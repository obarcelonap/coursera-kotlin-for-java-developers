package games.gameOfFifteen

import kotlin.random.Random

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val permutation = (1..15).shuffled()
        if (isEven(permutation)) {
            return@lazy permutation
        }
        permutation.swap(Random.nextInt(15), Random.nextInt(15))
    }
}

private fun <E> List<E>.swap(index1: Int, index2: Int): List<E> {
    val list = this.toMutableList()
    val value1 = list[index1]
    list[index1] = list[index2]
    list[index2] = value1
    return list
}

