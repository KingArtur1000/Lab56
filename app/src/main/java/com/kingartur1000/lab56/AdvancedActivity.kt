package com.kingartur1000.lab56

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class AdvancedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        val tvAdvDisplay = findViewById<TextView>(R.id.tvAdvDisplay)
        val animClick = AnimationUtils.loadAnimation(this, R.anim.button_click)

        findViewById<Button>(R.id.btnBackToMain).setOnClickListener {
            finish()
        }

        fun getVal(): Double = tvAdvDisplay.text.toString().toDoubleOrNull() ?: 0.0

        findViewById<Button>(R.id.btnSin).setOnClickListener { it.startAnimation(animClick)
            tvAdvDisplay.text = sin(Math.toRadians(getVal())).toString()
        }

        findViewById<Button>(R.id.btnCos).setOnClickListener { it.startAnimation(animClick)
            tvAdvDisplay.text = cos(Math.toRadians(getVal())).toString()
        }

        findViewById<Button>(R.id.btnTan).setOnClickListener { it.startAnimation(animClick)
            tvAdvDisplay.text = tan(Math.toRadians(getVal())).toString()
        }

        findViewById<Button>(R.id.btnSqrt).setOnClickListener { it.startAnimation(animClick)
            tvAdvDisplay.text = sqrt(getVal()).toString()
        }

        findViewById<Button>(R.id.btnPow).setOnClickListener { it.startAnimation(animClick)
            val v = getVal()
            tvAdvDisplay.text = (v * v).toString()
        }

        findViewById<Button>(R.id.btnAdvAC).setOnClickListener { it.startAnimation(animClick)
            tvAdvDisplay.text = getString(R.string.calc_digit_0)
        }
    }
}