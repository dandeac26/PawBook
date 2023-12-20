package com.example.pawbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.pawbook.R
import com.example.pawbook.model.BreedsItem

class BreedsAdapter(private val context: Context,
                    private var dataSource: MutableList<BreedsItem>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(p0: Int): Any {
        return dataSource[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val rowView = p1 ?: inflater.inflate(
            R.layout.breeds_list_item,
            p2, false)
        rowView.findViewById<TextView>(R.id.breedsTitleTextView).text = dataSource[p0].title

        return rowView
    }
}