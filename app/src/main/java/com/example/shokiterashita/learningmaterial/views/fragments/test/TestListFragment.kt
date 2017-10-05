package com.example.shokiterashita.learningmaterial.views.fragments.test

import android.support.v4.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.example.shokiterashita.learningmaterial.R
//import com.example.shokiterashita.learningmaterial.viewmodel.CardDataImpl

import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.*
import com.example.shokiterashita.learningmaterial.viewmodel.TestListViewModel
import com.example.shokiterashita.learningmaterial.views.fragments.CardListItemAdapter
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.github.mikephil.charting.charts.PieChart
import com.ramotion.expandingcollection.*

class TestListFragment: Fragment() {

    private var ecPagerView: ECPagerView? = null

    lateinit var testNumber: TextView
    lateinit var previousCorrectCount: TextView
    lateinit var fastestAnswerTime: TextView
    lateinit var averageAnswerTime: TextView
    lateinit var startTestButton: Button
    lateinit var testStatusChart: PieChart

    var englishWord: TextView? = null
    var japaneseWord: TextView? = null
    var japaneseSentence: TextView? = null
    var englishSentence: TextView? = null
    var pronounceButton: ImageButton? = null
    lateinit var wordListFrame: LinearLayout

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

                //testListId = 0 or 1 or 2 ,DEMO: in this case, testListId = 1 (show testList 101-200)
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

                previousCorrectCount.text = testCardData.result.toString()
//                averageAnswerTime!!.text = (testCardData.result!!.toInt() / testCardData.time!!.toInt()).toString()
                fastestAnswerTime.text = testCardData.quicktime.toString()
                startTestButton = res.findViewById(R.id.start_test_button)

                //WordListのレイアウトを、インスタンス化する。
                var takeTestLinearLayout = res.findViewById<LinearLayout>(R.id.take_test_linear_layout)
                var takeTestIconLinearLayout = res.findViewById<LinearLayout>(R.id.take_test_icon_linear_layout)
                var wordListButtonsLinearLayout = res.findViewById<LinearLayout>(R.id.word_list_buttons_linear_layout)
                takeTestLinearLayout.visibility = View.INVISIBLE
                takeTestIconLinearLayout.visibility = View.INVISIBLE
                wordListButtonsLinearLayout.visibility = View.INVISIBLE


                englishWord = res.findViewById(R.id.word_en_text)
                japaneseWord = res.findViewById(R.id.word_jp_text)
                englishSentence = res.findViewById(R.id.sentence_en_text)
                japaneseSentence = res.findViewById(R.id.sentence_jp_text)
                pronounceButton = res.findViewById(R.id.pronounce_word_button)
                wordListFrame = res.findViewById(R.id.word_list_frame)

                //WordListのViewを非表示にする。
                englishWord?.visibility = View.INVISIBLE
                japaneseWord?.visibility = View.INVISIBLE
                englishSentence?.visibility = View.INVISIBLE
                japaneseSentence?.visibility = View.INVISIBLE
                pronounceButton?.visibility = View.INVISIBLE
                wordListFrame.visibility = View.INVISIBLE

                var testListDataIdxStart = testCardData.idx_start.toString()
                var testListDataTotalCount = testCardData.totalCount.toString()
                testNumber.text = testListDataIdxStart + "-" + testListDataTotalCount


                //PieChartのインスタンスをとる。

                //TODO: テストの成績を取得する
                //取得方法：単語No.101-110 ... 101-110　//前回正解数
                //test_listモデルのカラムにあるresultを更新する。 //最速正答時間
                //test_listモデルのカラムにあるtime, quicktimeを更新する。 //平均回答時間
                //結局は、モデルから取得することになる。-> テスト画面から取得する必要あり。
                //ここで、ランダムリストを作成しなければいけない。

                testStatusChart = res.findViewById(R.id.test_status_chart)


                //テストページで表示する。
                startTestButton.setOnClickListener {
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