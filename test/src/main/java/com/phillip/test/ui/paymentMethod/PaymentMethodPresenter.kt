package com.phillip.test.ui.paymentMethod

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

class PaymentMethodPresenter(view: PaymentMethodContract.View, context: Context) :
    PaymentMethodContract.Presenter {
    private var mView: PaymentMethodContract.View = view

    override fun submitOrder(
        customerId: String,
        name: String,
        phone: String,
        voucherCode: String,
        voucherId: String,
        requestType: Int,
        list: ArrayList<Product>
    ) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "submitOrder")
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
                val item = JSONObject()
                val items = JSONArray()
                item.put("uid", list.get(0).uid)
                item.put("quantity", list.get(0).quantity)
                items.put(item)
                parent.put("pay_gateway", "cod")
                parent.put("customer_id", customerId)
                parent.put("phone_number", phone)
                parent.put("customer_name", name)
                parent.put("voucher_code", voucherCode)
                parent.put("voucher_uid", voucherId)
                parent.put("address_des", "")
                parent.put("province", "")
                parent.put("district", "")
                parent.put("address_type", 0)
                parent.put("items", items)
                parent.put("request_delivery_time", "all_day")
                parent.put("client_notes", "")
                parent.put("created_address_des", "")
                parent.put("created_customer_name", name)
                parent.put("created_phone_number", phone)
                parent.put("created_province", "")
                parent.put("created_district", "")
                parent.put("create_address_type", 0)

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
                            var orderId = Functions.parseJSONString(json, "order_id")
                            handler.post {
                                mView.sendOrderSuccess(orderId)
                            }
                        } catch (e: java.lang.Exception) {
                            handler.post {
                                mView.sendFailed("Error parse JSON: " + e.message.toString())
                            }
                        }
                    }

                } else {
                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        Log.d("AAA", line!!)
                        try {
                            var json: JSONObject = JSONObject(line)
                            var message = Functions.parseJSONString(json, "message")
                            handler.post {
                                mView.sendFailed(message)
                            }
                        } catch (e: java.lang.Exception) {
                            handler.post {
                                mView.sendFailed("Error parse JSON: " + e.message.toString())
                            }
                        }
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