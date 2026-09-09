package com.kingartur1000.lab56

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object CalculatorEngine {

    private val mathContext = MathContext(15, RoundingMode.HALF_UP)
    // Контекст с точностью 12 цифр для сглаживания погрешностей Double и Math.PI
    private val trigMathContext = MathContext(12, RoundingMode.HALF_UP)

    fun evaluate(expression: String): BigDecimal {
        val str = expression
            .replace("×", "*")
            .replace("÷", "/")
            .replace(",", ".")
            .replace("π", Math.PI.toString())
            .replace("E", Math.E.toString())

        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): BigDecimal {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Неожиданный символ: " + ch.toChar())
                return x
            }

            fun parseExpression(): BigDecimal {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x = x.add(parseTerm(), mathContext)
                    else if (eat('-'.code)) x = x.subtract(parseTerm(), mathContext)
                    else return x
                }
            }

            fun parseTerm(): BigDecimal {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) {
                        x = x.multiply(parseFactor(), mathContext)
                    } else if (eat('/'.code)) {
                        val divisor = parseFactor()
                        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                            throw ArithmeticException("Деление на ноль")
                        }
                        x = x.divide(divisor, mathContext)
                    } else if (eat('%'.code)) {
                        val divisor = parseFactor()
                        x = x.remainder(divisor, mathContext)
                    } else {
                        return x
                    }
                }
            }

            fun parseFactor(): BigDecimal {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return parseFactor().negate()

                var x: BigDecimal
                val startPos = pos

                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
                    while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                    x = BigDecimal(str.substring(startPos, pos))
                } else if (ch in 'a'.code..'z'.code || ch in 'A'.code..'Z'.code || ch == '√'.code) {
                    while (ch in 'a'.code..'z'.code || ch in 'A'.code..'Z'.code || ch == '√'.code) nextChar()
                    val func = str.substring(startPos, pos)
                    val arg = parseFactor()
                    val argDouble = arg.toDouble()

                    val resDouble = when (func) {
                        "sin" -> sin(Math.toRadians(argDouble))
                        "cos" -> cos(Math.toRadians(argDouble))
                        "tan" -> tan(Math.toRadians(argDouble))
                        "asin" -> Math.toDegrees(asin(argDouble))
                        "acos" -> Math.toDegrees(acos(argDouble))
                        "atan" -> Math.toDegrees(atan(argDouble))
                        "lg" -> log10(argDouble)
                        "ln" -> ln(argDouble)
                        "√" -> sqrt(argDouble)
                        else -> throw RuntimeException("Неизвестная функция: $func")
                    }

                    if (resDouble.isNaN() || resDouble.isInfinite()) {
                        throw ArithmeticException("Математическая ошибка")
                    }

                    // Округление до 12 значащих цифр мягко сглаживает 0.49999999999999994 до 0.5
                    x = BigDecimal.valueOf(resDouble).round(trigMathContext)
                } else {
                    throw RuntimeException("Неожиданный символ: " + ch.toChar())
                }

                if (eat('^'.code)) {
                    val exponent = parseFactor()
                    val resDouble = x.toDouble().pow(exponent.toDouble())
                    x = BigDecimal.valueOf(resDouble).round(trigMathContext)
                }
                if (eat('!'.code)) {
                    var fact = BigDecimal.ONE
                    val n = x.toInt()
                    for (i in 1..n) {
                        fact = fact.multiply(BigDecimal.valueOf(i.toLong()))
                    }
                    x = fact
                }
                return x
            }
        }.parse()
    }

    fun formatResult(result: BigDecimal): String {
        if (result.compareTo(BigDecimal.ZERO) == 0) return "0"
        return result.stripTrailingZeros().toPlainString().replace('.', ',')
    }
}