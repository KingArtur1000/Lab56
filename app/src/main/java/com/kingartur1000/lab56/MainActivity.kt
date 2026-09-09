package com.kingartur1000.lab56

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var expression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        expression = intent.getStringExtra("EXPRESSION") ?: ""
        if (expression.isNotEmpty()) {
            tvExpression.text = expression
            updatePreviewResult()
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

        val opButtons = mapOf(
            R.id.btnPlus to "+", R.id.btnMinus to "-",
            R.id.btnMultiply to "×", R.id.btnDivide to "÷",
            R.id.btnPercent to "%"
        )
        for ((id, valStr) in opButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        findViewById<Button>(R.id.btnAC)?.setOnClickListener {
            expression = ""
            tvExpression.text = "0"
            tvResult.text = ""
        }

        findViewById<Button>(R.id.btnDelete)?.setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.substring(0, expression.length - 1)
                tvExpression.text = expression.ifEmpty { "0" }
                updatePreviewResult()
            }
        }

        findViewById<Button>(R.id.btnEquals)?.setOnClickListener {
            calculateResult()
        }

        findViewById<Button>(R.id.btnSwitch)?.setOnClickListener {
            val intent = Intent(this, AdvancedActivity::class.java)
            intent.putExtra("EXPRESSION", expression)
            startActivity(intent)
            finish()
        }
    }

    private fun appendToExpression(str: String) {
        expression += str
        tvExpression.text = expression
        updatePreviewResult()
    }

    private fun updatePreviewResult() {
        if (expression.isEmpty()) {
            tvResult.text = ""
            return
        }
        try {
            val result = CalculatorEngine.evaluate(expression)
            tvResult.text = CalculatorEngine.formatResult(result)
        } catch (_: Exception) {
            // Пока выражение не закончено, оставляем поле предварительного ответа пустым
            tvResult.text = ""
        }
    }

    private fun calculateResult() {
        if (expression.isEmpty()) return
        try {
            val result = CalculatorEngine.evaluate(expression)
            val formatted = CalculatorEngine.formatResult(result)
            tvExpression.text = formatted
            tvResult.text = ""
            expression = formatted
        } catch (_: Exception) {
            tvExpression.text = "Ошибка"
            tvResult.text = ""
        }
    }
}