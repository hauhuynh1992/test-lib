package com.phillip.test.ui.chooseVoucher

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.phillip.test.BuildConfig
import com.phillip.test.models.Voucher
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

class ChooseVoucherPresenter(view: ChooseVoucherContract.View, context: Context) :
    ChooseVoucherContract.Presenter {
    private var mView: ChooseVoucherContract.View = view

    override fun checkVoucher(
        productId: String,
        orderQuality: Int,
        voucherCode: String,
        customerId: String
    ) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "voucher/apply")
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
                item.put("uid", productId)
                item.put("quantity", orderQuality)
                items.put(item)
                parent.put("pay_gateway", "momo")
                parent.put("customer_id", customerId)
                parent.put("phone_number", "")
                parent.put("customer_name", "")
                parent.put("voucher_code", voucherCode)
                parent.put("voucher_uid", "")
                parent.put("address_des", "")
                parent.put("province", "")
                parent.put("district", "")
                parent.put("address_type", 0)
                parent.put("items", items)
                parent.put("request_delivery_time", "")
                parent.put("client_notes", "")
                parent.put("created_address_des", "")
                parent.put("created_customer_name", "")
                parent.put("created_phone_number", "")
                parent.put("created_province", "")
                parent.put("created_district", "")
                parent.put("create_address_type", 0)

                val out: OutputStream = BufferedOutputStream(myConnection.getOutputStream())
                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
                writer.write(parent.toString())
                writer.flush()

                val code: Int = myConnection.getResponseCode()
                val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
                var line: String?
                if (code == HttpURLConnection.HTTP_OK) {
                    while (rd.readLine().also { line = it } != null) {
                        Log.d("AAA", line!!)
                        try {
                            var json: JSONObject = JSONObject(line)
                            val statusCode = Functions.parseJSONInt(json, "statusCode")
                            if( statusCode == 200){
                                val voucherObject = json.getJSONObject("data")
                                val voucher = Voucher(
                                    uid = Functions.parseJSONString(voucherObject, "uid"),
                                    code = Functions.parseJSONString(voucherObject, "voucher_code").toUpperCase(),
                                    lable = Functions.parseJSONString(
                                        voucherObject,
                                        "voucher_label"
                                    ),
                                    value = Functions.parseJSONDouble(
                                        voucherObject,
                                        "voucher_value"
                                    ),
                                    condition_type_label = Functions.parseJSONString(
                                        voucherObject,
                                        "condition_type_label"
                                    ),
                                    applied_value = Functions.parseJSONDouble(
                                        voucherObject,
                                        "applied_value"
                                    )
                                )
                                handler.post {
                                    mView.sendAppyVoucherSuccess(voucher)
                                }
                            } else {
                                val message = Functions.parseJSONString(json, "message")
                                handler.post {
                                    mView.sendFailed(message)
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            handler.post {
                                mView.sendFailed("Error parse JSON: " + e.message.toString())
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

    override fun getListVoucher(mUserId: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "list/voucher")
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
                parent.put("customer_id", mUserId)

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
                            var listVoucher = ArrayList<Voucher>()
                            var json: JSONObject = JSONObject(line)
                            var arrayData: JSONArray = json.getJSONArray("data")
                            for (item in 0..arrayData.length() - 1) {
                                var voucherObject: JSONObject = arrayData.getJSONObject(item)

                                var voucher = Voucher(
                                    uid = Functions.parseJSONString(voucherObject, "uid"),
                                    code = Functions.parseJSONString(voucherObject, "voucher_code"),
                                    lable = Functions.parseJSONString(
                                        voucherObject,
                                        "voucher_label"
                                    ),
                                    value = Functions.parseJSONDouble(
                                        voucherObject,
                                        "voucher_value"
                                    ),
                                    condition_type_label = Functions.parseJSONString(
                                        voucherObject,
                                        "condition_type_label"
                                    )
                                )
                                listVoucher.add(voucher)
                            }
                            handler.post {
                                mView.sendListVoucherSuccess(listVoucher)
                            }
                        } catch (e: java.lang.Exception) {
                            handler.post {
                                mView.sendFailed("Error parse JSON: " + e.message.toString())
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