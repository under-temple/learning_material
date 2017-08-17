package com.example.shokiterashita.learningmaterial.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import com.example.shokiterashita.learningmaterial.R
import com.ramotion.expandingcollection.ECCardData
import com.ramotion.expandingcollection.ECPagerCard
import com.ramotion.expandingcollection.ECPagerView
import com.ramotion.expandingcollection.ECPagerViewAdapter
import com.ramotion.expandingcollection.examples.simple.CardDataImpl

/**
 * Created by shokiterashita on 2017/08/17.
 */
class WordListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null
    lateinit var testNumber: TextView
    lateinit var previousCorrectCount: TextView
    lateinit var fastestAnswerTime: TextView
    lateinit var averageAnswerTime: TextView
    lateinit var startTestBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_word_list, container, false)

        ecPagerView = view.findViewById(R.id.ec_word_pager_element)

        val dataset = CardDataImpl.generateTestCardList(testListPosition = 1, context = context)//DEMO: 単語テスト一覧：101-200を選択したと想定


        // Implement pager adapter and attach it to pager view
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

                // add cardTitle : String
                val cardTitle = TextView(activity.applicationContext)
                cardTitle.text = cardData.cardTitle
                cardTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
                head.addView(cardTitle, layoutParams)
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val res = super.instantiateItem(container, position) as ECPagerCard
                var testListPosition = 1 //testListId = 0 or 1 or 2 ,DEMO: in this case, testListId = 1 (show testList 101-200)
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

                var testCardData = CardDataImpl.fetchTestCardContents(startPosition,context)

                testNumber = res.findViewById(R.id.test_number)
                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTime = res.findViewById(R.id.average_answer_time)
                fastestAnswerTime = res.findViewById(R.id.fastest_answer_time)

                var testListDataIdxStart = testCardData.idx_start.toString()
                var testListDataTotalCount = testCardData.totalCount.toString()
                testNumber!!.text = testListDataIdxStart + "-" + testListDataTotalCount

                previousCorrectCount!!.text = testCardData.result.toString()
//                averageAnswerTime!!.text = (testCardData.result!!.toInt() / testCardData.time!!.toInt()).toString()
                fastestAnswerTime!!.text = testCardData.quicktime.toString()

                startTestBtn = res.findViewById(R.id.start_test)
                startTestBtn.setOnClickListener {

                    var learningMaterial: LearningMaterialTestFragment = LearningMaterialTestFragment()
                    var transaction = parentFragment.fragmentManager.beginTransaction()
                    var args = Bundle()

                    args.putInt("testId", startPosition)
                    learningMaterial.arguments = args

                    transaction.add(R.id.start_test,learningMaterial)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }
                return res
            }
        }

        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)

        // Add background switcher to pager view
        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))


        return view
    }
}