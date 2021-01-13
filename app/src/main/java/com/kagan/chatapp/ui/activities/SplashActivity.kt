package com.kagan.chatapp.ui.activities

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kagan.chatapp.databinding.ActivitySplashBinding
import com.kagan.chatapp.viewmodels.TokenPreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

const val TAG = "SplashActivity"

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val tokenPreferenceViewModel: TokenPreferenceViewModel by viewModels()

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

        observe()
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

    private fun observe() {
        tokenPreferenceViewModel.accessToken.observe(this, { accessToken ->
            CoroutineScope(Main).launch {
                delay(1000)

                if (accessToken == null) {
                    startActivity()
                } else {

                }
            }
        })
    }
}