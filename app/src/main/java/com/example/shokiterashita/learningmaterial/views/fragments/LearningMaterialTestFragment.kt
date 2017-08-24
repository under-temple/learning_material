package com.example.shokiterashita.learningmaterial.views.fragments

import android.content.Context
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
import com.example.shokiterashita.learningmaterial.views.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.views.lib.manager.TOEICFlash600Word


import android.util.Log


import rx.subscriptions.CompositeSubscription
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.Subscription
import rx.functions.Func1
import android.widget.Toast
import com.jakewharton.rxbinding.view.RxView
import rx.functions.Action1
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS


class LearningMaterialTestFragment : Fragment() {

    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var overallTestNumberTextView: TextView
    lateinit var testWordTextView: TextView

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button
    lateinit var initTestContent : TOEICFlash600Word
    lateinit var answerWordJp: CharSequence
    private var subscriptions: CompositeSubscription? = null
    private var mSubscription: Subscription? = null

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

        object : CountDownTimer(3000, 1000) {
            internal var countDownTimer = view.findViewById<TextView>(R.id.count_down_timer)

            override fun onTick(millisUntilFinished: Long) {
                countDownTimer.text = (millisUntilFinished / 1000).toString()
                clearField(view)
            }
            override fun onFinish() {
                showTest(context,view,testId)
            }
        }.start()

        return view
    }

    private fun showTest(context: Context, view: View, testId: Int){

        LessonMaterialManager.setup(context)

        //TestListFragmentから、testIdを、受け取る。
        initTestContent = LessonMaterialManager.fetchTestListStartAndTotalCount(testId)

        answerWordJp = initTestContent.wordjp.toString()

        testTitleTextView = view.findViewById(R.id.material_test_title)!!
        currentTestNumberTextView = view.findViewById(R.id.currentTestNumber)
        overallTestNumberTextView = view.findViewById(R.id.overallTestNumber)
        testWordTextView = view.findViewById(R.id.testWord)

        choiceAButton = view.findViewById(R.id.choice_a)
        choiceBButton = view.findViewById(R.id.choice_b)
        choiceCButton = view.findViewById(R.id.choice_c)

        testWordTextView.text = initTestContent.worden
        choiceAButton.text = initTestContent.wordjp
        choiceBButton.text = initTestContent.option_1
        choiceCButton.text = initTestContent.option_2

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
    }

    private fun clearField(view: View){
        testWordTextView.text = ""
        choiceAButton.text = ""
        choiceBButton.text = ""
        choiceCButton.text = ""
        view.setBackgroundColor(R.color.blur)

    }

    private fun checkAnswer(answerText: CharSequence){
        if (answerText == answerWordJp){
            correct()
        } else {
            inCorrect()
        }
    }

    private fun correct(){
        Log.d("答えは","正解です")
        showNextTest(LessonMaterialManager.nextQuestion())

    }

    private fun inCorrect(){
        Log.d("答えは","不正解です")
        showNextTest(LessonMaterialManager.nextQuestion())
    }

    private fun showNextTest(testContent:TOEICFlash600Word){

        testWordTextView.text = testContent.worden
        choiceAButton.text = testContent.wordjp
        choiceBButton.text = testContent.option_1
        choiceCButton.text = testContent.option_2

        timerObservable.unsubscribe()
        timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe{

            //5秒間何もなかったので、不正解になります。
            inCorrect()
        }
    }

}

