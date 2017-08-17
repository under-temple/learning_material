package com.example.shokiterashita.learningmaterial.views.fragments

import android.app.Activity
import android.support.v4.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView


import com.example.shokiterashita.learningmaterial.R
//import com.example.shokiterashita.learningmaterial.viewmodel.CardDataImpl

import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.Button
import com.example.shokiterashita.learningmaterial.views.lib.manager.extention.replaceFragment
import com.ramotion.expandingcollection.*
import com.ramotion.expandingcollection.examples.simple.CardDataImpl

class TestListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null
    lateinit var testNumber: TextView
    lateinit var previousCorrectCount: TextView
    lateinit var fastestAnswerTime: TextView
    lateinit var averageAnswerTime: TextView
    // 変数名のルールは統一する
    lateinit var startTestBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_test_list, container, false)
        ecPagerView = view.findViewById(R.id.ec_pager_element)

        //DEMO: 単語テスト一覧：101-200を選択したと想定
        val dataset = CardDataImpl.generateTestCardList(testListPosition = 1)

        //カードの発行枚数を算出するメソッド
        val ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                // Data object for current card
                val cardData = data as CardDataImpl

                //Set adapter and items to current card content list
                val listItems = cardData.listItems
                val listItemAdapter = CardListItemAdapter(activity.applicationContext, listItems)
                list.adapter = listItemAdapter
                // Also some visual tuning can be done here
                list.setBackgroundColor(Color.WHITE)

                val cardTitle = TextView(activity.applicationContext)
                cardTitle.text = cardData.cardTitle
                cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20f)
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
            }

            //カード一枚一枚の中身を、実装するメソッド
            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val res = super.instantiateItem(container, position) as ECPagerCard
                val learningMaterial = LearningMaterialTestFragment()

                //testListId = 0 or 1 or 2 ,DEMO: in this case, testListId = 1 (show testList 101-200)
                var testListPosition = 1
                var startPosition = 0

                when (testListPosition) {
                    0 -> {
                        startPosition = position + 1
                    }
                    1 -> {
                        startPosition = position + 12
                    }
                    2 -> {
                        startPosition = position + 23
                    }
                }

                val testCardData = CardDataImpl.fetchTestCardContents(startPosition,context)

                testNumber = res.findViewById(R.id.test_number)
                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTime = res.findViewById(R.id.average_answer_time)
                fastestAnswerTime = res.findViewById(R.id.fastest_answer_time)

                var testListDataIdxStart = testCardData.idx_start.toString()
                var testListDataTotalCount = testCardData.totalCount.toString()
                // 必要ないフォースアンラップは消す
                testNumber!!.text = testListDataIdxStart + "-" + testListDataTotalCount

                previousCorrectCount!!.text = testCardData.result.toString()
//                averageAnswerTime!!.text = (testCardData.result!!.toInt() / testCardData.time!!.toInt()).toString()
                fastestAnswerTime!!.text = testCardData.quicktime.toString()


                startTestBtn = res.findViewById(R.id.start_test)
                startTestBtn.setOnClickListener {

                    var fragmentManager = fragmentManager.beginTransaction()
                    var args = Bundle()
                    args.putInt("testId", startPosition)
                    learningMaterial.arguments = args

                    fragmentManager.replace(R.id.test_list,learningMaterial)
                    fragmentManager.commit()

                }

                return res
            }
        }

        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)

        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))


        return view
    }
}