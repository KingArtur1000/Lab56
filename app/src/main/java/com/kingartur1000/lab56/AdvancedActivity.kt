package com.kingartur1000.lab56

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdvancedActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private var expression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        tvExpression = findViewById(R.id.tvExpression)

        expression = intent.getStringExtra("EXPRESSION") ?: ""
        if (expression.isNotEmpty()) {
            tvExpression.text = expression
        }

        setupButtons()
    }

    private fun setupButtons() {
        val digitButtons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to ","
        )
        for ((id, valStr) in digitButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        // Обновлены ID кнопок под activity_advanced.xml
        val opButtons = mapOf(
            R.id.btnPlus to "+", R.id.btnMinus to "-",
            R.id.btnMultiply to "×", R.id.btnDivide to "÷",
            R.id.btnPercent to "%", R.id.btnOpenParen to "(",
            R.id.btnCloseParen to ")"
        )
        for ((id, valStr) in opButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        // Заменили btnPower на btnPow
        val funcButtons = mapOf(
            R.id.btnSin to "sin(", R.id.btnCos to "cos(", R.id.btnTan to "tan(",
            R.id.btnLg to "lg(", R.id.btnLn to "ln(", R.id.btnSqrt to "√(",
            R.id.btnPow to "^", R.id.btnFact to "!", R.id.btnPi to "π",
            R.id.btnE to "E"
        )
        for ((id, valStr) in funcButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        // Заменили btnInverse на btnInv
        findViewById<Button>(R.id.btnInv)?.setOnClickListener {
            appendToExpression("1/")
        }

        // Заменили btnClear на btnC
        findViewById<Button>(R.id.btnC)?.setOnClickListener {
            expression = ""
            tvExpression.text = "0"
        }

        findViewById<Button>(R.id.btnDelete)?.setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.substring(0, expression.length - 1)
                tvExpression.text = if (expression.isEmpty()) "0" else expression
            }
        }

        findViewById<Button>(R.id.btnEquals)?.setOnClickListener {
            calculateResult()
        }

        findViewById<Button>(R.id.btnSwitchBack)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("EXPRESSION", expression)
            startActivity(intent)
            finish()
        }
    }

    private fun appendToExpression(str: String) {
        expression += str
        tvExpression.text = expression
    }

    private fun calculateResult() {
        if (expression.isEmpty()) return
        try {
            val result = CalculatorEngine.evaluate(expression)
            val formatted = CalculatorEngine.formatResult(result)
            tvExpression.text = formatted
            expression = formatted
        } catch (_: Exception) {
            tvExpression.text = "Ошибка"
        }
    }
}