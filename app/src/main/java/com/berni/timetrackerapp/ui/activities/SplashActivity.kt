package com.berni.timetrackerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private var mBinding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        mBinding!!.ivTimeTrackerLogo.animation = splashAnimation

        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                switchToMainActivity()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun switchToMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}