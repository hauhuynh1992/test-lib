package com.phillip.test

import android.content.Context
import android.widget.Toast

class MToast {

    companion object {
        fun showMyMessage(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}