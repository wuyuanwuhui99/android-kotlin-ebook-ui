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
import com.player.ebook.entity.EventEntity
import com.player.ebook.http.RequestUtils
import com.player.ebook.http.ResultEntity
import com.player.ebook.view.MyGridView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    override fun onHiddenChanged(isVisibleToUser: Boolean){
        super.setUserVisibleHint(isVisibleToUser)
        System.out.println("111111111111111111111111111111")
        System.out.println(isVisibleToUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * @author: wuwenqiang
     * @description: 容器滚动懒加载数据
     * @date: 2021-01-25 23:19
     */
    private fun onLazyLoad() {
        var call: Call<ResultEntity> = RequestUtils.intanst.findBootList(State.token, classify,pageNum,pageSize)
        call.enqueue(object : Callback<ResultEntity> {
            //请求成功时回调
            override fun onResponse(
                call: Call<ResultEntity>?,
                response: Response<ResultEntity>
            ) {
                // 请求处理,输出结果
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

    override fun onDestroyView() {
        super.onDestroyView()
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

    //事件订阅者
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(eventEntity: EventEntity) {
        var eventName:String = classify+"loadMoreData"
        if (eventName.equals(eventEntity.what) && pageNum*pageSize < total) {//classify表示从HomeFragment派发过来的滚动事件
            loadMoreData()//加载更多数据
        }else if(classify == State.classify && isInitLoadData == false){//点击切换导航栏派发过来的事件
            onLazyLoad()
            isInitLoadData = true
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        System.out.println("=============================================")
        System.out.println(isVisibleToUser)
    }


    private fun loadMoreData() {
        if (total > courseList.size && loading == false) {
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
