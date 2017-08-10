package com.example.shokiterashita.learningmaterial.views.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.shokiterashita.learningmaterial.R
import com.ramotion.expandingcollection.ECPagerView
import com.ramotion.expandingcollection.ECBackgroundSwitcherView
import android.view.Gravity
import android.R.attr.gravity
import android.widget.FrameLayout
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.TextView
import com.ramotion.expandingcollection.ECCardData
import com.ramotion.expandingcollection.ECPagerViewAdapter

class TestListFragment : Fragment() {

    private val ecPagerView: ECPagerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_test_list, container, false)

        ecPagerView = view.findViewById(R.id.ec_pager_element) as ECPagerView
        // Generate example dataset
        val dataset = CardDataImpl.generateExampleData()

        // Implement pager adapter and attach it to pager view
        ecPagerView.setPagerViewAdapter(object : ECPagerViewAdapter(getApplicationContext(), dataset) {
            fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                // Data object for current card
                val cardData = data as CardDataImpl

                // Set adapter and items to current card content list
                list.setAdapter(CardListItemAdapter(getApplicationContext(), cardData.getListItems()))
                // Also some visual tuning can be done here
                list.setBackgroundColor(Color.WHITE)

                // Here we can create elements for head view or inflate layout from xml using inflater service
                val cardTitle = TextView(getApplicationContext())
                cardTitle.setText(cardData.getCardTitle())
                cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20f)
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
                head.addView(cardTitle, layoutParams)

                // Card toggling by click on head element
                head.setOnClickListener { ecPagerView.toggle() }
            }
        })

        // Add background switcher to pager view
        ecPagerView.setBackgroundSwitcherView(findViewById(R.id.ec_bg_switcher_element) as ECBackgroundSwitcherView)

    }


}
