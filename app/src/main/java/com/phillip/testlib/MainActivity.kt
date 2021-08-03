package com.phillip.testlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.phillip.test.HelloActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val text = findViewById<TextView>(R.id.text)
        text.setOnClickListener {
            val intent = Intent(this, HelloActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
}