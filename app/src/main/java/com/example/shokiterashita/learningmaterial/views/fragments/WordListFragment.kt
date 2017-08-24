package com.example.shokiterashita.learningmaterial.views.fragments

import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.beardedhen.androidbootstrap.TypefaceProvider
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.lib.manager.LessonMaterialManager
import com.ramotion.expandingcollection.*
import com.ramotion.expandingcollection.examples.simple.CardWordDataImpl
import java.net.URI

/**
 * Created by shokiterashita on 2017/08/17.
 */

class WordListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null
    private var englishWord: TextView? = null
    lateinit var japaneseWord: TextView
    lateinit var japaneseSentence: TextView
    private var englishSentence: TextView? = null
    private var previousCorrectCount: TextView? = null
    lateinit var testNumberLabel: TextView
    lateinit var startTestButton: Button

    //fastestAnswerTimeと、してしまうと、Dateクラスと勘違いしてしまう恐れ。
    private var fastestAnswerTime: TextView? = null
    private var averageAnswerTime: TextView? = null

    lateinit var showJpButton: ToggleButton
    lateinit var pronounceButton: com.beardedhen.androidbootstrap.AwesomeTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TypefaceProvider.registerDefaultIconSets()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_word_list, container, false)

        //後に、Bundleクラスから、値を取得する。
        ecPagerView = view.findViewById(R.id.ec_word_pager_element)

        //DEMO: 単語テスト一覧：101-200を選択したと想定
        val dataset = CardWordDataImpl.generateWordCardList(wordListPosition = 1)

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

                head.setOnClickListener{
                    Log.d("test","${listItems}")
                }
            }


            override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any?) {
                super.setPrimaryItem(container, position, `object`)

            }


            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val res = super.instantiateItem(container, position) as ECPagerCard

                //testListPosition = 0 or 1 or 2 ,DEMO:単語一覧101-200を選択した。後に、Bundleクラスから、getInt()メソッドを呼ぶ。
                val testListPosition = 1
                var wordCardId = 0

                when (testListPosition) {
                    0 -> {
                        wordCardId = position + 1
                    }
                    1 -> {
                        wordCardId = position + 101
                    }
                    2 -> {
                        wordCardId = position + 201
                    }
                }
<<<<<<< HEAD
                LessonMaterialManager.setup(context)
                var testCardData = CardWordDataImpl.fetchWordCardContents(startPosition,context)
=======
                var testCardData = CardWordDataImpl.fetchWordCardContents(wordCardId,context)
>>>>>>> abb8fbc05eaaa84a422323814b3424bd8ea43360


                //word_listに関するに関するUIを、表示する
                englishWord = res.findViewById(R.id.word_en_text)
                japaneseWord = res.findViewById(R.id.word_jp_text)
                englishSentence = res.findViewById(R.id.sentence_en_text)
                japaneseSentence = res.findViewById(R.id.sentence_jp_text)

                //test_listに関するUIを、非表示にする
                testNumberLabel = res.findViewById(R.id.test_number_label)
                startTestButton = res.findViewById(R.id.start_test)
                testNumberLabel.visibility = View.INVISIBLE
                startTestButton.visibility = View.INVISIBLE


                //testCardDataから、データを取得し、ウィジェットに挿入
                englishWord!!.text = testCardData.worden
                englishSentence!!.text = testCardData.exampleen

                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTime = res.findViewById(R.id.average_answer_time)
                fastestAnswerTime = res.findViewById(R.id.fastest_answer_time)

                showJpButton = res.findViewById(R.id.show_word_jp_button)
//                showJpButton.setOnCheckedChangeListener { compoundButton, isClicked ->
//
//                    //選択したviewを取得するメソッドが必要。
//                    japaneseWord.text = CardWordDataImpl.showOrHiddenJapaneseWord(testCardData,isClicked)
//                }
                showJpButton.setOnClickListener {
                    japaneseSentence.text = testCardData.wordjp
                }

                pronounceButton = res.findViewById(R.id.pronounce_word_button)
                pronounceButton.setOnClickListener{
                    CardWordDataImpl.pronounceWord(context, wordCardId)
                }
                return res
            }
        }

        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)
        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))

        return view
    }
}