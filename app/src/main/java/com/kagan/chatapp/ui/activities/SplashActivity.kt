package com.kagan.chatapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kagan.chatapp.R
import kotlinx.coroutines.*

const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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

    private fun startActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private suspend fun isLogin(): Boolean {
        delay(1000)
        return false
    }

}