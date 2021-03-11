package com.player.ebook.http

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log

import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.Locale

object HttpUtil {

    private val TIMEOUT_IN_MILLIONS = 5000

    /**
     * 异步的Get请求
     * @param urlStr
     * @param callBack
     */
    fun doGetAsyn(urlStr: String, token: String?, callBack: HttpCallBackListener?) {
        Thread(Runnable {
            try {
                val jsonObject = doGet(urlStr, token)
                jsonObject?.let { callBack!!.onFinish(it) }
            } catch (e: Exception) {
                callBack!!.onError(e)
                e.printStackTrace()
            }
        }).start()
    }

    /**
     * 异步的Get请求
     * @param urlStr
     * @param handler
     */
    fun doGetAsyn(urlStr: String, token: String?, handler: Handler?) {
        Thread(Runnable {
            try {
                val jsonObject = doGet(urlStr, token)
                if (handler != null) {
                    val message = handler.obtainMessage(1, jsonObject)
                    handler.sendMessage(message)
                }
            } catch (e: Exception) {
                val message = handler!!.obtainMessage(2, e)
                handler.sendMessage(message)
                e.printStackTrace()
            }
        }).start()
    }


    /**
     * 异步的Post请求
     * @param urlStr
     * @param params
     * @param callBack
     * @throws Exception
     */
    fun doPostAsyn(
        urlStr: String, params: String, token: String,
        callBack: HttpCallBackListener?
    ) {
        Thread(Runnable {
            try {
                val jsonObject = doPost(urlStr, params, token)
                jsonObject?.let { callBack!!.onFinish(it) }
            } catch (e: Exception) {
                callBack!!.onError(e)
                e.printStackTrace()
            }
        }).start()
    }


    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    fun doGet(urlStr: String, token: String?): JSONObject? {
        var url: URL? = null
        var conn: HttpURLConnection? = null
        var `is`: InputStream? = null
        val baos: ByteArrayOutputStream? = null
        try {
            url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = TIMEOUT_IN_MILLIONS
            conn.connectTimeout = TIMEOUT_IN_MILLIONS
            conn.requestMethod = "GET"
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.setRequestProperty("Accept-Charset", "UTF-8")
            conn.setRequestProperty("contentType", "UTF-8")
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8")
            if (token != null && "" != token) conn.setRequestProperty("Authorization", token)
            conn.setRequestProperty("Accept-Language", Locale.getDefault().toString())
            if (conn.responseCode == 200) {
                `is` = conn.inputStream
                var reader = BufferedReader(InputStreamReader(`is`,"UTF-8"))
                var strBuilder = StringBuilder()
                reader.forEachLine {
                    strBuilder.append(it)
                    Log.e("TAG", it)
                }
                return JSONObject(strBuilder.toString())
            } else {
                throw RuntimeException(" responseCode is not 200 ... ")
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            conn!!.disconnect()
        }
        return null
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param  请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    fun doPost(url: String, param: String?, token: String?): JSONObject? {
        var out: PrintWriter? = null
        var `in`: BufferedReader? = null
        var result = ""
        try {
            val realUrl = URL(url)
            val conn = realUrl.openConnection() as HttpURLConnection
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.requestMethod = "POST"
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
            conn.setRequestProperty("charset", "utf-8")
            conn.useCaches = false
            // 发送POST请求必须设置如下两行
            conn.doOutput = true
            conn.doInput = true
            conn.readTimeout = TIMEOUT_IN_MILLIONS
            conn.connectTimeout = TIMEOUT_IN_MILLIONS
            if (token != null && "" != token) conn.setRequestProperty("Authorization", token)
            if (param != null && param.trim { it <= ' ' } != "") {
                // 获取URLConnection对象对应的输出流
                out = PrintWriter(conn.outputStream)
                // 发送请求参数
                out.print(param)
                // flush输出流的缓冲
                out.flush()
            }
            // 定义BufferedReader输入流来读取URL的响应
            `in` = BufferedReader(InputStreamReader(conn.inputStream,"UTF-8"))
            var strBuilder = StringBuilder()
            `in`.forEachLine {
                strBuilder.append(it)
            }
            return JSONObject(strBuilder.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
                `in`?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }// 使用finally块来关闭输出流、输入流
        try {
            return JSONObject(result)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }

    }

    @Throws(IOException::class)
    fun getBitmap(path: String): Bitmap? {

        val url = URL(path)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5000
        conn.requestMethod = "GET"
        if (conn.responseCode == 200) {
            val inputStream = conn.inputStream
            return BitmapFactory.decodeStream(inputStream)
        }
        return null
    }
}
