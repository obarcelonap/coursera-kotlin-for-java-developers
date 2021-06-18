package board

fun createSquareBoard(width: Int): SquareBoard = CellSquareBoard(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = CellGameBoard(createSquareBoard(width))


class CellSquareBoard(override val width: Int) : SquareBoard {
    private val cells = generateSequence(1) { it + 1 }
        .take(width)
        .map { i ->
            generateSequence(1) { it + 1 }
                .take(width)
                .map { j -> Cell(i, j) }
                .toList()
        }
        .toList()

    override fun getCellOrNull(i: Int, j: Int): Cell? = cells.getOrNull(i - 1)?.getOrNull(j - 1)

    override fun getCell(i: Int, j: Int): Cell = cells[i - 1][j - 1]

    override fun getAllCells(): Collection<Cell> = cells.flatten()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        cells[i - 1]
            .filter { it.j in jRange }
            .sortedWith(jRange.comparator { it.j })


    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        cells
            .flatMap {
                it.filter { cell -> cell.i in iRange && cell.j == j }
            }
            .sortedWith(iRange.comparator { it.i })

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val neighbour = when (direction) {
            Direction.UP -> copy(i = i - 1)
            Direction.DOWN -> copy(i = i + 1)
            Direction.RIGHT -> copy(j = j + 1)
            Direction.LEFT -> copy(j = j - 1)
        }
        return getCellOrNull(neighbour.i, neighbour.j)
    }

    private fun <T> IntProgression.comparator(selector: (T) -> Comparable<*>?): Comparator<T> =
        if (step < 0) compareByDescending(selector)
        else compareBy(selector)
}

class CellGameBoard<T>(private val squareBoard: SquareBoard) : GameBoard<T>, SquareBoard by squareBoard {
    private val cellValues: MutableMap<Cell, T?> = squareBoard.getAllCells()
        .associateWith { null }
        .toMutableMap()

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean) = cellValues.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean) = filter(predicate).firstOrNull()

    override fun any(predicate: (T?) -> Boolean) = cellValues.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean) = cellValues.values.all(predicate)
}
