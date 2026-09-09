package com.kingartur1000.lab56

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        // "Умный" выход из приложения
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(baseContext, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot, R.id.btnPlus, R.id.btnMinus,
            R.id.btnMultiply, R.id.btnDivide, R.id.btnPercent
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { view ->
                animateClick(view)
                appendExpression((view as Button).text.toString())
            }
        }

        findViewById<Button>(R.id.btnAC).setOnClickListener {
            animateClick(it)
            tvExpression.text = ""
            tvResult.text = ""
        }

        findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
            animateClick(it)
            val currentText = tvExpression.text.toString()
            if (currentText.isNotEmpty()) {
                tvExpression.text = currentText.dropLast(1)
                calculateResult()
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            animateClick(it)
            try {
                val result = eval(tvExpression.text.toString().replace("×", "*").replace("÷", "/"))
                tvResult.text = "= ${formatResult(result)}"
            } catch (e: Exception) {
                tvResult.text = "Ошибка"
            }
        }

        findViewById<ImageButton>(R.id.btnSwitch).setOnClickListener {
            animateClick(it)
            val intent = android.content.Intent(this, AdvancedActivity::class.java)
            startActivity(intent)
        }
    }

    private fun appendExpression(string: String) {
        tvExpression.append(string)
        calculateResult()
    }

    private fun calculateResult() {
        try {
            if (tvExpression.text.isNotEmpty()) {
                val expression = tvExpression.text.toString().replace("×", "*").replace("÷", "/")
                val result = eval(expression)
                tvResult.text = "= ${formatResult(result)}"
            } else {
                tvResult.text = ""
            }
        } catch (e: Exception) {
            // Игнорируем промежуточные ошибки ввода, пока пользователь не нажмет "="
        }
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble())
            result.toLong().toString()
        else
            result.toString()
    }

    // Простая анимация масштабирования кнопок при нажатии
    private fun animateClick(view: android.view.View) {
        view.animate()
            .scaleX(0.9f).scaleY(0.9f).setDuration(50)
            .withEndAction {
                view.animate().scaleX(1f).scaleY(1f).setDuration(50).start()
            }.start()
    }

    // Простой парсер математических выражений
    private fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() { ch = if (++pos < str.length) str[pos].code else -1 }
            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) { nextChar(); return true }
                return false
            }
            fun parse(): Double { nextChar(); val x = parseExpression(); if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar()); return x }
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
                    else if (eat('%'.code)) x /= 100 // Поддержка процентов
                    else return x
                }
            }
            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('('.code)) { x = parseExpression(); eat(')'.code) }
                else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else { throw RuntimeException("Unexpected: " + ch.toChar()) }
                return x
            }
        }.parse()
    }
}