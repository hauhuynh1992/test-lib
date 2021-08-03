package com.phillip.testlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.phillip.test.HelloActivity
import com.phillip.test.MToast
import com.phillip.test.QuickPayOMNI
import com.phillip.test.ui.main.MainQuickPayDialog

class MainActivity : AppCompatActivity(), QuickPayOMNI.QuickPayListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val text = findViewById<TextView>(R.id.text)
        text.setOnClickListener {
//
            val diaot = MainQuickPayDialog(
                "1963158",
                "0989430509",
                "0x7f04",
                "box2019", {

                }
            )
            diaot.show(supportFragmentManager, diaot.tag)
        }
    }

    override fun onQuickPayExit() {
    }

    override fun onBannerExit() {
    }
}