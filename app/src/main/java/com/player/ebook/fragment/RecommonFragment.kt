package com.player.ebook.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.player.ebook.R
import com.player.ebook.common.State
import com.player.ebook.config.Api
import com.player.ebook.entity.BannerEntity
import com.player.ebook.http.RequestUtils
import com.player.ebook.http.ResultEntity
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class RecommonFragment : ViewPagerFragment() {

    internal var view:View ?= null
    var classify:String ?= null
    private var count = 4//表示初始化只加载两个分类，后面的做滚动懒加载
    private lateinit var classifyList: List<Map<*,*>>
    private lateinit var fragmentTransaction:FragmentTransaction
    var loading:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.recommon_fragment, container, false)
        val bundle = arguments
        classifyList = JSON.parseArray(bundle!!.getString("classifyList"), Map::class.java)
        classify = bundle.getString("classify")
        getBannerData()
        initClassifyView()
        return view
    }

    /**
     * @author: wuwenqiang
     * @description: 获取轮播数据
     * @date: 2021-01-23 14:16
     */
    fun getBannerData() {
        var call: Call<ResultEntity> = RequestUtils.intanst.getBanner()
        call.enqueue(object : Callback<ResultEntity> {
            //请求成功时回调
            override fun onResponse(
                call: Call<ResultEntity>?,
                response: Response<ResultEntity>
            ) {
                // 请求处理,输出结果
                initBannerView(JSON.parseArray(JSON.toJSONString(response.body().data),BannerEntity::class.java))
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
     * @description: 初始化轮播图
     * @date: 2021-01-23 14:16
     */
    private fun initBannerView(list: List<BannerEntity>) {

        //方法二：使用自带的图片适配器
        var banner:Banner<BannerEntity,BannerImageAdapter<BannerEntity>> = view!!.findViewById(R.id.banner)
        banner.setAdapter(object : BannerImageAdapter<BannerEntity>(list) {
            override fun onBindView(
                holder: BannerImageHolder,
                bannerEntity: BannerEntity,
                position: Int,
                size: Int
            ) {
                //图片加载自己实现
                Glide.with(holder.imageView)
                    .load(Api.HOST + bannerEntity.loacalImg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }).setIndicator(CircleIndicator(context)).setBannerRound(20.0f)
    }

    /**
     * @author: wuwenqiang
     * @description: 初始化加载fragment
     * @date: 2021-01-25 21:24
     */
    private fun initClassifyView() {
        fragmentTransaction = fragmentManager!!.beginTransaction()
        for (i in 1 until count) {
//            this.addFragment(i)
            val categoryFragment = CategoryFragment()
            val bundle = Bundle()
            bundle.putString("classify", classifyList[i]["classify"] as String)
            categoryFragment.arguments = bundle
            fragmentTransaction.add(R.id.book_category, categoryFragment)
        }
        fragmentTransaction.commit()
    }

    /**
     * @author: wuwenqiang
     * @description: 逐个添加fragment
     * @date: 2021-01-25 21:24
     */
    private fun addFragment(index: Int) {
        val categoryFragment = CategoryFragment()
        val bundle = Bundle()
        bundle.putString("classify", classifyList[index]["classify"] as String)
        categoryFragment.arguments = bundle
        fragmentTransaction.add(R.id.book_category, categoryFragment)
    }

    /**
     * @author: wuwenqiang
     * @description: 容器滚动时做懒加载
     * @date: 2021-01-25 21:24
     */
    fun loadMoreData() {
        if (count < classifyList.size && loading == false) {
            loading = true
            fragmentTransaction = fragmentManager!!.beginTransaction()
            addFragment(count)
            fragmentTransaction.commit()
            count++
            loading = false
        }
    }
}
