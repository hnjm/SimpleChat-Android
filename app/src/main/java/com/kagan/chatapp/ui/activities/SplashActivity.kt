package com.kagan.chatapp.ui.activities

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kagan.chatapp.databinding.ActivitySplashBinding
import kotlinx.coroutines.*

const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        translateDownY()
        translateUpY()
        scaleUpLogo()

        loginCheck()
    }

    private fun loginCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            val isLogin = isLogin()
            withContext(Dispatchers.Main) {
                if (isLogin) {
                    Log.d(TAG, "onCreate: Main activity have not created yet")
                } else {
                    startActivity()
                }
            }
        }
    }

    private fun translateDownY() {
        val animator: ObjectAnimator =
            ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_Y, 800f)
        animator.duration = 1000
        animator.start()
    }

    private fun translateUpY() {
        val animator: ObjectAnimator =
            ObjectAnimator.ofFloat(binding.tvAppName, View.TRANSLATION_Y, -800f)
        animator.duration = 1000
        animator.start()
    }

    private fun scaleUpLogo() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 3f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 3f)

        val animator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(binding.ivLogo, scaleX, scaleY)
        animator.duration = 1000
        animator.start()
    }

    private fun startActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private suspend fun isLogin(): Boolean {
        delay(1000)
        return false
    }

}