package com.player.ebook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.player.ebook.R
import com.player.ebook.config.Api
import com.player.ebook.entity.BookEntity
import com.player.ebook.view.RoundImageView


class BookAdapter(
    internal var context: Context,
    private val bookList: List<BookEntity>
): BaseAdapter() {

    private lateinit var viewHolder: ViewHolder

    override fun getCount(): Int {
        return bookList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        viewHolder = ViewHolder()
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_item, null)

            viewHolder.tv = convertView!!.findViewById<View>(R.id.category_item_name) as TextView
            viewHolder.tv!!.text = bookList[position].name

            viewHolder.iv = convertView.findViewById<View>(R.id.category_item_img) as RoundImageView
            viewHolder.iv!!.setImageURL(Api.HOST +  bookList[position].localImg)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        return convertView
    }

    class ViewHolder {
        internal var tv: TextView? = null
        internal var iv: RoundImageView? = null
    }
}
