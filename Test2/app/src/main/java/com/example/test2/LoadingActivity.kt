package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
            finish()
        }, 300)
    }
}