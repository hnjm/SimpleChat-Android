package com.kagan.chatapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kagan.chatapp.R

const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

}