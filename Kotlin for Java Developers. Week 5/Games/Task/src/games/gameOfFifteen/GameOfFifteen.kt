package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game {
    return GameOfFifteen(initializer)
}

class GameOfFifteen(private var initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)
    private lateinit var freeCell: Cell

    override fun initialize() {
        freeCell = initializer.initialPermutation.addToBoard(board)
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        val cellValues = board.getAllCells()
            .dropLast()
            .map { board[it] }

        return cellValues == (1..15).toList()
    }

    override fun processMove(direction: Direction) {
        val nextFreeCell = when {
            direction == Direction.RIGHT && freeCell.j > 1 -> freeCell.copy(j = freeCell.j - 1)
            direction == Direction.LEFT && freeCell.j < 4 -> freeCell.copy(j = freeCell.j + 1)
            direction == Direction.UP && freeCell.i < 4 -> freeCell.copy(i = freeCell.i + 1)
            direction == Direction.DOWN && freeCell.i > 1 -> freeCell.copy(i = freeCell.i - 1)
            else -> freeCell
        }
        if (nextFreeCell != freeCell) {
            board.swap(freeCell, nextFreeCell)
            freeCell = nextFreeCell
        }
    }

    override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]
}

private fun <T> GameBoard<T>.swap(cell1: Cell, cell2: Cell) {
    val value1 = this[cell1]
    this[cell1] = this[cell2]
    this[cell2] = value1
}

private fun <E> Collection<E>.dropLast(): Collection<E> = if (size == 0) this else take(size - 1)

private fun List<Int>.addToBoard(board: GameBoard<Int?>): Cell {
    val permutation = this + null

    board.getAllCells().zip(permutation)
        .forEach { (cell, value) -> board[cell] = value }

    return board.getCell(4, 4)
}

