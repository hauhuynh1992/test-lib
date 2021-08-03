package com.phillip.test

import android.app.Activity
import android.os.Bundle

class HelloActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
    }
}