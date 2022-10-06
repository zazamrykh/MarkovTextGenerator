class SquareMatrix(private var N: Int) {
    private val matrix = mutableListOf(mutableListOf<Double>())

    init {
        for (i in 0 until N) {
            matrix.add(mutableListOf())
            for (j in 0 until N) {
                matrix[i].add(0.0)
            }
        }
    }

    fun expandMatrix() {
        matrix.add(mutableListOf())
        for (j in 0 until N) {
            matrix[N].add(0.0)
        }
        N++
        for (i in 0 until N) {
            matrix[i].add(0.0)
        }
    }

    fun getElementAt(i: Int, j: Int): Double = matrix[i][j]

    fun setElementAt(elementValue: Double, i: Int, j: Int) {
        matrix[i][j] = elementValue
    }

    fun printMatrix() {
        for (i in 0 until N) {
            for (j in 0 until N) {
                print("${matrix[i][j]} ")
            }
            println()
        }
    }

    fun getRow(i: Int): MutableList<Double> {
        val row = mutableListOf<Double>()
        for (j in 0 until N) {
            row.add(matrix[i][j])
        }
        return row
    }

}