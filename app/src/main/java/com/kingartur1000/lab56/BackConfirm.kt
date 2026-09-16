package com.kingartur1000.lab56

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun AppCompatActivity.setupExitConfirm() {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            MaterialAlertDialogBuilder(this@setupExitConfirm)
                .setTitle("Выход")
                .setMessage("Вы уверены, что хотите выйти из приложения?")
                .setNegativeButton("Нет") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Да") { _, _ ->
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
                .show()
        }
    })
}
