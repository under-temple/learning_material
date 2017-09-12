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
import java.util.List
//import com.beardedhen.androidbootstrap.TypefaceProvider
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.lib.extention.WordListCard
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Test
import com.example.shokiterashita.learningmaterial.model.TestScore
import com.ramotion.expandingcollection.*
import com.ramotion.expandingcollection.examples.simple.CardWordDataImpl

import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word

/**
 * Created by shokiterashita on 2017/08/17.
 */

class WordListFragment: Fragment() {

    //ECPagerCardのサブクラスのインスタンス
    private var ecPagerView: ECPagerView? = null

    private var fastestAnswerTimeTextView: TextView? = null
    private var averageAnswerTimeTextView: TextView? = null
    private val ecPagerCardArr = arrayOfNulls<ECPagerCard>(100)

    lateinit var instantAnswerLabel: TextView

    var englishWord: TextView? = null
    var japaneseWord: TextView? = null
    var englishSentence: TextView? = null
    var previousCorrectCount: TextView? = null
    var testNumberLabel: TextView? = null

    lateinit var startTestButton: Button
    lateinit var showJpButton: ToggleButton
    lateinit var pronounceButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_word_list, container, false)

        //後に、Bundleクラスから、値を取得する。
        ecPagerView = view.findViewById(R.id.ec_word_pager_element)

        //DEMO: 単語テスト一覧：101-200を選択したと想定
        val dataset = CardWordDataImpl.generateWordCardList(wordListPosition = 0)


        val ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {

                var cardData = data as CardWordDataImpl
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
                val realm = LessonMaterialManager.getLessonMaterial()


                //DEMO:単語一覧101-200を選択したとする。後に、Bundleクラスから、getInt()メソッドを呼ぶ。
                val testListPosition = 1
                var wordId = 0
                when (testListPosition) {
                    0 -> {
                        wordId = position + 1
                    }
                    1 -> {
                        wordId = position + 101
                    }
                    2 -> {
                        wordId = position + 201
                    }
                }


                LessonMaterialManager.setup(context)
                var testCardData = CardWordDataImpl.fetchWordCardContents(wordId,context)
                var fastestAnswerTimeSeconds = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst().fastestAnsewrTimeSeconds


                englishWord = res.findViewById(R.id.word_en_text)
                japaneseWord = res.findViewById(R.id.word_jp_text)
                englishSentence = res.findViewById(R.id.sentence_en_text)

                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTimeTextView = res.findViewById(R.id.average_answer_time)
                fastestAnswerTimeTextView = res.findViewById(R.id.fastest_answer_time)

                    //test_listに関するUIを、非表示にする
                testNumberLabel = res.findViewById(R.id.test_number_label)
                startTestButton = res.findViewById(R.id.start_test)
                testNumberLabel!!.visibility = View.INVISIBLE
                startTestButton.visibility = View.INVISIBLE

                    //word_listに関するに関するUIに、値を挿入する。
                englishWord!!.text = testCardData.worden
                englishSentence!!.text = testCardData.exampleen



                showJpButton = res.findViewById(R.id.show_word_jp_button)
                showJpButton.setOnCheckedChangeListener { showJpButton, isClicked ->
                    ecPagerCardArr[position]!!.findViewById<TextView>(R.id.word_jp_text).text = CardWordDataImpl.showOrHiddenJapaneseWord(testCardData,isClicked)
                    ecPagerCardArr[position]!!.findViewById<TextView>(R.id.sentence_jp_text).text = CardWordDataImpl.showOrHiddenJapaneseSentense(testCardData,isClicked)
                }

                //fastestAnswerTimeSecondsを、optional bindingすべし。
                if (0 < fastestAnswerTimeSeconds!! && fastestAnswerTimeSeconds!! <= 1.50){

                    instantAnswerLabel = res.findViewById(R.id.instant_answer_label)
                    instantAnswerLabel.text = "瞬間回答"
                }

                //TODO: Realm から、ANSWER_DATA を取得する。
                previousCorrectCount!!.text = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst().correctAnswerCount.toString()
                averageAnswerTimeTextView!!.text = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst().averageAnsewrTimeSeconds.toString()
                fastestAnswerTimeTextView!!.text = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst().fastestAnsewrTimeSeconds.toString()

                pronounceButton = res.findViewById(R.id.pronounce_word_button)
                pronounceButton.setOnClickListener{
                    CardWordDataImpl.pronounceWord(context, wordId)
                }
                ecPagerCardArr[position] = res

                return res
            }
        }

        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)
        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))


        return view
    }
}