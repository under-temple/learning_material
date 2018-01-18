package com.example.shokiterashita.learningmaterial.views.fragments.result.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ex.expandingcollection.examples.simple.WordListViewModel
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.views.fragments.result.TestResultItemList
import com.example.shokiterashita.learningmaterial.views.fragments.result.viewholder.TestResultViewHolder
import com.example.shokiterashita.learningmaterial.views.fragments.result.viewholder.TestResultWordViewHolder


/**
 * Created by shokiterashita on 2017/12/21.
 */

class TestResultRecycleViewAdapter(val mContext: Context, val mValues: List<TestResultItemList>, val testId: Int, val totalAnswerTime: Double) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val HeaderViewId = 1
    enum class HeaderViewIds(val headerId:Int){
        TOP(0),
        BOTTOM(1)
    }


    public interface OnClickListener {
        fun onClickRetryButton()
        fun onClickWordListButton()
        fun onClickTestListButton()

        fun onClickPronounceButton()
        fun onClickShareLinearLayout()
    }

    var mClickListener: TestResultRecycleViewAdapter.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType){
            HeaderViewIds.TOP.headerId ->{

            }
            HeaderViewIds.BOTTOM.headerId ->{

            }
            else ->{

            }
        }

        // TODO: when構文で書き直す.
        if (viewType == HeaderViewId){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.test_result_list_item, parent, false)
            return TestResultViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.test_result_word_list_item, parent, false)
            return TestResultWordViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is TestResultViewHolder){
            var TOEIC600Test = LessonMaterialManager.fetchTestList(testId)

            holder.testResultCorrectCountTextView.text = TOEIC600Test.result.toString()
            holder.testResultInstantAnswerCountTextView.text = TOEIC600Test.instantAnswerCount.toString()
            holder.testResultFirstIndexTextView.text = TOEIC600Test.idx_start.toString()
            holder.testResultLastIndexTextView.text = TOEIC600Test.totalCount.toString()
            holder.testResultFastestAnswerTimeTextView.text = "%.2f".format(TOEIC600Test.fastestTime)
            holder.testResultTotalTestCountTextView.text = (TOEIC600Test.totalCount!! - TOEIC600Test.idx_start!! + 1).toString()
            holder.testResultTotalAnswerTimeTextView.text = "%.2f".format(totalAnswerTime)

            holder.shareLinearLayout.setOnClickListener{ view ->
                mClickListener?.onClickShareLinearLayout()

//                var intent = Intent()
//                intent.setAction(Intent.ACTION_SEND)
//                intent.setPackage("com.twitter.android")
//                intent.setType("image/png")
//                intent.putExtra(Intent.EXTRA_TEXT, "Rarejob 瞬間英単語のテストを受けたよ。${TOEIC600Test.totalCount.toString()}")
            }
            holder.testResultRetryButton.setOnClickListener{ view ->
                mClickListener?.onClickRetryButton()
            }
            holder.testResultWordListButton.setOnClickListener { view ->
                mClickListener?.onClickWordListButton()
            }
            holder.testResultTestListButton.setOnClickListener { view ->
                mClickListener?.onClickTestListButton()
            }

            // オブジェクト式　無名内部クラス
            holder.mClickListener = object : TestResultViewHolder.OnClickListener {

                override fun onClickRetryButton() {
                    mClickListener?.onClickRetryButton()
                }

                override fun onClickTestListButton() {
                    mClickListener?.onClickTestListButton()
                }

                override fun onClickWordListButton() {
                    mClickListener?.onClickWordListButton()
                }

                // クリックしても呼ばれない。てか、pronounceButtonImageiconとbindしているかも謎である。
                override fun onClickPronounceButton() {
                    mClickListener?.onClickPronounceButton()
                }

                override fun onClickShareLinearLayout() {
                    mClickListener?.onClickShareLinearLayout()
                }
            }

        } else if (holder is TestResultWordViewHolder){

            var TOEIC600Word = mValues[position - 1]
            holder.answerdWordEnTextview.text = TOEIC600Word.answerd_word_en
            holder.answerTimeTextView.text = TOEIC600Word.answer_time
            holder.answerdWordJaTextview.text = TOEIC600Word.answerd_word_ja

            if (TOEIC600Word.isCorrect){

                //colorには、アクセスできていないけど、画像は取得できている。
                holder.judgementImageView.setImageResource(R.drawable.correct)
                holder.answerdWordEnTextview.setTextColor(ContextCompat.getColor(mContext, R.color.correctAnswerTimeColor))
            } else {
                holder.judgementImageView.setImageResource(R.drawable.incorrect)

                //上でかきなおす。
                holder.answerdWordEnTextview.setTextColor(mContext.resources.getColor(R.color.colorLightRed))
            }

            // viewHolderにリスナーを実装すべし。
            holder.testResultPronounceIconImageView.setOnClickListener{
                WordListViewModel.pronounceWord(mContext, TOEIC600Word.wordId)
            }
        }
    }

    override fun getItemCount(): Int = this.mValues.count() + 1
    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 1
        } else {
            return super.getItemViewType(position)
        }
    }
}