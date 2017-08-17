package com.example.shokiterashita.learningmaterial.views.fragments

import android.graphics.BitmapFactory
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
import com.ramotion.expandingcollection.*
import com.ramotion.expandingcollection.examples.simple.CardWordDataImpl

/**
 * Created by shokiterashita on 2017/08/17.
 */

class WordListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null
    private var englishWord: TextView? = null
    private var japaneseWord: TextView? = null
    private var japaneseSentence: TextView? = null
    private var englishSentence: TextView? = null
    private var previousCorrectCount: TextView? = null

    //fastestAnswerTimeと、してしまうと、Dateクラスと勘違いしてしまう恐れ。
    private var fastestAnswerTime: TextView? = null
    private var averageAnswerTime: TextView? = null

    private var showJp: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_word_list, container, false)

        //後に、Bundleクラスから、値を取得する。
        ecPagerView = view.findViewById(R.id.ec_word_pager_element)

        //DEMO: 単語テスト一覧：101-200を選択したと想定
        val dataset = CardWordDataImpl.generateWordCardList(wordListPosition = 1)

        // Implement pager adapter and attach it to pager view
        val ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                // Data object for current card
                val cardData = data as CardWordDataImpl

                //Set adapter and items to current card content list
                val listItems = cardData.listItems
                val listItemAdapter = CardListItemAdapter(activity.applicationContext, listItems)
                list.adapter = listItemAdapter
                list.setBackgroundColor(Color.WHITE)

                val cardTitle = TextView(activity.applicationContext)
                cardTitle.text = cardData.cardTitle
                cardTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val res = super.instantiateItem(container, position) as ECPagerCard

                var testListPosition = 1 //testListId = 0 or 1 or 2 ,DEMO:単語一覧101-200を選択した。後に、Bundleクラスから、getInt()メソッドを呼ぶ。
                var startPosition = 0

                when (testListPosition) {
                    0 -> {
                        startPosition = position + 1
                    }
                    1 -> {
                        startPosition = position + 101
                    }
                    2 -> {
                        startPosition = position + 201
                    }
                }
                var testCardData = CardWordDataImpl.fetchWordCardContents(startPosition,context)
                
                englishWord = res.findViewById(R.id.word_en_text)
                japaneseWord = res.findViewById(R.id.word_jp_text)
                englishSentence = res.findViewById(R.id.sentence_en_text)
                japaneseSentence = res.findViewById(R.id.sentence_jp_text)

                englishWord!!.text = testCardData.worden
                japaneseWord!!.text = testCardData.wordjp
                englishSentence!!.text = testCardData.exampleen
                japaneseSentence!!.text = testCardData.examplejp


                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTime = res.findViewById(R.id.average_answer_time)
                fastestAnswerTime = res.findViewById(R.id.fastest_answer_time)

                //クリック時に、日本語表示メソッドを作成する。
                showJp = res.findViewById(R.id.show_word_jp_btn)

                return res
            }
        }

        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)
        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))

        return view
    }
}