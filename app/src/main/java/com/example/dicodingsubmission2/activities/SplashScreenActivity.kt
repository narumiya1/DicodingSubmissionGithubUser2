package com.example.dicodingsubmission2.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingsubmission2.MainActivity
import com.example.dicodingsubmission2.R

class SplashScreenActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            run {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 5000)
    }
}