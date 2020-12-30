package com.kagan.chatapp.ui.activities

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kagan.chatapp.databinding.ActivitySplashBinding
import com.kagan.chatapp.viewmodels.UserPreferenceViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var userPreferenceViewModel: UserPreferenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreferenceViewModel = ViewModelProvider(this).get(UserPreferenceViewModel::class.java)

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
        userPreferenceViewModel.getUser.observe(this, {
            val user = it
            CoroutineScope(Main).launch {
                delay(1000)

                if (user == null) {
                    startActivity()
                } else {
                    if (user.isNotEmpty()) {
                        Log.d(TAG, "onCreate: Main activity have not created yet")
                        Toast.makeText(
                            applicationContext,
                            "onCreate: Main activity have not created yet",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivity()
                    }
                }
            }
        })
    }
}