package com.kingartur1000.lab56

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class AdvancedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        val animClick = AnimationUtils.loadAnimation(this, R.anim.button_click)

        val goBackWithAnim = {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Кнопка переключения "⇇" возвращает назад
        findViewById<Button>(R.id.btnAdvSwitch).setOnClickListener { view ->
            view.startAnimation(animClick)
            goBackWithAnim()
        }

        // Кнопка Назад устройства
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackWithAnim()
            }
        })
    }
}