package com.player.ebook.http

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class LoadImagesTask(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg params: String): Bitmap? {
        var imageUrl: URL? = null
        var bitmap: Bitmap? = null
        var inputStream: InputStream? = null
        try {
            imageUrl = URL(params[0])
            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            inputStream = conn.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream!!.close()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    override fun onPostExecute(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }
}
