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
import com.jakewharton.rxbinding.view.RxView
import io.realm.Realm
import rx.functions.Action1
import java.security.PrivateKey
import com.example.shokiterashita.learningmaterial.viewmodel.TestListViewModel
import com.example.shokiterashita.learningmaterial.views.activities.MainActivity
import com.example.shokiterashita.learningmaterial.views.fragments.test.TestListFragment
import com.example.shokiterashita.learningmaterial.views.fragments.word.WordListFragment
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

class LearningMaterialTestFragment : Fragment() {

    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var testSizeTextView: TextView
    lateinit var testWordTextView: TextView
    lateinit var materialTestSizeTextView: TextView

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button
    lateinit var answerWordJp: String
    private var subscriptions: CompositeSubscription? = null
    private var mSubscription: Subscription? = null

    private var beginMeasureTimeMillis: Long = 0
    private var endMeasureTimeMillis: Long = 0
    private var answerTimeSeconds: Double = 0.0
    private var wordId: Int = 0
    private var testId: Int = 0

    private var TOEIC600WordArray = ArrayList<TOEICFlash600Word>()
    private var isNormalOrder = true
    var BEGIN = 0
    var currentTestNumbers = 1


    private var correctCount: Int = 0
    private var averageTime: Double = 0.0
    private var quickTime: Double = 0.0
    var timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testId = arguments.getInt("testId")
        TOEIC600WordArray = LessonMaterialManager.generateTestWordArray(context,testId)
        wordId = LessonMaterialManager.convertTestIDtoWordID(testId)
        subscriptions = CompositeSubscription()
    }

    override fun onDestroy() {

        super.onDestroy()
        mSubscription?.unsubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_learning_material_test, container, false)
        var TOEIC600Test = LessonMaterialManager.fetchTestCard(context,testId)
        // if (TOEIC600Test.totalCount != null || TOEIC600Test.idx_start != null) { }
        var testSize = (TOEIC600Test.totalCount!! - TOEIC600Test.idx_start!! + 1).toString()

        materialTestSizeTextView = view.findViewById(R.id.material_test_size)
        testSizeTextView = view.findViewById(R.id.testSizeTextView)
        materialTestSizeTextView.text = testSize
        testSizeTextView.text = testSize

        if (isNormalOrder){
            //ここで、通常出題順
        }

        view.setBackgroundColor(Color.WHITE)
        clearField(view)

        object : CountDownTimer(4000, 1000) {
            internal var countDownTimer = view.findViewById<TextView>(R.id.count_down_timer)

            override fun onTick(millisUntilFinished: Long) {
                countDownTimer.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                showTest(view)
                countDownTimer.visibility = View.INVISIBLE
            }
        }.start()

        return view
    }

    private fun clearField(view: View){
        testTitleTextView = view.findViewById(R.id.material_test_title)
        currentTestNumberTextView = view.findViewById(R.id.currentTestNumberTextView)
        testSizeTextView = view.findViewById(R.id.testSizeTextView)
        testWordTextView = view.findViewById(R.id.testWord)

        choiceAButton = view.findViewById(R.id.choice_a)
        choiceBButton = view.findViewById(R.id.choice_b)
        choiceCButton = view.findViewById(R.id.choice_c)

        testWordTextView.text = ""
        choiceAButton.text = ""
        choiceBButton.text = ""
        choiceCButton.text = ""
    }

    //問題が始まるさいに、呼ばれるメソッド
    private fun showTest(view: View){

        //currentTestNumber= 1 を表示する -> 加算する & 表示する。
        var TOEIC600Words = TOEIC600WordArray[BEGIN]

        currentTestNumberTextView.text = currentTestNumbers.toString()
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

        //10問目答えた時に、評価するコードにしなくてはいけない。
        view.setBackgroundColor(Color.WHITE)
        beginMeasureTimeMillis = System.currentTimeMillis()
    }

    private fun checkAnswer(choicedButtonText: CharSequence){

        if (answerWordJp == choicedButtonText){
            endMeasureTimeMillis = System.currentTimeMillis()
            answerTimeSeconds = (endMeasureTimeMillis - beginMeasureTimeMillis)/1000.0

            //updateCorrectAnswerDataメソッドは、wordDataを更新する。
            LessonMaterialManager.updateCorrectAnswerData(context,wordId,answerTimeSeconds)
            updateTestScore(answerTimeSeconds)
            correct()

        } else {
            LessonMaterialManager.updateInCorrectAnswerData(context,wordId)
            inCorrect()
        }

        BEGIN++
        currentTestNumbers++
    }

    private fun correct(){

        Log.d("答えは","正解です")
        var END = TOEIC600WordArray.size

        if (BEGIN == END) {
            var home = TestListFragment()
            LessonMaterialManager.updateTestData(context, testId, correctCount, quickTime, averageTime)
            val fragmentManager = fragmentManager.beginTransaction()
            fragmentManager.add(R.id.test_list,home)
            fragmentManager.commit()
        } else {
            showTest(view!!)
        }
    }

    private fun inCorrect(){
        Log.d("答えは","不正解です")
        beginMeasureTimeMillis = 0

        var END = TOEIC600WordArray.size
        if (BEGIN == END) {
            var home = TestListFragment()
            LessonMaterialManager.updateTestData(context, testId, correctCount, quickTime, averageTime)
            val fragmentManager = fragmentManager.beginTransaction()
            fragmentManager.add(R.id.test_list,home)
            fragmentManager.commit()
        } else {
            showTest(view!!)
        }
    }

    private fun showNextTest(TOEICFlash600Word:TOEICFlash600Word) {

        //なぜ、checkAnswerより先に、これが呼ばれるのだろうか。
        beginMeasureTimeMillis = System.currentTimeMillis()
        answerWordJp = TOEICFlash600Word.wordjp!!
        testWordTextView.text = TOEICFlash600Word.worden
        choiceAButton.text = TOEICFlash600Word.wordjp
        choiceBButton.text = TOEICFlash600Word.option_1
        choiceCButton.text = TOEICFlash600Word.option_2

        timerObservable.unsubscribe()
        timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {

            //ここで、inCorrectメソッドを呼ぶと、_testIdの値が加算されないまま、次の問題へ移ってします。
            inCorrect()
        }
    }

    private fun updateTestScore(answerTimeSeconds: Double){

        correctCount += 1
        if (averageTime == 0.0){
            averageTime = answerTimeSeconds
        } else {
            averageTime = (averageTime * (correctCount - 1) + answerTimeSeconds) / correctCount
        }
        if (quickTime == 0.0){
            quickTime = answerTimeSeconds
        } else {
            if (quickTime > answerTimeSeconds){
                quickTime = answerTimeSeconds
            }
        }
    }
}