package com.example.shokiterashita.learningmaterial.views.fragments.word

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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
import com.ex.expandingcollection.examples.simple.WordListViewModel
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.github.mikephil.charting.charts.PieChart

/**
 * Created by shokiterashita on 2017/09/20.
 */

class LearningWordListFragment: Fragment(){

    lateinit var fastestAnswerTimeTextView: TextView
    lateinit var averageAnswerTimeTextView: TextView
    private val ecPagerCardArr = arrayOfNulls<ECPagerCard>(111)
    private var wordCardArr: MutableList<TOEICFlash600Word>? = null
    private var wordCardWithTestArr = ArrayList<TOEICFlash600Word>()

    private var dataset: MutableList<ECCardData<*>>? = null
    private var cardRange: IntRange? = null

    lateinit var instantAnswerLabel: TextView
    lateinit var instantAnswerIcon: ImageView

    lateinit var lauralImageView: ImageView
    lateinit var testFirstWordIdTextView: TextView
    lateinit var testLastWordIdTextView: TextView
    lateinit var testCardWelldoneTextView: TextView
    lateinit var testCardTakeTestButton: Button

    lateinit var englishWord: TextView
    lateinit var japaneseWord: TextView
    lateinit var englishSentence: TextView
    lateinit var previousCorrectCount: TextView

    lateinit var showJpButton: ToggleButton
    lateinit var pronounceButton: ImageButton
    lateinit var ecPagerViewAdapter:ECPagerViewAdapter

    lateinit var pieChartLinearLayout: LinearLayout

    private var ecPagerView: ECPagerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.card_word_list, container, false)

        wordCardArr = WordListViewModel.fetchLearningWordCardArr(context,1)

        // TODO: 前画面からtestIdを受け取る。
        var tmp = 1
        var tmpRange = when (tmp){
            0 -> 1..100
            1 -> 101..200
            else -> 201..300
        }
        for (i in tmpRange){

            val data = wordCardArr?.filter { it.id == i }?.firstOrNull()
            data?.let { wordCardWithTestArr.add(it) }

            if (i % 10 == 0){

                val testCard = TOEICFlash600Word()
                testCard.isTestData = true
                testCard.testFirstWordId = i - 9
                testCard.testLastWordId = i
                wordCardWithTestArr.add(testCard)
            }

            if(i % 100 == 0){

                val testCard = TOEICFlash600Word()
                testCard.isTestData = true
                testCard.testFirstWordId = i - 99
                testCard.testLastWordId = i
                wordCardWithTestArr.add(testCard)
            }
        }
        ecPagerView = view.findViewById(R.id.all_word_pager_element)
        cardRange = wordCardWithTestArr.indices
        dataset = WordListViewModel.generateWordCardList(cardRange!!)
        ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {

            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val res = super.instantiateItem(container, position) as ECPagerCard
                var TOEIC600Word = wordCardWithTestArr[position]

                // TODO: TOEIC.isTakeTestではなくて？？
                if (TOEIC600Word.isTestData){

                    //テストを受けに行くカードが表示される。
                    var TOEIC600Test = WordListViewModel.fetchTestCardArr(context,TOEIC600Word.testFirstWordId!!, TOEIC600Word.testLastWordId!! )

                    lauralImageView = res.findViewById(R.id.testcard_laurel_image_view)
                    testFirstWordIdTextView = res.findViewById(R.id.test_first_word_id_text_view)
                    testLastWordIdTextView = res.findViewById(R.id.test_last_word_id_text_view)
                    testCardWelldoneTextView = res.findViewById(R.id.testcard_welldone_text_view)
                    testCardTakeTestButton = res.findViewById(R.id.testcard_take_test_button)

                    pieChartLinearLayout = res.findViewById(R.id.pie_chart_linear_layout)
                    pieChartLinearLayout.visibility = View.GONE

                    lauralImageView.setImageResource(R.drawable.ast_laurel)
                    testFirstWordIdTextView.text = TOEIC600Word.testFirstWordId.toString()
                    testLastWordIdTextView.text = TOEIC600Word.testLastWordId.toString()

                    var testDataLinearLayout = res.findViewById<LinearLayout>(R.id.test_data_linear_layout)
                    var underlineTextView = res.findViewById<TextView>(R.id.underline_text_view)
                    japaneseWord = res.findViewById(R.id.word_jp_text)
                    var testNumberLabelLineaLayout = res.findViewById<LinearLayout>(R.id.test_number_label_linear_layout)
                    var startTestButton = res.findViewById<Button>(R.id.start_test_button)
                    var problemProcedureLinearLayout = res.findViewById<LinearLayout>(R.id.problem_procedure_linear_layout)


                    problemProcedureLinearLayout.visibility = View.INVISIBLE
                    testNumberLabelLineaLayout.visibility = View.INVISIBLE
                    startTestButton.visibility = View.INVISIBLE
                    testDataLinearLayout.visibility = View.INVISIBLE
                    underlineTextView.visibility = View.INVISIBLE
                    japaneseWord.visibility = View.INVISIBLE

                    testCardTakeTestButton.setOnClickListener {
                        val learningMaterial = LearningMaterialTestFragment()
                        var transaction = fragmentManager.beginTransaction()
                        var args = Bundle()
                        args.putInt("testId", TOEIC600Test.id!!)
                        args.putBoolean("isNormalOrder", false)
                        learningMaterial.arguments = args
                        transaction.replace(R.id.word_list,learningMaterial)
                        transaction.commit()
                    }

                } else {

                    //TODO: R.id.next_buttonを実装する。タップ時に、次のカードへ遷移するシステムを組む。
                    englishWord = res.findViewById(R.id.word_en_text)
                    japaneseWord = res.findViewById(R.id.word_jp_text)
                    englishSentence = res.findViewById(R.id.sentence_en_text)
                    previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                    averageAnswerTimeTextView = res.findViewById(R.id.average_answer_time)
                    fastestAnswerTimeTextView = res.findViewById(R.id.fastest_answer_time)
                    showJpButton = res.findViewById(R.id.show_word_jp_button)
                    pronounceButton = res.findViewById(R.id.pronounce_word_button)

                    pieChartLinearLayout = res.findViewById(R.id.pie_chart_linear_layout)
                    pieChartLinearLayout.visibility = View.GONE

                    //テストを受けに行くカードをInvisibleにする。
                    var takeTestLinearLayout = res.findViewById<LinearLayout>(R.id.take_test_linear_layout)
                    var takeTestIconLinearLayout = res.findViewById<LinearLayout>(R.id.take_test_icon_linear_layout)
                    var testNumberLabelLineaLayout = res.findViewById<LinearLayout>(R.id.test_number_label_linear_layout)
                    var startTestButton = res.findViewById<Button>(R.id.start_test_button)
                    var problemProcedureLinearLayout = res.findViewById<LinearLayout>(R.id.problem_procedure_linear_layout)

                    problemProcedureLinearLayout.visibility = View.INVISIBLE
                    testNumberLabelLineaLayout.visibility = View.INVISIBLE
                    startTestButton.visibility = View.INVISIBLE
                    takeTestLinearLayout.visibility = View.INVISIBLE
                    takeTestIconLinearLayout.visibility = View.INVISIBLE

                    englishWord.text = TOEIC600Word.worden
                    englishSentence.text = TOEIC600Word.exampleen
                    showJpButton.setOnCheckedChangeListener { showJpButton, isClicked ->
                        ecPagerCardArr[position]!!.findViewById<TextView>(R.id.word_jp_text).text = WordListViewModel.showOrHiddenJapaneseWord(TOEIC600Word, isClicked)
                        ecPagerCardArr[position]!!.findViewById<TextView>(R.id.sentence_jp_text).text = WordListViewModel.showOrHiddenJapaneseSentense(TOEIC600Word, isClicked)
                    }

                    if (TOEIC600Word.isCorrect == null) {

                        //未回答の場合
                        previousCorrectCount.text = TOEIC600Word.correctAnswerCount.toString()
                        fastestAnswerTimeTextView.text = "未回答"
                        averageAnswerTimeTextView.text = "未回答"

                    } else {

                        //回答している場合
                        //前回は、1.5秒以上の回答タイムであったが、過去の最速タイムが1.5秒以下のとき、瞬間回答と表示されてしまう。
                        if (TOEIC600Word.isCorrect!!){

                            if (TOEIC600Word.answerTimeSeconds!! <= 1.50) {

                                //瞬間回答の場合
                                //そもそも、ここの行はまず呼ばれない。呼ばれたらミスである。
                                instantAnswerLabel = res.findViewById(R.id.instant_answer_label)
                                instantAnswerIcon = res.findViewById(R.id.instant_answer_icon)
                                instantAnswerLabel.text = "瞬間回答"
                                instantAnswerIcon.setImageResource(R.drawable.master_small)
                                previousCorrectCount.text = TOEIC600Word.correctAnswerCount.toString()
                                fastestAnswerTimeTextView.text = "%.2f".format(TOEIC600Word.fastestAnswerTimeSeconds)
                                averageAnswerTimeTextView.text = "%.2f".format(TOEIC600Word.averageAnswerTimeSeconds)
                            } else {

                                //瞬間回答ではない場合
                                previousCorrectCount.text = TOEIC600Word.correctAnswerCount.toString()
                                fastestAnswerTimeTextView.text = "%.2f".format(TOEIC600Word.fastestAnswerTimeSeconds)
                                averageAnswerTimeTextView.text = "%.2f".format(TOEIC600Word.averageAnswerTimeSeconds)
                            }
                        } else {
                            //不正解の場合
                            previousCorrectCount.text = TOEIC600Word.correctAnswerCount.toString()
                            fastestAnswerTimeTextView.text = if (TOEIC600Word.fastestAnswerTimeSeconds != null) "%.2f".format(TOEIC600Word.fastestAnswerTimeSeconds) else "不正解"
                            averageAnswerTimeTextView.text = if (TOEIC600Word.averageAnswerTimeSeconds != null) "%.2f".format(TOEIC600Word.averageAnswerTimeSeconds) else "不正解"
                        }
                    }
                    pronounceButton = res.findViewById(R.id.pronounce_word_button)
                    pronounceButton.setOnClickListener{
                        WordListViewModel.pronounceWord(context, TOEIC600Word.id!!)
                    }
                    ecPagerCardArr[position] = res
                }
                return res
            }
        }

        ecPagerView!!.setPagerViewAdapter(ecPagerViewAdapter)
        ecPagerView!!.setBackgroundSwitcherView(view.findViewById(R.id.ec_bg_switcher_element))
        return view
    }
}