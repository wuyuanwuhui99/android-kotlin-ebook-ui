package com.player.ebook.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.player.ebook.R
import com.player.ebook.common.State

class NavAdapter (
    internal var context: Context,
    private val classifyList: List<Map<*, *>>,
    private val viewPage: ViewPager
): BaseAdapter() {

    private lateinit var viewHolder: ViewHolder

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        var convertView = convertView
//        viewHolder = NavAdapter.ViewHolder()
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.nav_item, null)
//
//            viewHolder.textView = convertView!!.findViewById<TextView>(R.id.tvbutn)
//            viewHolder.textView!!.text = classifyList[position]["classify"] as String?
//            viewHolder.textView!!.setTextColor(if (position === 0) Color.parseColor("#039769") else Color.parseColor("#000000"))
//
//            viewHolder.underline = convertView.findViewById<View>(R.id.underline)
//            viewHolder.underline!!.visibility = if (position === 0) View.VISIBLE else View.INVISIBLE
//            convertView.tag = viewHolder
//            convertView.setOnClickListener { v ->
//                viewPage.setCurrentItem(position)
//                for(i in 0 until parent!!.childCount){
//                    var viewHolder = parent.getChildAt(i).tag as ViewHolder
//                    viewHolder.textView!!.setTextColor(if (i == position)  Color.parseColor("#039769") else  Color.parseColor("#000000"))
//                    viewHolder.underline!!.visibility = if (i == position) View.VISIBLE else View.INVISIBLE
//                }
//            }
//        } else {
//            viewHolder = convertView.tag as NavAdapter.ViewHolder
//        }
        val myView  = LayoutInflater.from(context).inflate(R.layout.nav_item, null)
        val textView:TextView = myView.findViewById<TextView>(R.id.tvbutn)
        textView.setText(classifyList[position]["classify"] as String?)
        textView.setTextColor(if (position === 0) Color.parseColor("#039769") else Color.parseColor("#000000"))
        val  undeline:View = myView.findViewById<View>(R.id.underline)
        undeline.visibility  = if (position === 0) View.VISIBLE else View.INVISIBLE
        myView.setOnClickListener { v ->
            State.classify = (classifyList[position]["classify"] as String?).toString()
            viewPage.setCurrentItem(position)
            for(i in 0 until parent!!.childCount){
                var viewChild = parent.getChildAt(i)
                viewChild.findViewById<TextView>(R.id.tvbutn).setTextColor(if (i == position)  Color.parseColor("#039769") else  Color.parseColor("#000000"))
                viewChild.findViewById<View>(R.id.underline).visibility = if (i == position) View.VISIBLE else View.INVISIBLE
            }
        }
        return myView
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return classifyList.size
    }

    class ViewHolder {
        internal var textView: TextView? = null
        internal var underline: View? = null
    }
}