package com.phillip.mytoastlib

import android.content.Context
import android.widget.Toast


public class ToasterMessage {


    fun showMessage(c: Context?, message: String?) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }
}