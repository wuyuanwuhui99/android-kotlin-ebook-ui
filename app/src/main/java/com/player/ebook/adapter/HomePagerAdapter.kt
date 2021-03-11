package com.player.ebook.adapter

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.alibaba.fastjson.JSON
import com.player.ebook.fragment.*

class HomePagerAdapter : FragmentPagerAdapter {

    private var classifyList: List<Map<*, *>>
    var myFragment = arrayListOf<Fragment>()

    constructor(manager: FragmentManager, classifyList: List<Map<*, *>>) : super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.classifyList = classifyList
        var index:Int = 0
        classifyList.forEach {
            if (index == 0) {
                var fragment:Fragment = RecommonFragment()
                val bundle = Bundle()
                bundle.putString("classifyList", JSON.toJSONString(classifyList))
                bundle.putString("classify", it["classify"] as String?)
                fragment.arguments = bundle
                myFragment.add(fragment)
            } else{
                var fragment:Fragment = TabFragment()
                val bundle2 = Bundle()
                bundle2.putString("classify", it["classify"] as String?)
                fragment.arguments = bundle2
                myFragment.add(fragment)
            }
            index++
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return classifyList[position]["classify"] as CharSequence?
    }


    override fun getItem(position: Int): Fragment {
        return myFragment[position]
    }

    override fun getCount(): Int {
        return classifyList.size
    }
}
