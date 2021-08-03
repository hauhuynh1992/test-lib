package com.phillip.test.ui.detailProduct

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.phillip.test.BuildConfig
import com.phillip.test.models.*
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

class DetailProductPresenter(view: DetailProductContract.View, context: Context) :
    DetailProductContract.Presenter {
    private var mView: DetailProductContract.View = view

    override fun getProfile(mFptId: String, phone: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "checkCustomer_v2")
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
                parent.put("phone_number", phone)
                parent.put("fptplay_id", mFptId)

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
                            var data = json.getJSONObject("data")
                            var customerId = Functions.parseJSONString(data, "uid")
                            var phone = Functions.parseJSONString(data, "phone_number")
                            var name = Functions.parseJSONString(data, "customer_name")
                            handler.post {
                                mView.sendProfileSuccess(
                                    User(
                                        uid = customerId, phone = phone, name = name
                                    )
                                )
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

    override fun checkCart(userId: String, mProductId: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "getCart/" + userId)
                val myConnection: HttpsURLConnection =
                    shoppingTVUrl.openConnection() as HttpsURLConnection
                myConnection.setRequestProperty("x-api-key", Config.xApiKey)
                myConnection.setRequestMethod("GET")

                val code: Int = myConnection.getResponseCode()
                if (code == HttpURLConnection.HTTP_OK) {
                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        Log.d("AAA", line!!)
                        try {
                            var json: JSONObject = JSONObject(line)
                            var arrayData: JSONArray = json.getJSONArray("data")
                            var products = ArrayList<Product>()
                            for (item in 0..arrayData.length() - 1) {
                                val product =
                                    Functions.parseJSONProduct(arrayData.getJSONObject(item))
                                products.add(product)
                            }
                            handler.post {
                                var isAddToCart = false
                                products?.forEach { item ->
                                    if (item.uid.equals(mProductId)) {
                                        isAddToCart = true
                                    }
                                }
                                mView.sendCheckCartSuccess(isAddToCart)
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


    override fun getProduct(mProductId: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "list/products/uids")
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
                val uids = JSONArray()
                uids.put(mProductId)
                parent.put("uids", uids)

                val out: OutputStream = BufferedOutputStream(myConnection.getOutputStream())
                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
                writer.write(parent.toString())
                writer.flush()

                val code: Int = myConnection.getResponseCode()
                if (code == HttpURLConnection.HTTP_OK) {
                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        try {
                            var json: JSONObject = JSONObject(line)
                            var arrayData: JSONArray = json.getJSONArray("data")
                            var product: JSONObject = arrayData.getJSONObject(0)
                            val mProduct = Functions.parseJSONProduct(product)
                            handler.post {
                                mView.sendProductSuccess(mProduct)
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

    override fun addToWishList(productId: String, userId: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "addFavourite")
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
                parent.put("customer_id", userId)
                parent.put("product_uid", productId)

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
                            var isFav = Functions.parseJSONBoolean(json, "checkStatus")
                            handler.post {
                                mView.sendAddWishListSuccess(isFav)
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

    override fun deleteToWishList(productId: String, userId: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl = URL(Config.baseUrl + "deleteFavourite")
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
                parent.put("customer_id", userId)
                parent.put("product_uid", productId)

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
                            var isFav = Functions.parseJSONBoolean(json, "checkStatus")
                            handler.post {
                                mView.sendDeleteWishListSuccess(isFav)
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

    override fun checkWishList(productId: String, userId: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val shoppingTVUrl =
                    URL(Config.baseUrl + "checkFavourite/" + userId + "/" + productId)
                val myConnection: HttpsURLConnection =
                    shoppingTVUrl.openConnection() as HttpsURLConnection
                myConnection.setRequestProperty("x-api-key", Config.xApiKey)
                myConnection.setRequestMethod("GET")

                val code: Int = myConnection.getResponseCode()
                if (code == HttpURLConnection.HTTP_OK) {
                    val rd = BufferedReader(InputStreamReader(myConnection.inputStream))
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        Log.d("AAA", line!!)
                        try {
                            var json: JSONObject = JSONObject(line)
                            var isFav = Functions.parseJSONBoolean(json, "checkStatus")
                            handler.post {
                                mView.sendCheckWishListSuccess(isFav)
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