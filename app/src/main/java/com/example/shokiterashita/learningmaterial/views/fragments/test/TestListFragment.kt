package com.example.shokiterashita.learningmaterial.views.fragments.test

import android.support.v4.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.animation.Easing

import com.example.shokiterashita.learningmaterial.R
//import com.example.shokiterashita.learningmaterial.viewmodel.CardDataImpl

import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.*
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Test
import com.example.shokiterashita.learningmaterial.viewmodel.TestListViewModel
import com.example.shokiterashita.learningmaterial.views.fragments.CardListItemAdapter
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.ramotion.expandingcollection.*

class TestListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null

    lateinit var testNumber: TextView
    lateinit var previousCorrectCount: TextView
    lateinit var fastestAnswerTime: TextView
    lateinit var averageAnswerTime: TextView
    lateinit var startTestButton: Button
    lateinit var testStatusChart: PieChart
    lateinit var normalOrderButton: RadioButton
    lateinit var randomOrderButton: RadioButton

    var englishWord: TextView? = null
    var japaneseWord: TextView? = null
    var japaneseSentence: TextView? = null
    var englishSentence: TextView? = null
    var pronounceButton: ImageButton? = null
    lateinit var wordListFrame: LinearLayout
    private var TEST_COUNT = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_test_list, container, false)
        ecPagerView = view.findViewById(R.id.ec_pager_element)

        //DEMO: 単語テスト一覧：101-200を選択したと想定
        val dataset = TestListViewModel.generateTestCardList(testListPosition = 1)

        val ecPagerViewAdapter = object : ECPagerViewAdapter(context, dataset) {
            override fun instantiateCard(inflaterService: LayoutInflater, head: ViewGroup, list: ListView, data: ECCardData<*>) {
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val res = super.instantiateItem(container, position) as ECPagerCard
                val learningMaterial = LearningMaterialTestFragment()
                var testListGroup = 1
                var startPosition = 0

                when (testListGroup) {
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

                val testCardData = TestListViewModel.fetchTestCardContents(startPosition,context)

                testNumber = res.findViewById(R.id.test_number_text)
                previousCorrectCount = res.findViewById(R.id.previous_correct_count)
                averageAnswerTime = res.findViewById(R.id.average_answer_time)
                fastestAnswerTime = res.findViewById(R.id.fastest_answer_time)


                //modelの中身を変えたので、nullでも値が取得できそう。
                if (testCardData.quicktime == 0 && testCardData.result == 0 && testCardData.time == 0){

                    previousCorrectCount.text = "未回答"
                    averageAnswerTime.text = "未回答"
                    fastestAnswerTime.text = "未回答"

                } else {

                    previousCorrectCount.text = testCardData.result.toString()
                    averageAnswerTime.text = String.format("%.2f", testCardData.averageTime)
                    fastestAnswerTime.text = String.format("%.2f", testCardData.fastestTime)
                }

                startTestButton = res.findViewById(R.id.start_test_button)

                normalOrderButton = res.findViewById(R.id.normal_order_button)
                randomOrderButton = res.findViewById(R.id.random_order_button)
                normalOrderButton.setOnClickListener { }
                randomOrderButton.setOnClickListener{ }



                //WordListのレイアウトを、インスタンス化する。
                var takeTestLinearLayout = res.findViewById<LinearLayout>(R.id.take_test_linear_layout)
                var takeTestIconLinearLayout = res.findViewById<LinearLayout>(R.id.take_test_icon_linear_layout)
                var wordListButtonsLinearLayout = res.findViewById<LinearLayout>(R.id.word_list_buttons_linear_layout)
                takeTestLinearLayout.visibility = View.INVISIBLE
                takeTestIconLinearLayout.visibility = View.INVISIBLE
                wordListButtonsLinearLayout.visibility = View.INVISIBLE

                //WordListのViewを非表示にする。引数に'res'を渡せば、関数でくくれそう。
                englishWord = res.findViewById(R.id.word_en_text)
                japaneseWord = res.findViewById(R.id.word_jp_text)
                englishSentence = res.findViewById(R.id.sentence_en_text)
                japaneseSentence = res.findViewById(R.id.sentence_jp_text)
                pronounceButton = res.findViewById(R.id.pronounce_word_button)
                wordListFrame = res.findViewById(R.id.word_list_frame)

                englishWord?.visibility = View.INVISIBLE
                japaneseWord?.visibility = View.INVISIBLE
                englishSentence?.visibility = View.INVISIBLE
                japaneseSentence?.visibility = View.INVISIBLE
                pronounceButton?.visibility = View.INVISIBLE
                wordListFrame.visibility = View.INVISIBLE

                var testListDataIdxStart = testCardData.idx_start.toString()
                var testListDataTotalCount = testCardData.totalCount.toString()
                testNumber.text = testListDataIdxStart + "-" + testListDataTotalCount

                testStatusChart = res.findViewById(R.id.test_status_chart)
                var correctCount = testCardData.result!!.toFloat()

                //マジックナンバーなので、変数に置き換える。
                var totalTestCount = TEST_COUNT
                var inCorrectCount = totalTestCount - correctCount
                val entries = ArrayList<PieEntry>()

                entries.add(PieEntry(correctCount))
                entries.add(PieEntry(inCorrectCount))
                var colors:IntArray = intArrayOf(R.color.appColor, R.color.colorLightGray)
                var dataSet = PieDataSet(entries, "正解数")
                dataSet.setColors(colors, context)

                dataSet.isHighlightEnabled = false

                val data = PieData(dataSet)
                data.setValueTextColor(Color.TRANSPARENT)
                testStatusChart.data = data
                testStatusChart.isDrawHoleEnabled = true
                testStatusChart.setUsePercentValues(false)
                testStatusChart.legend.isEnabled = false
                testStatusChart.description.isEnabled = false

                testStatusChart.holeRadius = 95f
                testStatusChart.setHoleColor(Color.TRANSPARENT)
                testStatusChart.transparentCircleRadius = 60f
                testStatusChart.isRotationEnabled = false
                testStatusChart.animateXY(2000, 2000)
                testStatusChart.invalidate()

                startTestButton.setOnClickListener {
                    var fragmentManager = fragmentManager.beginTransaction()
                    var args = Bundle()
                    args.putInt("testId", startPosition)
                    args.putBoolean("isNormalOrder", true)
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