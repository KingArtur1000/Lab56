package com.kingartur1000.lab56

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdvancedActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var expression = ""
    private var is2ndActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

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
            R.id.btnPercent to "%", R.id.btnOpenParen to "(",
            R.id.btnCloseParen to ")"
        )
        for ((id, valStr) in opButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        val btn2nd = findViewById<Button>(R.id.btn2nd)
        val btnSin = findViewById<Button>(R.id.btnSin)
        val btnCos = findViewById<Button>(R.id.btnCos)
        val btnTan = findViewById<Button>(R.id.btnTan)
        val btnLn = findViewById<Button>(R.id.btnLn)
        val btnLg = findViewById<Button>(R.id.btnLg)
        val btnSqrt = findViewById<Button>(R.id.btnSqrt)

        btn2nd?.setOnClickListener {
            is2ndActive = !is2ndActive
            if (is2ndActive) {
                btn2nd.setTextColor(Color.parseColor("#FF6D00"))
                btnSin?.text = "asin"
                btnCos?.text = "acos"
                btnTan?.text = "atan"
                btnLn?.text = "eˣ"
                btnLg?.text = "10ˣ"
                btnSqrt?.text = "x²"
            } else {
                btn2nd.setTextColor(Color.parseColor("#E0E0E0"))
                btnSin?.text = "sin"
                btnCos?.text = "cos"
                btnTan?.text = "tan"
                btnLn?.text = "ln"
                btnLg?.text = "lg"
                btnSqrt?.text = "√"
            }
        }

        btnSin?.setOnClickListener { appendToExpression(if (is2ndActive) "asin(" else "sin(") }
        btnCos?.setOnClickListener { appendToExpression(if (is2ndActive) "acos(" else "cos(") }
        btnTan?.setOnClickListener { appendToExpression(if (is2ndActive) "atan(" else "tan(") }
        btnLn?.setOnClickListener { appendToExpression(if (is2ndActive) "E^(" else "ln(") }
        btnLg?.setOnClickListener { appendToExpression(if (is2ndActive) "10^(" else "lg(") }
        btnSqrt?.setOnClickListener { appendToExpression(if (is2ndActive) "^2" else "√(") }

        val staticFuncButtons = mapOf(
            R.id.btnPow to "^", R.id.btnFact to "!", R.id.btnPi to "π",
            R.id.btnE to "E"
        )
        for ((id, valStr) in staticFuncButtons) {
            findViewById<Button>(id)?.setOnClickListener { appendToExpression(valStr) }
        }

        findViewById<Button>(R.id.btnInv)?.setOnClickListener {
            appendToExpression("1/")
        }

        findViewById<Button>(R.id.btnC)?.setOnClickListener {
            expression = ""
            tvExpression.text = "0"
            tvResult.text = ""
        }

        findViewById<Button>(R.id.btnDelete)?.setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.substring(0, expression.length - 1)
                tvExpression.text = if (expression.isEmpty()) "0" else expression
                updatePreviewResult()
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