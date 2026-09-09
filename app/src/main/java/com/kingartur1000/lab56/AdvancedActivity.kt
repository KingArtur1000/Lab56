package com.kingartur1000.lab56

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AdvancedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)

        // Кнопка возврата на стандартный калькулятор
        findViewById<ImageButton>(R.id.btnSwitchBack).setOnClickListener {
                view ->
            view.animate()
                .scaleX(0.9f).scaleY(0.9f).setDuration(50)
                .withEndAction {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(50).start()
                    finish() // Закрываем Activity, возвращаясь на MainActivity
                }.start()
        }

        // TODO: Здесь нужно будет добавить слушатели для новых кнопок (sin, cos, π)
        // и адаптировать парсер математических выражений.
    }
}