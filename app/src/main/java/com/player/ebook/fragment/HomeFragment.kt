package com.player.ebook.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.player.ebook.R
import com.player.ebook.adapter.HomePagerAdapter
import com.player.ebook.common.State
import com.player.ebook.config.Api
import com.player.ebook.entity.BookEntity
import com.player.ebook.entity.EventEntity
import com.player.ebook.entity.UserEntity
import com.player.ebook.http.RequestUtils
import com.player.ebook.http.ResultEntity
import com.player.ebook.view.CircleImageView
import com.player.ebook.view.WrapContentHeightViewPager
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var sp: SharedPreferences//缓存
    private var userData: UserEntity? = null//用户数据
    private var token:String? = null
    private var rootView:View?=null
    private lateinit var viewPagerAdapter:HomePagerAdapter
    private var position:Int = 0//默认展示首页，下标为0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.home_fragment, container, false)
            getToken()
            getUserData()
            onScrollview() //监听滚动事件，滚动懒加载
        }
        return rootView
    }

    /**
     * @author: wuwenqiang
     * @description: 设置缓存中的token
     * @date: 2021-01-31 12:21
     */
    fun getToken(){
        sp = context!!.getSharedPreferences("user", 0)
        token = if (sp != null) sp.getString("token", null) else null
    }

    fun getUserData(){
        var token:String = sp.getString("token","").toString()
        var call:Call<ResultEntity> = RequestUtils.intanst.getUserData(token)
        call.enqueue(object : Callback<ResultEntity> {
            //请求成功时回调
            override fun onResponse(
                call: Call<ResultEntity>?,
                response: Response<ResultEntity>
            ) {
                // 请求处理,输出结果
                val gson = Gson()
                userData = gson.fromJson(gson.toJson(response.body().data),UserEntity::class.java)
                State.token = response.body().token.toString()
                sp.edit().putString("token",  State.token).commit()
                setAvater()
                getAllClassify()
            }

            //请求失败时回调
            override fun onFailure(
                call: Call<ResultEntity>?,
                throwable: Throwable
            ) {
                println(throwable.message)
            }
        })
    }

    /**
     * @author: wuwenqiang
     * @description: 设置头像
     * @date: 2021-01-13 22:24
     */
    private fun setAvater() {
        val img = Api.HOST + userData!!.avater
        val circleImageView:CircleImageView = rootView!!.findViewById(R.id.avater)
        Glide.with(this).load(img).into(circleImageView)
    }

    private fun onScrollview() {
        val myScrollView:ScrollView = rootView!!.findViewById(R.id.myScrollView)
        myScrollView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val scrollView = (v as ScrollView).getChildAt(0)
                if (scrollView.measuredHeight <= v.getScrollY() + v.getHeight()) {
                    //加载数据代码
                    if(position == 0){
                        (viewPagerAdapter.myFragment[position] as RecommonFragment).loadMoreData()
                    }else{
                        (viewPagerAdapter.myFragment[position] as TabFragment).loadMoreData()
                    }
                }
            }
            false
        }
    }

    private fun getAllClassify() {
        var call:Call<ResultEntity> = RequestUtils.intanst.getAllClassify()
        call.enqueue(object : Callback<ResultEntity> {
            //请求成功时回调
            override fun onResponse(
                call: Call<ResultEntity>?,
                response: Response<ResultEntity>
            ) {
                var classifyList = JSON.parseArray(JSON.toJSONString(response.body().data), Map::class.java)
                initNavView(classifyList)
            }

            //请求失败时回调
            override fun onFailure(
                call: Call<ResultEntity>?,
                throwable: Throwable
            ) {
                println(throwable.message)
            }
        })
    }

    /**
     * @author: wuwenqiang
     * @description: 渲染导航条
     * @date: 2021-01-16 12:00
     */
    private fun initNavView(classifyList: List<Map<*, *>>) {
        val tabLayout: TabLayout = view!!.findViewById(R.id.tablayout)
        val viewPager:WrapContentHeightViewPager = view!!.findViewById(R.id.viewPager)
        viewPagerAdapter = HomePagerAdapter(fragmentManager!!, classifyList)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab) {

            }
            override fun onTabUnselected(p0: TabLayout.Tab) {

            }
            override fun onTabSelected(p0: TabLayout.Tab) {
                if(p0.position !== 0){
                    position = p0.position
                    (viewPagerAdapter.myFragment[p0.position] as TabFragment).initData(p0.text as String)
                }
            }
        })
    }
}