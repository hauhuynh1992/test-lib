package com.phillip.test.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import com.phillip.test.QuickPayOMNI
import com.phillip.test.R
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection


object QuickPayUtils {

    fun getAnmiation(position: String): Int {
        return when (position) {
            QuickPayOMNI.TOP_LEFT_POSITION,
            QuickPayOMNI.LEFT_CENTER_POSITION,
            QuickPayOMNI.BOT_LEFT_POSITION -> {
                R.style.SlideLeftAnimation
            }
            QuickPayOMNI.TOP_RIGHT_POSITION,
            QuickPayOMNI.RIGHT_CENTER_POSITION,
            QuickPayOMNI.BOT_RIGHT_POSITION -> {
                R.style.SlideRightAnimation
            }
            QuickPayOMNI.TOP_CENTER_POSITION -> {
                R.style.SlideDownAnimation
            }
            QuickPayOMNI.BOT_CENTER_POSITION -> {
                R.style.SlideUpAnimation
            }
            else -> {
                R.style.SlideRightAnimation
            }
        }
    }

    fun getScreenPosition(position: String): Int {
        return when (position) {
            QuickPayOMNI.TOP_LEFT_POSITION -> {
                Gravity.TOP or Gravity.LEFT
            }
            QuickPayOMNI.TOP_RIGHT_POSITION -> {
                Gravity.TOP or Gravity.RIGHT
            }
            QuickPayOMNI.TOP_CENTER_POSITION -> {
                Gravity.TOP or Gravity.CENTER_HORIZONTAL
            }
            QuickPayOMNI.BOT_RIGHT_POSITION -> {
                Gravity.BOTTOM or Gravity.RIGHT
            }
            QuickPayOMNI.BOT_LEFT_POSITION -> {
                Gravity.BOTTOM or Gravity.LEFT
            }
            QuickPayOMNI.BOT_CENTER_POSITION -> {
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            }
            QuickPayOMNI.LEFT_CENTER_POSITION -> {
                Gravity.LEFT or Gravity.CENTER_VERTICAL
            }
            QuickPayOMNI.RIGHT_CENTER_POSITION -> {
                Gravity.RIGHT or Gravity.CENTER_VERTICAL
            }
            else -> {
                Gravity.CENTER
            }
        }
    }

    fun getScreenHeight(mActivity: Activity, size: String): Int {
        return when (size) {
            QuickPayOMNI.SQUARE -> {
                getScreenWidth(mActivity, size)
            }
            QuickPayOMNI.SMALL_RECTANGLE -> {
                (mActivity.resources.displayMetrics.heightPixels * (450f / 1080f)).toInt()
            }
            QuickPayOMNI.LARGE_RECTANGLE_VERTICAL -> {
                (mActivity.resources.displayMetrics.heightPixels * (600f / 1080f)).toInt()
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_HORIZONTAL -> {
                (mActivity.resources.displayMetrics.heightPixels * (90f / 1080f)).toInt()
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_VERTICAL -> {
                (mActivity.resources.displayMetrics.heightPixels * (600f / 1080f)).toInt()
            }
            QuickPayOMNI.LARGE_RECTANGLE_HORIZONTAL -> {
                (mActivity.resources.displayMetrics.heightPixels * (160f / 1080f)).toInt()
            }
            else -> {
                mActivity.resources.displayMetrics.heightPixels
            }
        }
    }

    fun getScreenView(size: String): Int {
        return when (size) {
            QuickPayOMNI.SQUARE -> {
                R.layout.dialog_image_square
            }
            QuickPayOMNI.SMALL_RECTANGLE -> {
                R.layout.dialog_image_small_rectangle
            }
            QuickPayOMNI.LARGE_RECTANGLE_VERTICAL -> {
                R.layout.dialog_image_large_rectangle_vertical
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_HORIZONTAL -> {
                R.layout.dialog_image_medium_rectangle_horizontal
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_VERTICAL -> {
                R.layout.dialog_image_medium_rectangle_vertical
            }
            QuickPayOMNI.LARGE_RECTANGLE_HORIZONTAL -> {
                R.layout.dialog_image_large_rectangle_horizontal
            }
            else -> {
                R.layout.dialog_image_square
            }
        }
    }

    fun getProgressWidth(mActivity: Activity, size: String): Int {
        return when (size) {
            QuickPayOMNI.SQUARE -> {
                (mActivity.resources.displayMetrics.widthPixels * (430f / 1920f) - 130).toInt()
            }
            QuickPayOMNI.SMALL_RECTANGLE -> {
                (mActivity.resources.displayMetrics.widthPixels * (430f / 1920f) - 130).toInt()
            }
            QuickPayOMNI.LARGE_RECTANGLE_VERTICAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (430f / 1920f) - 130).toInt()
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_HORIZONTAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (928f / 1920f) - 200).toInt()
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_VERTICAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (290f / 1920f) - 130).toInt()
            }
            QuickPayOMNI.LARGE_RECTANGLE_HORIZONTAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (800f / 1920f) - 200).toInt()
            }
            else -> {
                mActivity.resources.displayMetrics.widthPixels
            }
        }
    }

    fun getScreenWidth(mActivity: Activity, size: String): Int {
        return when (size) {
            QuickPayOMNI.SQUARE -> {
                (mActivity.resources.displayMetrics.widthPixels * (430f / 1920f)).toInt()
            }
            QuickPayOMNI.SMALL_RECTANGLE -> {
                (mActivity.resources.displayMetrics.widthPixels * (430f / 1920f)).toInt()
            }
            QuickPayOMNI.LARGE_RECTANGLE_VERTICAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (430f / 1920f)).toInt()
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_HORIZONTAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (928f / 1920f)).toInt()
            }
            QuickPayOMNI.MEDIUM_RECTANGLE_VERTICAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (300f / 1920f)).toInt()
            }
            QuickPayOMNI.LARGE_RECTANGLE_HORIZONTAL -> {
                (mActivity.resources.displayMetrics.widthPixels * (800f / 1920f)).toInt()
            }
            else -> {
                mActivity.resources.displayMetrics.widthPixels
            }
        }
    }


    fun callLogApi(
        mAction: String,
        mUserID: String,
        mUserPhone: String,
        mProductID: String,
        mImage: String,
        mPlatform: String
    ) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL("https://tracking.shoppingtv.vn/log/behavior")
                val myConnection: HttpsURLConnection =
                    shoppingTVUrl.openConnection() as HttpsURLConnection
                myConnection.setRequestProperty("x-api-key", "Mb8kGgAZ7zEfTUDw")
                myConnection.setRequestProperty("Content-Type", "application/json; utf-8")
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setRequestMethod("POST")
                myConnection.setDoOutput(true)
                val parent = JSONObject()
                val data = JSONObject()
                data.put("url", mImage)
                data.put("productId", mProductID)
                parent.put("action", mAction)
                parent.put("timestamp", Calendar.getInstance().timeInMillis)
                parent.put("user_id", mUserID)
                parent.put("platform", mPlatform)
                parent.put("user_phone", mUserPhone)
                parent.put("data", data.toString())
                val wr = OutputStreamWriter(myConnection.getOutputStream())
                wr.write(parent.toString())
                wr.flush()

                val sb = StringBuilder()
                val HttpResult: Int = myConnection.getResponseCode()
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    Log.d("AAA", "HTTP_OK")
                } else {
                    Log.d("AAA", "HTTP_error:" + myConnection.getResponseMessage())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("AAA", "EEOE:" + e.message.toString())
            }
            handler.post {
                Log.d("AAA", "DONE")
            }
        }
    }
}