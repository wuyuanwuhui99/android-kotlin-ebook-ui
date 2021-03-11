package com.player.ebook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.player.ebook.R

class TabFragment2 : BaseLazyLoadFragment() {
    override fun onLazyLoad() {
        System.out.println("==================================================================")
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {
        val bundle: Bundle? = arguments
        var classify: String? = bundle!!.getString("classify")
        var myView = inflater!!.inflate(R.layout.tab_fragment2, container, false)
        myView.findViewById<TextView>(R.id.text2)
        return  myView
    }
}
