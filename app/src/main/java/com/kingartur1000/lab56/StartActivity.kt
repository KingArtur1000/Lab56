package com.kingartur1000.lab56

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val btnStandard = findViewById<Button>(R.id.btnStandard)
        val btnAdvanced = findViewById<Button>(R.id.btnAdvanced)

        val openActivity = { targetClass: Class<*> ->
            val intent = Intent(this, targetClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        btnStandard.setOnClickListener { openActivity(MainActivity::class.java) }
        btnAdvanced.setOnClickListener { openActivity(AdvancedActivity::class.java) }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity()
                } else {
                    Toast.makeText(
                        this@StartActivity,
                        getString(R.string.msg_smart_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }
}