package com.example.shokiterashita.learningmaterial.views.fragments.result

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.example.shokiterashita.learningmaterial.views.fragments.result.adapter.TestResultRecycleViewAdapter
import com.example.shokiterashita.learningmaterial.views.fragments.test.TestListFragment
import com.example.shokiterashita.learningmaterial.views.fragments.word.WordListFragment


class LearningMaterialTestResultFragment : Fragment(){

    lateinit var testResultRecyclerView: RecyclerView
    private var testResultRecyclerAdapter: TestResultRecycleViewAdapter? = null
    private var testResultLayoutManager: LinearLayoutManager? = null
    private var wordDataArr = mutableListOf<TestResultItemList>()
    private var testId: Int = 0
        get() = arguments.getInt("testId")
    private var isNormalOrder: Boolean = true
        get() = arguments.getBoolean("isNormalOrder")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var testWordsId = arguments.getIntegerArrayList("showedWordIds")

        LessonMaterialManager.setup(context)
        for( i in 0 .. testWordsId.size - 1){
            var TOEIC600Word = LessonMaterialManager.fetchWordList(testWordsId[i])
            wordDataArr.add(TestResultItemList(TOEIC600Word.id!!, R.drawable.correct, TOEIC600Word.worden!!, TOEIC600Word.answerTimeSeconds.toString(), TOEIC600Word.wordjp!!, TOEIC600Word.isCorrect!!))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_learning_material_test_result, container, false)
        var totalTestTimeSeconds: Double = arguments.getDouble("totalAnswerTime")
        val mp: MediaPlayer = MediaPlayer.create(context,R.raw.result)
        try {
            mp.start()
        }catch (e:Exception){
            Log.d("Error", e.toString())
        }

        testResultRecyclerView = view.findViewById(R.id.test_result_recyclerview)
        testResultRecyclerView.setHasTransientState(true)

        testResultLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        testResultRecyclerView.layoutManager = testResultLayoutManager

        testResultRecyclerAdapter = TestResultRecycleViewAdapter(context, wordDataArr, testId, totalTestTimeSeconds)
        testResultRecyclerAdapter?.mClickListener = object : TestResultRecycleViewAdapter.OnClickListener {

            override fun onClickRetryButton() {

                var transaction = fragmentManager.beginTransaction()
                var learningMaterial = LearningMaterialTestFragment()
                var args = Bundle()
                args.putInt("testId", testId)
                args.putBoolean("isNormalOrder", isNormalOrder)
                learningMaterial.arguments = args
                transaction.replace(R.id.learning_material_test_result_fragment,learningMaterial)
//                transaction.addToBackStack(null)
                transaction.commit()
            }

            override fun onClickWordListButton() {

                var transaction = fragmentManager.beginTransaction()
                var wordList = WordListFragment()

//                var args = Bundle()
//                // テストリストのグループを指定する。
//                args.putInt("wordListGroupId", 1)
//                wordList.arguments = args

                transaction.replace(R.id.learning_material_test_result_fragment,wordList)
                transaction.commit()

            }

            override fun onClickTestListButton() {

                var transaction = fragmentManager.beginTransaction()
                var testList = TestListFragment()

                //テストリスト画面に飛ぶ仕様を実装する。1-100, 101-200, 201-300で考える。
//                var args = Bundle()
//                // テストリストのグループを指定する。
//                args.putInt("testListPosition", 1)
//                testList.arguments = args

                transaction.replace(R.id.learning_material_test_result_fragment,testList)
                transaction.commit()
            }


            override fun onClickPronounceButton() {
                //viewAdapterで実装した。

            }

            //ボタンを一個に統一する。
            override fun onClickTwitterButton() {

                var builder = ShareCompat.IntentBuilder.from(activity)
                var TOEIC600Test = LessonMaterialManager.fetchTestList(testId)
                var sharedText = "瞬間英単語のテスト[${TOEIC600Test.idx_start} - ${TOEIC600Test.totalCount}]を終えました。瞬間回答出来た単語は${TOEIC600Test.instantAnswerCount}個でした。 #瞬間英単語 #TOEIC"

                builder.setChooserTitle("Sharecompat test")
                builder.setSubject("Sharecompat subject")
                builder.setText(sharedText)
                builder.setType("text/plain")
                builder.startChooser()
            }

            override fun onClickFacebookButton() {

                var builder = ShareCompat.IntentBuilder.from(activity)
                var TOEIC600Test = LessonMaterialManager.fetchTestList(testId)
                var sharedText = "瞬間英単語のテスト[${TOEIC600Test.idx_start} - ${TOEIC600Test.totalCount}]を終えました。瞬間回答出来た単語は${TOEIC600Test.instantAnswerCount}個でした。 #瞬間英単語 #TOEIC"

                builder.setChooserTitle("Sharecompat test")
                builder.setSubject("Sharecompat subject")
                builder.setText(sharedText)
                builder.setType("text/plain")
                builder.startChooser()
            }
        }

        testResultRecyclerView.adapter = testResultRecyclerAdapter
        return view
    }
}
