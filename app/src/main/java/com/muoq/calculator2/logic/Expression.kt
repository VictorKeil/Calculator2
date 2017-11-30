package com.muoq.calculator2.logic

import java.math.BigDecimal

class Expression() {

    companion object {
        val operators = Operator.validOperators
        val EPSILON = 0.0000000000000001
    }

    private var expression: MutableList<Any> = mutableListOf()
    private var isNumber: MutableList<Boolean> = mutableListOf()

    var size: Int = 0
        get() {
            return expression.size
        }

    constructor(expressionString: String): this() {
        expressionString.replace(" ", "")
        if (expressionString.isEmpty()) {
            addNumber(BigDecimal(0))
        }

        var decimalDivisor = BigDecimal(1)
        for (char in expressionString) {
            if (char in operators) {
                addOperator(Operator(char))
            } else {
                if (isNumber.isNotEmpty() && isNumber[isNumber.size - 1]) {

                    if (char == '.') {
                        decimalDivisor = BigDecimal(10)
                        continue
                    } else if (decimalDivisor.compareTo(BigDecimal(1)) == 1) {
                        expression[expression.size - 1] = expression.last() as BigDecimal + BigDecimal(char.toString()).divide(decimalDivisor)
                    } else {
                        expression[expression.size - 1] = expression.last() as BigDecimal * BigDecimal(10) + BigDecimal(char.toString())
                    }
                } else {
                    addNumber(BigDecimal(char.toString()))
                }
            }
        }
    }

    constructor(openingIndex: Int, expressionArg: Expression): this() {
        for (i in openingIndex until expressionArg.size) {
            if (expressionArg.isNumber(i)) {
                addNumber(expressionArg.getNumberAt(i))
            } else {
                addOperator(expressionArg.getOperatorAt(i))
            }
        }
    }

    constructor(expressionArg: Expression): this() {
        for (i in 0 until expressionArg.size) {
            if (expressionArg.isNumber(i)) {
                addNumber(expressionArg.getNumberAt(i))
            } else {
                addOperator(expressionArg.getOperatorAt(i))
            }
        }
    }

    fun addNumber(num: BigDecimal) {
        expression.add(num)
        isNumber.add(true)
        size++
    }

    fun addOperator(operator: Operator) {
        expression.add(operator)
        isNumber.add(false)
        size++
    }

    fun removeAt(index: Int) {
        expression.removeAt(index)
        isNumber.removeAt(index)
        size--
    }

    fun removeLast() {
        expression.removeAt(size - 1)
        isNumber.removeAt(size - 1)
        size--
    }

    fun setNumberAt(index: Int, num: BigDecimal) {
        expression[index] = num
        isNumber[index] = true
    }

    fun setOperatorAt(index: Int, operator: Operator) {
        expression[index] = operator
        isNumber[index] = false
    }

    fun isNumber(index: Int) = isNumber[index]


    fun isOperator(index: Int) = !isNumber[index]

    fun getNumberAt(index: Int): BigDecimal {
        if (!isNumber(index)) {
            throw TypeCastException("Error: The element you are trying to access is not of type Operator")
        }

        return expression[index] as BigDecimal
    }

    fun getOperatorAt(index: Int): Operator {
        if (!isOperator(index)) {
            throw TypeCastException("Error: The element you are trying to access is not of type Operator")
        }

        return expression[index] as Operator
    }

    fun getSurroundingNumbers(index: Int): Pair<BigDecimal, BigDecimal> {
        return Pair(getNumberAt(index - 1), getNumberAt(index + 1))
    }

    fun clear() {
        size = 0
        expression = mutableListOf(BigDecimal(0))
        isNumber = mutableListOf(true)
    }

    override fun toString(): String {
        var classString = ""

        for (i in 0 until expression.size) {
            classString += (expression[i].toString())
        }

        return classString
    }

}
