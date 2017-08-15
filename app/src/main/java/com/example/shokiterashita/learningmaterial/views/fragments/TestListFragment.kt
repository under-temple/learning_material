package com.example.shokiterashita.learningmaterial.views.fragments

import android.app.Activity
import android.support.v4.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView


import com.ramotion.expandingcollection.ECBackgroundSwitcherView
import com.ramotion.expandingcollection.ECCardData
import com.ramotion.expandingcollection.ECPagerCardContentList
import com.ramotion.expandingcollection.ECPagerView
import com.ramotion.expandingcollection.ECPagerViewAdapter

import com.example.shokiterashita.learningmaterial.R
//import com.example.shokiterashita.learningmaterial.viewmodel.CardDataImpl

import android.util.TypedValue.COMPLEX_UNIT_DIP
import com.ramotion.expandingcollection.examples.simple.CardDataImpl

class TestListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_test_list, container, false)


        // Get pager from layout
        ecPagerView = view.findViewById(R.id.ec_pager_element)

        // Generate example dataset
        val dataset = CardDataImpl.generateExampleData()

        // Implement pager adapter and attach it to pager view
        val ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                // Data object for current card
                val cardData = data as CardDataImpl

                // Set adapter and items to current card content list
//                val listItems = cardData.listItems
//                val listItemAdapter = CardListItemAdapter(applicationContext, listItems)
//                list.adapter = listItemAdapter
//                // Also some visual tuning can be done here
//                list.setBackgroundColor(Color.WHITE)

                // Here we can create elements for head view or inflate layout from xml using inflater service
                val cardTitle = TextView(activity.applicationContext)
                cardTitle.text = cardData.cardTitle
                cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20f)
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
                head.addView(cardTitle, layoutParams)

                // Card toggling by click on head element
                head.setOnClickListener { ecPagerView!!.toggle() }
            }
        }
        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)

        // Add background switcher to pager view
        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))

        ecPagerViewAdapter.notifyDataSetChanged()

        return view
    }

}