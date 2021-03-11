package com.player.ebook.utils


import android.os.Handler
import android.os.Message

class MyHander : Handler() {

    override fun handleMessage(msg: Message) {

    }

    companion object {
        val instance: MyHander
            get() = MyHander()
    }
}
