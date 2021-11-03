package com.player.ebook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.player.ebook.R
import com.player.ebook.adapter.BookAdapter
import com.player.ebook.common.State
import com.player.ebook.entity.BookEntity
import com.player.ebook.http.RequestUtils
import com.player.ebook.http.ResultEntity
import com.player.ebook.view.MyGridView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabFragment : Fragment() {

    private var classify: String = ""//当前容器的标题
    private var pageNum = 0//分页页码
    private val pageSize = 9//每页数量
    private var total = 0//总数
    private var courseList: MutableList<BookEntity> = mutableListOf<BookEntity>()
    private var myGridView: MyGridView? = null
    private var loading = false//是否正在加载数据
    private var myView: View ?= null
    private var isInitLoadData:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(myView == null){
            myView = inflater.inflate(R.layout.tab_fragment, container, false)
            val bundle = arguments
            classify = bundle!!.getString("classify")?:""
            myGridView = myView!!.findViewById<View>(R.id.book_grid) as MyGridView
        }
        return myView
    }

    /**
     * @author: wuwenqiang
     * @description: 渲染列表
     * @date: 2021-01-25 23:19
     */
    private fun initCourseView() {
        val categoryAdapter = BookAdapter(context!!,courseList)
        myGridView!!.setAdapter(categoryAdapter)
    }

    /**
     * @author: wuwenqiang
     * @description: 容器滚动懒加载数据
     * @date: 2021-11-03 23:58
     */
    fun initData(classify:String){
        if(!isInitLoadData){
            this.classify = classify
            var call: Call<ResultEntity> = RequestUtils.intanst.findBootList(State.token, classify,pageNum,pageSize)
            call.enqueue(object : Callback<ResultEntity> {
                //请求成功时回调
                override fun onResponse(
                    call: Call<ResultEntity>?,
                    response: Response<ResultEntity>
                ) {
                    // 请求处理,输出结果
                    isInitLoadData = true
                    pageNum++
                    val arry = JSON.parseArray(JSON.toJSONString(response.body().data), BookEntity::class.java)
                    courseList.addAll(arry)
                    initCourseView()
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
    }

    fun loadMoreData() {
        if (loading == false && pageNum*pageSize < total) {
            loading = true
            var call: Call<ResultEntity> = RequestUtils.intanst.findBootList(State.token, classify,pageNum,pageSize)
            call.enqueue(object : Callback<ResultEntity> {
                //请求成功时回调
                override fun onResponse(
                    call: Call<ResultEntity>?,
                    response: Response<ResultEntity>
                ) {
                    loading = false
                    // 请求处理,输出结果
                    pageNum++
                    val arry = JSON.parseArray(response.body().data.toString(), BookEntity::class.java)
                    courseList.addAll(arry)
                    initCourseView()
                }

                //请求失败时回调
                override fun onFailure(
                    call: Call<ResultEntity>?,
                    throwable: Throwable
                ) {
                    loading = false
                    println(throwable.message)
                }
            })
        }
    }
}
