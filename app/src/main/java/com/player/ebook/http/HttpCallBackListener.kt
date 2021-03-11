package com.player.ebook.http

import org.json.JSONException
import org.json.JSONObject

interface HttpCallBackListener {
    @Throws(JSONException::class)
    fun onFinish(response: JSONObject)

    fun onError(e: Exception)
}
