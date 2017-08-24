package com.example.shokiterashita.learningmaterial.views.fragments

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.shokiterashita.learningmaterial.R

import com.ramotion.expandingcollection.ECCardContentListItemAdapter
import java.util.Random

class CardListItemAdapter(context: Context, objects: List<String>) : ECCardContentListItemAdapter<String>(context, R.layout.fragment_test_list_item, objects) {

    //本来カードクリック時に出現するitemリストのメソッド。tagを設定して、itemを取得している。
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var rowView = convertView

        if (rowView == null) {
            val inflater = LayoutInflater.from(context)
            rowView = inflater.inflate(R.layout.fragment_test_list_item, null)
            viewHolder = ViewHolder()
            viewHolder.itemText = rowView!!.findViewById(R.id.list_item_text)
            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolder
        }

        val item = getItem(position)
        if (item != null) {
            viewHolder.itemText!!.text = item
        }
        // Example of changing/removing card list items
        viewHolder.itemText!!.setOnClickListener { v ->
            val tapToRemoveText = "Tap again to remove!"
            val view = v as TextView
            if (view.text == tapToRemoveText) {
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary))
                this@CardListItemAdapter.remove(item)
                this@CardListItemAdapter.notifyDataSetChanged()
            } else {
                view.text = tapToRemoveText
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent))
            }
        }
        return rowView
    }
    internal class ViewHolder {
        var itemText: TextView? = null
    }
}
