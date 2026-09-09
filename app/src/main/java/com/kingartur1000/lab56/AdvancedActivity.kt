package com.kingartur1000.lab56

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AdvancedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Временно подключим тот же макет или пустой.
        // Для полноценной сдачи лабы №5 здесь нужно будет создать activity_advanced.xml
        // с дополнительными кнопками (sin, cos, и т.д.)
        setContentView(R.layout.activity_main)
    }
}