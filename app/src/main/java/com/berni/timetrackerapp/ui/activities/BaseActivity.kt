package com.berni.timetrackerapp.ui.activities

import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.berni.timetrackerapp.R

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false}, 2000)
    }

}