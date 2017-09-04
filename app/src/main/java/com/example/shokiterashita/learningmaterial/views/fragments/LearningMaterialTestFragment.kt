package com.example.shokiterashita.learningmaterial.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.timer

import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word


import android.util.Log


import rx.subscriptions.CompositeSubscription
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.Subscription
import rx.functions.Func1
import android.widget.Toast
import com.example.shokiterashita.learningmaterial.model.TestScore
import com.jakewharton.rxbinding.view.RxView
import com.example.shokiterashita.learningmaterial.viewModel.TestListViewModel
import rx.functions.Action1
import java.security.PrivateKey
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS

//バグ：事実：テスト画面の始めのカードビューにて、ファーストタッチを反応しない。仮説：同じ問題が二度表示されている？
class LearningMaterialTestFragment : Fragment() {

    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var overallTestNumberTextView: TextView
    lateinit var testWordTextView: TextView

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button
    lateinit var answerWordJp: String
    private var subscriptions: CompositeSubscription? = null
    private var mSubscription: Subscription? = null

    private var beginMeasureTimeMillis: Long = 0
    private var endMeasureTimeMillis: Long = 0
    private var answerTimeMillis: Long = 0
    private var wordId: Int = 0
    var prefs: SharedPreferences? = null
    var timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscriptions = CompositeSubscription()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.unsubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_learning_material_test, container, false)
        var testId = arguments.getInt("testId")
        prefs = context.getSharedPreferences("TEST_SCORE", Context.MODE_PRIVATE)
        wordId = LessonMaterialManager.convertTestIDtoWordID(testId)
        view.setBackgroundColor(Color.WHITE)
        clearField(view)

        object : CountDownTimer(4000, 1000) {
            internal var countDownTimer = view.findViewById<TextView>(R.id.count_down_timer)

            override fun onTick(millisUntilFinished: Long) {
                countDownTimer.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                showTest(context,view)
                countDownTimer.visibility = View.INVISIBLE
            }
        }.start()
        return view
    }

    private fun clearField(view: View){
        testTitleTextView = view.findViewById(R.id.material_test_title)
        currentTestNumberTextView = view.findViewById(R.id.currentTestNumber)
        overallTestNumberTextView = view.findViewById(R.id.overallTestNumber)
        testWordTextView = view.findViewById(R.id.testWord)

        choiceAButton = view.findViewById(R.id.choice_a)
        choiceBButton = view.findViewById(R.id.choice_b)
        choiceCButton = view.findViewById(R.id.choice_c)

        testWordTextView.text = ""
        choiceAButton.text = ""
        choiceBButton.text = ""
        choiceCButton.text = ""
    }

    private fun showTest(context: Context, view: View){
        LessonMaterialManager.setup(context)
        var TOEIC600Words = LessonMaterialManager.fetchWordList(wordId)

        answerWordJp = TOEIC600Words.wordjp.toString()
        testWordTextView.text = TOEIC600Words.worden
        choiceAButton.text = TOEIC600Words.wordjp
        choiceBButton.text = TOEIC600Words.option_1
        choiceCButton.text = TOEIC600Words.option_2

        choiceAButton.setOnClickListener {
            checkAnswer(choiceAButton.text)
        }
        choiceBButton.setOnClickListener {
            checkAnswer(choiceBButton.text)
        }
        choiceCButton.setOnClickListener {
            checkAnswer(choiceCButton.text)
        }
        view.setBackgroundColor(Color.WHITE)
        beginMeasureTimeMillis = System.currentTimeMillis()
    }

    private fun checkAnswer(choicedButtonText: CharSequence){

        if (answerWordJp == choicedButtonText){
            endMeasureTimeMillis = System.currentTimeMillis()
            answerTimeMillis = endMeasureTimeMillis - beginMeasureTimeMillis

            TestScore.updateTestScore(wordId,answerTimeMillis,context)

            correct()
        } else {
            inCorrect()
        }
    }

    private fun correct(){
        Log.d("答えは","正解です")
        showNextTest(LessonMaterialManager.fetchWordList(wordId++))

        //獲得成功。
        Log.d("前回までの正解数","${TestScore.getCorrectAnswerCount(wordId,context)}")
    }

    private fun inCorrect(){
        Log.d("答えは","不正解です")

        //計測時間をリセットする。checkAnswer()に書かない理由は、タイムアウトによる不正解も考慮するため。
        beginMeasureTimeMillis = 0
        showNextTest(LessonMaterialManager.fetchWordList(wordId++))
    }

    //showNextTestメソッドには、testId+1 を引数に持ったTOEICFlash600wordを渡す。
    private fun showNextTest(TOEICFlash600Word:TOEICFlash600Word){

        //なぜ、checkAnswerより先に、これが呼ばれるのだろうか。
        beginMeasureTimeMillis = System.currentTimeMillis()
        answerWordJp = TOEICFlash600Word.wordjp!!
        testWordTextView.text = TOEICFlash600Word.worden
        choiceAButton.text = TOEICFlash600Word.wordjp
        choiceBButton.text = TOEICFlash600Word.option_1
        choiceCButton.text = TOEICFlash600Word.option_2

        timerObservable.unsubscribe()
        timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe{

            //ここで、inCorrectメソッドを呼ぶと、_testIdの値が加算されないまま、次の問題へ移ってします。
            inCorrect()
        }
    }

}

