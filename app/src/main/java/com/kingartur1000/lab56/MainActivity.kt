package com.kingartur1000.lab56

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private lateinit var tvExpression: TextView

    private var firstNum: BigDecimal? = null
    private var currentOperation: String = ""
    private var isNewInput = true
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
        tvExpression = findViewById(R.id.tvExpression)
        val animClick = AnimationUtils.loadAnimation(this, R.anim.button_click)

        // Переход в инженерный режим с анимацией
        findViewById<Button>(R.id.btnSwitch).setOnClickListener { view ->
            view.startAnimation(animClick)
            val intent = Intent(this, AdvancedActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Цифры
        val numberIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        for (id in numberIds) {
            findViewById<Button>(id).setOnClickListener { view ->
                view.startAnimation(animClick)
                val digit = (view as Button).text.toString()
                if (isNewInput || tvDisplay.text.toString() == "0") {
                    tvDisplay.text = digit
                    isNewInput = false
                } else {
                    tvDisplay.append(digit)
                }
            }
        }

        val setOperation = { op: String ->
            val currentValue = getDisplayValue()
            if (currentValue != null) {
                firstNum = currentValue
                currentOperation = op
                tvExpression.text = "${formatValue(currentValue)} $op"
                isNewInput = true
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { it.startAnimation(animClick); setOperation("+") }
        findViewById<Button>(R.id.btnSub).setOnClickListener { it.startAnimation(animClick); setOperation("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { it.startAnimation(animClick); setOperation("×") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { it.startAnimation(animClick); setOperation("÷") }

        findViewById<Button>(R.id.btnDot).setOnClickListener { view ->
            view.startAnimation(animClick)
            if (isNewInput) {
                tvDisplay.text = "0."
                isNewInput = false
            } else if (!tvDisplay.text.contains(".")) {
                tvDisplay.append(".")
            }
        }

        findViewById<Button>(R.id.btnPercent).setOnClickListener { view ->
            view.startAnimation(animClick)
            val currentVal = getDisplayValue() ?: return@setOnClickListener
            val percentVal = currentVal.divide(BigDecimal("100"), 10, RoundingMode.HALF_UP)
            tvDisplay.text = formatValue(percentVal)
            isNewInput = true
        }

        findViewById<Button>(R.id.btnAC).setOnClickListener { view ->
            view.startAnimation(animClick)
            tvDisplay.text = getString(R.string.calc_digit_0)
            tvExpression.text = ""
            firstNum = null
            currentOperation = ""
            isNewInput = true
        }

        findViewById<Button>(R.id.btnDel).setOnClickListener { view ->
            view.startAnimation(animClick)
            if (!isNewInput) {
                val text = tvDisplay.text.toString()
                if (text.length > 1) {
                    tvDisplay.text = text.dropLast(1)
                } else {
                    tvDisplay.text = getString(R.string.calc_digit_0)
                    isNewInput = true
                }
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { view ->
            view.startAnimation(animClick)
            val secondNum = getDisplayValue()
            if (firstNum != null && secondNum != null && currentOperation.isNotEmpty()) {
                tvExpression.text = "${formatValue(firstNum!!)} $currentOperation ${formatValue(secondNum)} ="
                val result: BigDecimal? = try {
                    when (currentOperation) {
                        "+" -> firstNum!!.add(secondNum)
                        "-" -> firstNum!!.subtract(secondNum)
                        "×" -> firstNum!!.multiply(secondNum)
                        "÷" -> if (secondNum.compareTo(BigDecimal.ZERO) == 0) null else firstNum!!.divide(secondNum, 10, RoundingMode.HALF_UP)
                        else -> null
                    }
                } catch (e: Exception) { null }

                tvDisplay.text = if (result == null) getString(R.string.msg_error) else formatValue(result)
                firstNum = null
                currentOperation = ""
                isNewInput = true
            }
        }

        // Умный выход из приложения
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity()
                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.msg_smart_exit), Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

    private fun getDisplayValue(): BigDecimal? = try { BigDecimal(tvDisplay.text.toString().replace(",", ".")) } catch (e: Exception) { null }

    private fun formatValue(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return if (stripped.scale() <= 0) stripped.toBigInteger().toString() else stripped.toPlainString()
    }
}