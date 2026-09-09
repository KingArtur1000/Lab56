package com.kingartur1000.lab56

import kotlin.math.ln

object CalculatorEngine {

    fun evaluate(expression: String): Double {
        // Подготовка выражения перед парсингом
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

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Неожиданный символ: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm()
                    else if (eat('-'.code)) x -= parseTerm()
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor()
                    else if (eat('/'.code)) x /= parseFactor()
                    else if (eat('%'.code)) x %= parseFactor()
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                var x: Double
                val startPos = pos

                if (eat('('.code)) {
                    x = parseExpression()
                    eat(')'.code)
                } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
                    while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch in 'a'.code..'z'.code || ch in 'A'.code..'Z'.code || ch == '√'.code) {
                    while (ch in 'a'.code..'z'.code || ch in 'A'.code..'Z'.code || ch == '√'.code) nextChar()
                    val func = str.substring(startPos, pos)
                    x = parseFactor()
                    x = when (func) {
                        "sin" -> Math.sin(Math.toRadians(x))
                        "cos" -> Math.cos(Math.toRadians(x))
                        "tan" -> Math.tan(Math.toRadians(x))
                        "lg" -> Math.log10(x)
                        "ln" -> ln(x)
                        "√" -> Math.sqrt(x)
                        else -> throw RuntimeException("Неизвестная функция: $func")
                    }
                } else {
                    throw RuntimeException("Неожиданный символ: " + ch.toChar())
                }

                if (eat('^'.code)) x = Math.pow(x, parseFactor())
                if (eat('!'.code)) {
                    var fact = 1.0
                    for (i in 1..x.toInt()) fact *= i
                    x = fact
                }
                return x
            }
        }.parse()
    }

    fun formatResult(result: Double): String {
        return if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString().replace('.', ',')
        }
    }
}