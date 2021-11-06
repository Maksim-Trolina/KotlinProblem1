open class Matrix {
    var height: Int
        protected set
    var width: Int
        protected set
    protected var matrix: Array<Array<Double>>

    constructor(matrix: Array<Array<Double>>) {
        this.matrix = Array(matrix.size){Array(matrix[0].size){0.0} }
        height = matrix.size
        width = matrix[0].size
        for(row in 0 until height)
            for(column in 0 until width)
                this.matrix[row][column] = matrix[row][column]
    }

    operator fun get(row: Int, column: Int): Double {
        return matrix[row][column]
    }


    operator fun plus(otherMatrix: Matrix): Matrix = Matrix(addition(otherMatrix))

    operator fun minus(otherMatrix: Matrix): Matrix = Matrix(subtraction(otherMatrix))

    operator fun times(otherMatrix: Matrix): Matrix = Matrix(multiply(otherMatrix))

    open operator fun times(scalar: Double): Matrix = Matrix(multiply(scalar))

    open operator fun div(scalar: Double): Matrix = Matrix(division(scalar))

    operator fun unaryPlus(): Matrix = Matrix(matrix)

    operator fun unaryMinus(): Matrix {
        val resultMatrix = Array(height) { Array(width) { 0.0 } }
        for (row in matrix.indices)
            for (column in matrix[row].indices)
                resultMatrix[row][column] = -matrix[row][column]
        return Matrix(resultMatrix)
    }

    override operator fun equals(other: Any?): Boolean {
        if(other == null)
            return false
        val otherMatrix = other as Matrix
        if(height != otherMatrix.height || width != otherMatrix.width)
            return false
        for(row in 0 until height)
            for(column in 0 until width)
                if(matrix[row][column] != otherMatrix.matrix[row][column])
                    return false
        return true
    }

    override fun toString(): String {
        var result = ""
        for(row in 0 until height){
            for(column in 0 until width){
                result += matrix[row][column].toString() + " "
            }
            result += "\n"
        }
        return result
    }

    protected fun addition(otherMatrix: Matrix): Array<Array<Double>> {
        if (height != otherMatrix.height || width != otherMatrix.width)
            throw Exception("These matrices cannot be added")
        val resultMatrix = Array(height) { Array(width) { 0.0 } }
        for (row in matrix.indices)
            for (column in matrix[row].indices)
                resultMatrix[row][column] = matrix[row][column] + otherMatrix[row, column]
        return resultMatrix
    }

    protected fun subtraction(otherMatrix: Matrix): Array<Array<Double>> {
        if (height != otherMatrix.height || width != otherMatrix.width)
            throw Exception("These matrices cannot be subtracted")
        val resultMatrix = Array(height) { Array(width) { 0.0 } }
        for (row in matrix.indices)
            for (column in matrix[row].indices)
                resultMatrix[row][column] = matrix[row][column] - otherMatrix[row, column]
        return resultMatrix
    }

    protected fun division(scalar: Double): Array<Array<Double>> {
        if (scalar == 0.0)
            throw Exception("Trying to divide by zero")
        val resultMatrix = Array(height) { Array(width) { 0.0 } }
        for (row in matrix.indices)
            for (column in matrix[row].indices)
                resultMatrix[row][column] = matrix[row][column] / scalar
        return resultMatrix
    }

    protected fun multiply(otherMatrix: Matrix): Array<Array<Double>> {
        if (width != otherMatrix.height)
            throw Exception("These matrices cannot be multiplied")
        val resultMatrix = Array(height) { Array(otherMatrix.width) { 0.0 } }
        for (row in 0 until height)
            for (column in 0 until otherMatrix.width)
                for (k in 0 until width)
                    resultMatrix[row][column] += matrix[row][k] * otherMatrix.matrix[k][column]
        return resultMatrix
    }

    protected fun multiply(scalar: Double): Array<Array<Double>> {
        val resultMatrix = Array(height) { Array(width) { 0.0 } }
        for (row in matrix.indices)
            for (column in matrix[row].indices)
                resultMatrix[row][column] = matrix[row][column] * scalar
        return resultMatrix
    }
}

class MutableMatrix(matrix: Array<Array<Double>>) : Matrix(matrix) {

    operator fun plus(otherMatrix: MutableMatrix): MutableMatrix = MutableMatrix(addition(otherMatrix))

    operator fun plusAssign(otherMatrix: MutableMatrix) {
        matrix = addition(otherMatrix)
    }

    operator fun minus(otherMatrix: MutableMatrix): MutableMatrix = MutableMatrix(subtraction(otherMatrix))

    operator fun minusAssign(otherMatrix: MutableMatrix) {
        matrix = subtraction(otherMatrix)
    }

    operator fun times(otherMatrix: MutableMatrix): MutableMatrix = MutableMatrix(multiply(otherMatrix))

    operator fun timesAssign(otherMatrix: MutableMatrix) {
        matrix = multiply(otherMatrix)
        height = matrix.size
        width = matrix[0].size
    }

    override operator fun times(scalar: Double): MutableMatrix = MutableMatrix(multiply(scalar))

    operator fun timesAssign(scalar: Double) {
        matrix = multiply(scalar)
    }

    override operator fun div(scalar: Double): MutableMatrix = MutableMatrix(division(scalar))

    operator fun divAssign(scalar: Double) {
        matrix = division(scalar)
    }

    operator fun set(row: Int, column: Int, value: Double) {
        matrix[row][column] = value
    }

}

fun main() {

    val matrix = Array(2) { Array(3) { 2.0 } }

    val mutableMatrix = MutableMatrix(matrix)

    mutableMatrix[0, 0] = 4.0

    println(mutableMatrix[0, 0])

    println(mutableMatrix.height)

    println(mutableMatrix.width)

    mutableMatrix += mutableMatrix

    println(mutableMatrix) //1

    val sumMatrix = mutableMatrix + mutableMatrix

    println(sumMatrix) //2

    val subtractionMatrix = mutableMatrix - MutableMatrix(matrix)

    println(subtractionMatrix) //3

    mutableMatrix -= MutableMatrix(matrix)

    println(mutableMatrix) //4

    val matrix2 = MutableMatrix(Array(3){Array(2){3.0} })

    val timesMatrix = mutableMatrix * matrix2

    println(timesMatrix) //5

    mutableMatrix *= matrix2

    println(mutableMatrix) //6

    val unaryMinusMatrix = -mutableMatrix

    println(unaryMinusMatrix) //7

    val unaryPlusMatrix = +mutableMatrix

    println(unaryPlusMatrix) //8

    val scalarTimesMatrix = mutableMatrix * 2.0

    println(scalarTimesMatrix) //9

    val scalarDivMatrix = scalarTimesMatrix / 2.0

    println(scalarDivMatrix) //10

    mutableMatrix*=2.0

    println(mutableMatrix) //11

    mutableMatrix/=2.0

    println(mutableMatrix) //12

    println(mutableMatrix == mutableMatrix) //13

    println(mutableMatrix == scalarTimesMatrix) //14
}








