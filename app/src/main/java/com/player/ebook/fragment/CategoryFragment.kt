package com.player.ebook.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.player.ebook.R
import com.player.ebook.adapter.BookAdapter
import com.player.ebook.common.State
import com.player.ebook.entity.BookEntity
import com.player.ebook.http.RequestUtils
import com.player.ebook.http.ResultEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  CategoryFragment : Fragment() {

    private lateinit var classify:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        classify = bundle!!.getString("classify").toString()
        getCourseData()
        return  inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.category_name).setText(classify)
        var icon:ImageView = view.findViewById<ImageView>(R.id.icon_category)
        when(classify){
            "前端" -> icon.setImageResource(R.mipmap.icon_front)
            "后端" -> icon.setImageResource(R.mipmap.icon_backstage)
            "移动端" -> icon.setImageResource(R.mipmap.icon_mobile)
        }
        view.findViewById<TextView>(R.id.category_name).setText(classify)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getCourseData() {
        var call: Call<ResultEntity> = RequestUtils.getIntanst().findBootList(State.token, classify,0,6)
        call.enqueue(object : Callback<ResultEntity> {
            //请求成功时回调
            override fun onResponse(
                call: Call<ResultEntity>?,
                response: Response<ResultEntity>
            ) {
                // 请求处理,输出结果
                val bookList = JSON.parseArray(JSON.toJSONString(response.body().data), BookEntity::class.java)
                //渲染导航条
                initCourseView(bookList)
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

    private fun initCourseView(courseList: List<BookEntity>) {
        val gridView = view!!.findViewById<GridView>(R.id.book_grid)
        val categoryAdapter = BookAdapter(context!!, courseList)
        gridView.adapter = categoryAdapter
    }
}