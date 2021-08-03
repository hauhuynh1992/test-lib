package com.phillip.test.ui.detailCart

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.phillip.test.BuildConfig
import com.phillip.test.models.Product
import com.phillip.test.utils.Functions
import com.phillip.test.utils.view.Config
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class DetailCartPresenter(view: DetailCartContract.View, context: Context) :
    DetailCartContract.Presenter {
    private var mView: DetailCartContract.View = view

    override fun saveCart(userId: String, product: Product) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "saveCart")
                val myConnection: HttpsURLConnection =
                    shoppingTVUrl.openConnection() as HttpsURLConnection
                myConnection.setRequestProperty("x-api-key", Config.xApiKey)
                myConnection.setRequestProperty("Content-Type", "application/json; utf-8")
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setRequestMethod("POST")
                myConnection.doOutput = true
                myConnection.doInput = true

                // Body request
                val parent = JSONObject()
                val cartItems = JSONArray()
                val item = JSONObject()
                item.put("uid", product.uid)
                item.put("quantity", product.quantity)
                cartItems.put(item)

                parent.put("uid", userId)
                parent.put("cart_items", cartItems)

                val out: OutputStream = BufferedOutputStream(myConnection.getOutputStream())
                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
                writer.write(parent.toString())
                writer.flush()

                val code: Int = myConnection.getResponseCode()
                if (code == HttpURLConnection.HTTP_OK) {
                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        Log.d("AAA", line!!)
                        try {
                            var json: JSONObject = JSONObject(line)
                            var data = Functions.parseJSONInt(json, "statusCode")
                            if (data == 200) {
                                handler.post {
                                    mView.sendSaveCartSuccess()
                                }
                            } else {
                                handler.post {
                                    mView.sendFailed("Không thể thêm vào giỏ hàng")
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            handler.post {
                                mView.sendFailed("Error parse JSOn: " + e.message.toString())
                            }
                        }
                    }

                } else {
                    handler.post {
                        mView.sendFailed("Invalid response from server: " + code)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    mView.sendFailed("ERROR Call api: " + e.message.toString())
                }
            }
        }
    }


}