package com.kingartur1000.lab56

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Используем tvExpression, так как именно этот ID указан в activity_main.xml
    private lateinit var tvExpression: TextView
    private var expression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)

        // Получаем выражение, если вернулись с AdvancedActivity
        expression = intent.getStringExtra("EXPRESSION") ?: ""
        if (expression.isNotEmpty()) {
            tvExpression.text = expression
        }

        setupButtons()
    }

    private fun setupButtons() {
        // Заменили btnComma на btnDot в соответствии с разметкой
        val digitButtons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to ","
        )
        for ((id, valStr) in digitButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        // Заменили btnAdd и btnSubtract на btnPlus и btnMinus
        val opButtons = mapOf(
            R.id.btnPlus to "+", R.id.btnMinus to "-",
            R.id.btnMultiply to "×", R.id.btnDivide to "÷",
            R.id.btnPercent to "%"
        )
        for ((id, valStr) in opButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        // Заменили btnClear на btnAC
        findViewById<Button>(R.id.btnAC)?.setOnClickListener {
            expression = ""
            tvExpression.text = "0"
        }

        findViewById<Button>(R.id.btnDelete)?.setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.substring(0, expression.length - 1)
                tvExpression.text = expression.ifEmpty { "0" }
            }
        }

        findViewById<Button>(R.id.btnEquals)?.setOnClickListener {
            calculateResult()
        }

        findViewById<Button>(R.id.btnSwitch)?.setOnClickListener {
            val intent = Intent(this, AdvancedActivity::class.java)
            intent.putExtra("EXPRESSION", expression)
            startActivity(intent)
            finish() // Закрываем текущее Activity, чтобы не плодить их в стеке
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
        } catch (_: Exception) { // Заменили 'e' на '_', чтобы убрать предупреждение
            tvExpression.text = "Ошибка"
        }
    }
}