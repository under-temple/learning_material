package com.example.shokiterashita.learningmaterial.views.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Interpolator
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.concurrent.timer

//import android.animation.DecelerateInterpolator

import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word


import android.util.Log
import android.view.KeyEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.example.shokiterashita.learningmaterial.CountDownListener
import com.example.shokiterashita.learningmaterial.lib.extention.animateScaleAndAlpha
import com.example.shokiterashita.learningmaterial.lib.extention.animateShake


import rx.subscriptions.CompositeSubscription
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.Subscription
import rx.functions.Func1
import com.jakewharton.rxbinding.view.RxView
import io.realm.Realm
import rx.functions.Action1
import java.security.PrivateKey
import com.example.shokiterashita.learningmaterial.viewmodel.TestListViewModel
import com.example.shokiterashita.learningmaterial.views.activities.MainActivity
import com.example.shokiterashita.learningmaterial.views.fragments.test.TestListFragment
import com.example.shokiterashita.learningmaterial.views.fragments.word.WordListFragment
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class LearningMaterialTestFragment : Fragment() {

    lateinit var blackSheetLinearLayout: View
    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var testSizeTextView: TextView
    lateinit var testWordTextView: TextView
    lateinit var materialTestSizeTextView: TextView
    lateinit var currentTestNumberLinearLayout: LinearLayout
    lateinit var materialTestLabelLinearLayout: LinearLayout

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button

    lateinit var correctImageView: ImageView
    lateinit var incorrectImageView: ImageView

    lateinit var answerTimeTextView: TextView
    lateinit var instantAnswerTextView: TextView
    lateinit var finishTextView: TextView

    lateinit var answerWordJp: String
    private var subscriptions: CompositeSubscription? = null
    private var mSubscription: Subscription? = null

    private var beginMeasureTimeMillis: Long = 0
    private var endMeasureTimeMillis: Long = 0
    private var answerTimeSeconds: Double = 0.0
    private var wordId: Int = 0
    private var testId: Int = 0
    private var TOEIC600WordArray = ArrayList<TOEICFlash600Word>()
    var isNormalOrder = true
    var isImageViewAnimated = false
    var BEGIN = 0
    var currentTestNumbers = 1

    private val blackSheet = BlackSheetFragment()


    private var correctCount: Int = 0
    private var averageTime: Double = 0.0
    private var quickTime: Double = 0.0
    var timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testId = arguments.getInt("testId")
        isNormalOrder = arguments.getBoolean("isNormalOrder")
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
        var testSize = (TOEIC600Test.totalCount!! - TOEIC600Test.idx_start!! + 1).toString()

        materialTestSizeTextView = view.findViewById(R.id.material_test_size)
        testSizeTextView = view.findViewById(R.id.testSizeTextView)
        materialTestSizeTextView.text = testSize
        testSizeTextView.text = testSize


        correctImageView = view.findViewById(R.id.correct_image)
        incorrectImageView = view.findViewById(R.id.incorrect_image)
        answerTimeTextView = view.findViewById(R.id.animated_answer_time_text_view)
        instantAnswerTextView = view.findViewById(R.id.animated_instant_answer_text_view)
        finishTextView = view.findViewById(R.id.animated_finish_text_view)
        currentTestNumberLinearLayout = view.findViewById(R.id.current_test_number_linear_layout)
        materialTestLabelLinearLayout = view.findViewById(R.id.material_test_label_linear_layout)

        correctImageView.alpha = 0.0f
        incorrectImageView.alpha = 0.0f
        answerTimeTextView.alpha = 0.0f
        instantAnswerTextView.alpha = 0.0f
        finishTextView.alpha = 0.0f

        disableBackKey(view)

        //通常順かランダム順かを指定
        if (!isNormalOrder){
            Collections.shuffle(TOEIC600WordArray)
        }
        view.setBackgroundColor(Color.WHITE)
        clearField(view)
        showBlackSheet(isStart = true)

        blackSheet.listner = object : BlackSheetFragment.CountDownListner{
            override fun onFinish() {
                materialTestLabelLinearLayout.visibility = View.VISIBLE
                currentTestNumberLinearLayout.visibility = View.VISIBLE
                showTest(view)
            }
        }
        return view
    }

    private fun clearField(view: View){

        materialTestLabelLinearLayout.visibility = View.INVISIBLE
        currentTestNumberLinearLayout.visibility = View.INVISIBLE
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

    private fun showTest(view: View){
        var TOEIC600Words = TOEIC600WordArray[BEGIN]

        currentTestNumberTextView.text = currentTestNumbers.toString()
        answerWordJp = TOEIC600Words.wordjp.toString()
        testWordTextView.text = TOEIC600Words.worden

        val answerOptions = arrayListOf(TOEIC600Words.wordjp!!, TOEIC600Words.option_1!!, TOEIC600Words.option_2!!)
        Collections.shuffle(answerOptions)
        choiceAButton.text = answerOptions[0]
        choiceBButton.text = answerOptions[1]
        choiceCButton.text = answerOptions[2]

            choiceAButton.setOnClickListener {
                checkAnswer(choiceAButton, choiceBButton, choiceCButton)
            }
            choiceBButton.setOnClickListener {
                checkAnswer(choiceBButton, choiceAButton, choiceCButton)
            }
            choiceCButton.setOnClickListener {
                checkAnswer(choiceCButton, choiceAButton, choiceBButton)
            }

        view.setBackgroundColor(Color.WHITE)
        timerObservable.unsubscribe()
        timerObservable = Observable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            inCorrect()
        }
        beginMeasureTimeMillis = System.currentTimeMillis()
    }

    private fun checkAnswer(tappedButton: Button, secondButton: Button, thirdButton: Button){

        //animation中にボタンを押しても反応しないようにしている
        if (!isImageViewAnimated){
            if (answerWordJp == tappedButton.text){
                endMeasureTimeMillis = System.currentTimeMillis()
                answerTimeSeconds = (endMeasureTimeMillis - beginMeasureTimeMillis)/1000.0

                tappedButton.setBackgroundColor(Color.argb(51,91,154,144))
                LessonMaterialManager.updateCorrectAnswerData(context,wordId,answerTimeSeconds)
                updateTestScore(answerTimeSeconds)
                correct()

                if (answerTimeSeconds < 1.50){

                    answerTimeTextView.text = "%.2f".format(answerTimeSeconds)
                    answerTimeTextView.animateScaleAndAlpha()
                    instantAnswerTextView.animateScaleAndAlpha()

                } else {

                    answerTimeTextView.text = "%.2f".format(answerTimeSeconds)
                    answerTimeTextView.animateScaleAndAlpha()
                }
            } else {

                tappedButton.setBackgroundColor(Color.argb(51,255,0,0))
                if (answerWordJp == secondButton.text){
                    secondButton.setBackgroundColor(Color.argb(51,0,166,255))
                } else {
                    thirdButton.setBackgroundColor(Color.argb(51,0,166,255))
                }

                LessonMaterialManager.updateInCorrectAnswerData(context,wordId)
                inCorrect()
            }
        }
    }


    private fun correct(){

        var mp: MediaPlayer = MediaPlayer.create(context, R.raw.okay)
        try {
            mp.start()
        }catch (e:Exception){
            Log.d("Error", e.toString())
        }
        animateAlpha(correctImageView)
        var END = TOEIC600WordArray.size - 1

        if (BEGIN == END) {

            showBlackSheet(isStart = false)
            timerObservable.unsubscribe()

            var handler = Handler()
            handler.postDelayed({
                var home = TestListFragment()
                LessonMaterialManager.updateTestData(context, testId, correctCount, quickTime, averageTime)
                val fragmentManager = fragmentManager.beginTransaction()
                fragmentManager.add(R.id.test_list,home)
                fragmentManager.commit()
            }, 2500)
        } else {
            applyEffects()
        }

        BEGIN++
        currentTestNumbers++

    }

    private fun inCorrect(){

        beginMeasureTimeMillis = 0

        var mp: MediaPlayer = MediaPlayer.create(context, R.raw.ng)
        try {
            mp.start()
        }catch (e:Exception){
            Log.d("Error", e.toString())
        }
        animateAlpha(incorrectImageView)

        var END = TOEIC600WordArray.size - 1
        if (BEGIN == END) {
            finishTextView.alpha = 1.0f
            showBlackSheet(isStart = false)
            timerObservable.unsubscribe()


            var handler = Handler()
            handler.postDelayed({
                var home = TestListFragment()
                LessonMaterialManager.updateTestData(context, testId, correctCount, quickTime, averageTime)
                val fragmentManager = fragmentManager.beginTransaction()
                fragmentManager.add(R.id.test_list,home)
                fragmentManager.commit()
            }, 2500)

        } else {
            applyEffects()
        }

        BEGIN++
        currentTestNumbers++

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

    private fun showBlackSheet(isStart: Boolean){

        val transaction = fragmentManager.beginTransaction()
        var args = Bundle()
        args.putBoolean("isStart", isStart)
        blackSheet.arguments = args

        transaction.add(R.id.test_list,blackSheet)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun applyEffects(){
        var handler = Handler()
        handler.postDelayed({
            choiceAButton.setBackgroundColor(Color.WHITE)
            choiceBButton.setBackgroundColor(Color.WHITE)
            choiceCButton.setBackgroundColor(Color.WHITE)
            showTest(view!!)
        }, 1000)
    }

    private fun animateAlpha(target : ImageView){

        val accelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
        var handler = Handler()
        val alphaShow = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f)
        val alphaHide = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)

        val showAnimator = ObjectAnimator.ofPropertyValuesHolder(target, alphaShow)
        val hideAnimator = ObjectAnimator.ofPropertyValuesHolder(target, alphaHide)

        showAnimator.duration = 300
        showAnimator.interpolator = accelerateDecelerateInterpolator
        showAnimator.start()
        isImageViewAnimated = true
        handler.postDelayed({
            hideAnimator.duration = 50
            hideAnimator.start()
            isImageViewAnimated = false
        }, 950)
    }


    private fun disableBackKey(view: View){
        view.setFocusableInTouchMode(true)
        view.requestFocus()
        view.setOnKeyListener { v, keyCode, event ->
            return@setOnKeyListener (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP)
        }

    }
}