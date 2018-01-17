package com.example.shokiterashita.learningmaterial.views.fragments.result.viewholder

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.fragments.result.adapter.TestResultRecycleViewAdapter
import org.w3c.dom.Text

/**
 * Created by shokiterashita on 2017/12/25.
 */



/**
 * Created by kentaro.haneda on 16/11/28.
 */
class TestResultViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {

    lateinit var testResultFirstIndexTextView: TextView
    lateinit var testResultLastIndexTextView: TextView
    lateinit var testResultCorrectCountTextView: TextView
    lateinit var testResultTotalTestCountTextView: TextView
    lateinit var testResultTotalAnswerTimeTextView: TextView
    lateinit var testResultFastestAnswerTimeTextView: TextView
    lateinit var testResultInstantAnswerCountTextView: TextView
    lateinit var testResultRetryButton: TextView
    lateinit var testResultWordListButton: TextView
    lateinit var testResultTestListButton: TextView
    lateinit var twitterImageView: ImageView
    lateinit var facebookImageView: ImageView

    lateinit var itemAdapter: TestResultRecycleViewAdapter
    var mClickListener: TestResultViewHolder.OnClickListener? = null


    public interface OnClickListener {
        fun onClickRetryButton()
        fun onClickWordListButton()
        fun onClickTestListButton()

        fun onClickPronounceButton()
        fun onClickTwitterButton()
        fun onClickFacebookButton()
    }



    init {

        testResultFirstIndexTextView = mView.findViewById(R.id.test_result_first_index_text_view)
        testResultLastIndexTextView = mView.findViewById(R.id.test_result_last_index_text_view)
        testResultCorrectCountTextView = mView.findViewById(R.id.test_result_correct_count_text_view)
        testResultTotalTestCountTextView = mView.findViewById(R.id.test_result_total_test_count_text_view)
        testResultTotalAnswerTimeTextView = mView.findViewById(R.id.test_result_total_answer_time_text_view)
        testResultFastestAnswerTimeTextView = mView.findViewById(R.id.test_result_fastest_answer_time_text_view)
        testResultInstantAnswerCountTextView = mView.findViewById(R.id.test_result_instant_answer_count_text_view)

        testResultRetryButton = mView.findViewById(R.id.test_result_retry_button)
        testResultWordListButton = mView.findViewById(R.id.test_result_word_list_button)
        testResultTestListButton = mView.findViewById(R.id.test_result_test_list_button)

        twitterImageView = mView.findViewById(R.id.twitter_image_view)
        facebookImageView = mView.findViewById(R.id.facebook_image_view)

    }


}