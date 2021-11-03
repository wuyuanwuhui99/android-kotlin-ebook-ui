package com.player.ebook.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.player.ebook.R
import com.player.ebook.fragment.HomeFragment
import com.player.ebook.fragment.BookshelfFragment
import com.player.ebook.fragment.ClassifyFragment
import com.player.ebook.fragment.UserFragment
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var mViewPager: ViewPager//容器
    private val listFragment = ArrayList<Fragment>()

    //导航栏布局栏
    lateinit var homeLinearLayout: LinearLayout
    lateinit var classifyLinearLayout: LinearLayout
    lateinit var bookshelfLinearLayout: LinearLayout
    lateinit var userLinearLayout: LinearLayout

    //导航栏图标
    lateinit var homeImg: ImageView
    lateinit var classifyImg: ImageView
    lateinit var bookshelfImg: ImageView
    lateinit var userImg: ImageView

    //导航栏文字
    lateinit var homeText: TextView
    lateinit var classifyText: TextView
    lateinit var bookshelfText: TextView
    lateinit var userText: TextView

    private lateinit var sp: SharedPreferences//缓存
    private var userData: JSONObject? = null//用户数据
    private var token:String? = null

    lateinit var homeFragment:HomeFragment
    lateinit var classifyFragment:ClassifyFragment
    lateinit var bookshelfFragment:BookshelfFragment
    lateinit var userFragment:UserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getToken()
        initView()//初始化视图
        initEvent()//绑定底部导航栏事件
        setSelect(0)//设置默认选中的tab
    }

    private fun initEvent() {
        homeLinearLayout.setOnClickListener(this)
        classifyLinearLayout.setOnClickListener(this)
        bookshelfLinearLayout.setOnClickListener(this)
        userLinearLayout.setOnClickListener(this)
    }

    private fun initView() {
        mViewPager = findViewById<View>(R.id.viewpager) as ViewPager

        //导航栏对应内容区布局
        val homeFragment = HomeFragment()
        classifyFragment = ClassifyFragment()
        bookshelfFragment = BookshelfFragment()
        userFragment = UserFragment()

        //导航栏布局栏
        homeLinearLayout = findViewById<View>(R.id.home) as LinearLayout
        classifyLinearLayout = findViewById<View>(R.id.classify) as LinearLayout
        bookshelfLinearLayout = findViewById<View>(R.id.bookshelf) as LinearLayout
        userLinearLayout = findViewById<View>(R.id.user_center) as LinearLayout

        //导航栏图标
        homeImg = findViewById<View>(R.id.home_img) as ImageView
        classifyImg = findViewById<View>(R.id.classify_img) as ImageView
        bookshelfImg = findViewById<View>(R.id.bookshelf_img) as ImageView
        userImg = findViewById<View>(R.id.user_img) as ImageView

        //导航栏文字
        homeText = findViewById<View>(R.id.home_text) as TextView
        classifyText = findViewById<View>(R.id.classify_text) as TextView
        bookshelfText = findViewById<View>(R.id.bookshelf_text) as TextView
        userText = findViewById<View>(R.id.user_text) as TextView


        listFragment.add(homeFragment)
        listFragment.add(classifyFragment)
        listFragment.add(bookshelfFragment)
        listFragment.add(userFragment)

        val mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {  //适配器直接new出来
            override fun getItem(position: Int): Fragment {
                return listFragment.get(position)//直接返回
            }

            override fun getCount(): Int {
                return listFragment.size //放回tab数量
            }
        }

        mViewPager.setAdapter(mAdapter)  //加载适配器
        mViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {  //监听界面拖动
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val currentItem = mViewPager.getCurrentItem() //获取当前界面
                resetImg()  //将所有图标变暗
                tab(currentItem) //切换图标亮度
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun tab(i: Int) {  //用于屏幕脱拖动时切换底下图标，只在监听屏幕拖动中调用
        val color = this.resources.getColor(R.color.navigate_active)
        when (i) {
            0 -> {
                homeImg.setImageResource(R.mipmap.icon_home_active)
                homeText.setTextColor(color)
            }
            1 -> {
                classifyImg.setImageResource(R.mipmap.icon_classify_active)
                classifyText.setTextColor(color)
                classifyFragment.initData()
            }
            2 -> {
                bookshelfImg.setImageResource(R.mipmap.icon_bookshelf_active)
                bookshelfText.setTextColor(color)
                bookshelfFragment.intiData()
            }
            3 -> {
                userImg.setImageResource(R.mipmap.icon_user_active)
                userText.setTextColor(color)
                userFragment.initData()
            }
        }
    }

    //自定义一个方法
    private fun setSelect(i: Int) {
        mViewPager.setCurrentItem(i)//切换界面
    }

    override fun onClick(view: View) {  //设置点击的为；亮色
        resetImg()
        when (view.id) {
            R.id.home -> {
                setSelect(0)
                homeImg.setImageResource(R.mipmap.icon_home_active)
            }
            R.id.classify -> {
                setSelect(1)
                classifyImg.setImageResource(R.mipmap.icon_classify_active)
            }
            R.id.bookshelf -> {
                setSelect(2)
                bookshelfImg.setImageResource(R.mipmap.icon_bookshelf_active)
            }
            R.id.user_center -> {
                setSelect(3)
                userImg.setImageResource(R.mipmap.icon_user_active)
            }
        }
    }

    //设置暗色
    private fun resetImg() {
        homeImg.setImageResource(R.mipmap.icon_home)
        classifyImg.setImageResource(R.mipmap.icon_classify)
        bookshelfImg.setImageResource(R.mipmap.icon_bookshelf)
        userImg.setImageResource(R.mipmap.icon_user)

        val color = this.resources.getColor(R.color.navigate)
        homeText.setTextColor(color)
        classifyText.setTextColor(color)
        bookshelfText.setTextColor(color)
        userText.setTextColor(color)
    }

    /**
     * @author: wuwenqiang
     * @description: 设置缓存中的token
     * @date: 2021-01-31 12:21
     */
    fun getToken(){
        sp = getSharedPreferences("user", 0)
        token = if (sp != null) sp.getString("token", null) else null
    }
}
