package com.phillip.test.ui.quickPay

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.phillip.test.BuildConfig
import com.phillip.test.models.Product
import com.phillip.test.models.Supplier
import com.phillip.test.utils.Functions
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class QuickPayPresenter(view: QuickPayContract.View, context: Context) :
    QuickPayContract.Presenter {
    private var mView: QuickPayContract.View = view

//    override fun getProfile(mFptId: String, phone: String) {
//        val executor: ExecutorService = Executors.newSingleThreadExecutor()
//        val handler = Handler(Looper.getMainLooper())
//        executor.execute {
//            try {
//                val shoppingTVUrl = URL(Config.baseUrl + "checkCustomer_v2")
//                val myConnection: HttpsURLConnection =
//                    shoppingTVUrl.openConnection() as HttpsURLConnection
//                myConnection.setRequestProperty("x-api-key", Config.xApiKey)
//                myConnection.setRequestProperty("Content-Type", "application/json; utf-8")
//                myConnection.setRequestProperty("Accept", "application/json");
//                myConnection.setRequestMethod("POST")
//                myConnection.doOutput = true
//                myConnection.doInput = true
//
//                // Body request
//                val parent = JSONObject()
//                parent.put("phone_number", phone)
//                parent.put("fptplay_id", mFptId)
//
//                val out: OutputStream = BufferedOutputStream(myConnection.getOutputStream())
//                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
//                writer.write(parent.toString())
//                writer.flush()
//
//                val code: Int = myConnection.getResponseCode()
//                if (code == HttpURLConnection.HTTP_OK) {
//                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
//                    var line: String?
//                    while (rd.readLine().also { line = it } != null) {
//                        Log.d("AAA", line!!)
//                        try {
//                            var json: JSONObject = JSONObject(line)
//                            var data = json.getJSONObject("data")
//                            var customerId = Functions.parseJSONString(data, "uid")
//                            var phone = Functions.parseJSONString(data, "phone_number")
//                            var name = Functions.parseJSONString(data, "customer_name")
//                            handler.post {
//                                mView.sendProfileSuccess(
//                                    customerId = customerId,
//                                    name = name,
//                                    phone = phone
//                                )
//                            }
//                        } catch (e: java.lang.Exception) {
//                            handler.post {
//                                mView.sendFailed("Error parse JSOn: " + e.message.toString())
//                            }
//                        }
//                    }
//
//                } else {
//                    handler.post {
//                        mView.sendFailed("Invalid response from server: " + code)
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                handler.post {
//                    mView.sendFailed("ERROR Call api: " + e.message.toString())
//                }
//            }
//        }
//    }
//
//    override fun getProduct(mProductId: String) {
//        val executor: ExecutorService = Executors.newSingleThreadExecutor()
//        val handler = Handler(Looper.getMainLooper())
//        executor.execute {
//            try {
//                val shoppingTVUrl = URL(Config.baseUrl + "list/products/uids")
//                val myConnection: HttpsURLConnection =
//                    shoppingTVUrl.openConnection() as HttpsURLConnection
//                myConnection.setRequestProperty("x-api-key", Config.xApiKey)
//                myConnection.setRequestProperty("Content-Type", "application/json; utf-8")
//                myConnection.setRequestProperty("Accept", "application/json");
//                myConnection.setRequestMethod("POST")
//                myConnection.doOutput = true
//                myConnection.doInput = true
//
//                // Body request
//                val parent = JSONObject()
//                val uids = JSONArray()
//                uids.put(mProductId)
//                parent.put("uids", uids)
//
//                val out: OutputStream = BufferedOutputStream(myConnection.getOutputStream())
//                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
//                writer.write(parent.toString())
//                writer.flush()
//
//                val code: Int = myConnection.getResponseCode()
//                if (code == HttpURLConnection.HTTP_OK) {
//                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
//                    var line: String?
//                    while (rd.readLine().also { line = it } != null) {
//                        Log.d("AAA", line!!)
//                        try {
//                            var json: JSONObject = JSONObject(line)
//                            var arrayData: JSONArray = json.getJSONArray("data")
//                            var product: JSONObject = arrayData.getJSONObject(0)
//                            var priceObject = product.getJSONObject("product.pricing")
//                            var supplierObject = product.getJSONObject("product.supplier")
//                            val supplier = Supplier(
//                                name = Functions.parseJSONString(supplierObject, "supplier_name"),
//                                shipping_time = Functions.parseJSONInt(
//                                    supplierObject,
//                                    "shipping_time"
//                                )
//                            )
//                            val mProduct = Product(
//                                uid = Functions.parseJSONString(product, "uid"),
//                                name = Functions.parseJSONString(product, "display_name_detail"),
//                                image_cover = Functions.parseJSONString(product, "image_cover"),
////                                pricing = Functions.parseJSONDouble(priceObject, "price_with_vat"),
//                                supplier = supplier
//                            )
//                            handler.post {
//                                mView.sendProductSuccess(mProduct)
//                            }
//                        } catch (e: java.lang.Exception) {
//                            handler.post {
//                                mView.sendFailed("Error parse JSOn: " + e.message.toString())
//                            }
//                        }
//                    }
//
//                } else {
//                    handler.post {
//                        mView.sendFailed("Invalid response from server: " + code)
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                handler.post {
//                    mView.sendFailed("ERROR Call api: " + e.message.toString())
//                }
//            }
//        }
//    }
}