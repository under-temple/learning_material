package com.example.shokiterashita.learningmaterial.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word
import com.ramotion.expandingcollection.ECCardData
import com.ramotion.expandingcollection.ECPagerCard
import com.ramotion.expandingcollection.ECPagerView
import com.ramotion.expandingcollection.ECPagerViewAdapter
import com.ramotion.expandingcollection.examples.simple.CardWordDataImpl

/**
 * Created by shokiterashita on 2017/09/20.
 */
class AllWordListFragment: Fragment(){

    private var fastestAnswerTimeTextView: TextView? = null
    private var averageAnswerTimeTextView: TextView? = null
    private val ecPagerCardArr = arrayOfNulls<ECPagerCard>(100)
    private var wordCardArr: MutableList<TOEICFlash600Word>? = null
    private var ecPagerCardCount:Int = 100
    private var dataset: MutableList<ECCardData<*>>? = null
    private var cardRange: IntRange? = null


    lateinit var instantAnswerLabel: TextView
    lateinit var instantAnswerIcon: ImageView

    var englishWord: TextView? = null
    var japaneseWord: TextView? = null
    var englishSentence: TextView? = null
    var previousCorrectCount: TextView? = null

    lateinit var showJpButton: ToggleButton
    lateinit var pronounceButton: ImageButton
    lateinit var ecPagerViewAdapter:ECPagerViewAdapter
    private var ecPagerView: ECPagerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //TODO: WordListFragmentで実装したカードの生成方法を、ここに移植する。
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.card_word_list, container, false)

        wordCardArr = CardWordDataImpl.fetchAllWordCardArr(context,1)
        ecPagerCardCount = wordCardArr?.size ?: 0

        ecPagerView = view.findViewById(R.id.ec_word_pager_element)

        cardRange = wordCardArr?.let { it.indices }
        dataset = CardWordDataImpl.generateWordCardList(cardRange!!)

        ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {

                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {

                val res = super.instantiateItem(container, position) as ECPagerCard

                ecPagerCardCount = wordCardArr?.size ?: 0

                var TOEIC600Word = wordCardArr?.let { it.get(position) }

                englishWord = res.findViewById(R.id.word_en_text)
                japaneseWord = res.findViewById(R.id.word_jp_text)
                englishSentence = res.findViewById(R.id.sentence_en_text)

                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTimeTextView = res.findViewById(R.id.average_answer_time)
                fastestAnswerTimeTextView = res.findViewById(R.id.fastest_answer_time)

                //word_listに関するに関するUIに、値を挿入する。
                englishWord!!.text = TOEIC600Word?.let { it.worden }
                englishSentence!!.text = TOEIC600Word?.let { it.exampleen }

                showJpButton = res.findViewById(R.id.show_word_jp_button)
                showJpButton.setOnCheckedChangeListener { showJpButton, isClicked ->
                    ecPagerCardArr[position]!!.findViewById<TextView>(R.id.word_jp_text).text = CardWordDataImpl.showOrHiddenJapaneseWord(TOEIC600Word!!,isClicked)
                    ecPagerCardArr[position]!!.findViewById<TextView>(R.id.sentence_jp_text).text = CardWordDataImpl.showOrHiddenJapaneseSentense(TOEIC600Word!!,isClicked)
                }

                var fastestAnswerTimeSeconds = TOEIC600Word!!.fastestAnsewrTimeSeconds
                if (0 < fastestAnswerTimeSeconds && fastestAnswerTimeSeconds <= 1.50){
                    instantAnswerLabel.text = "瞬間回答"
                    instantAnswerIcon.setImageResource(R.drawable.master_small)
                }

                previousCorrectCount!!.text = TOEIC600Word!!.correctAnswerCount.toString()
                averageAnswerTimeTextView!!.text = TOEIC600Word!!.averageAnsewrTimeSeconds.toString()
                fastestAnswerTimeTextView!!.text = TOEIC600Word!!.fastestAnsewrTimeSeconds.toString()

                pronounceButton = res.findViewById(R.id.pronounce_word_button)
                pronounceButton.setOnClickListener{
                    var wordId = TOEIC600Word?.let { it.id } ?: 0
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
