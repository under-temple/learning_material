package com.example.shokiterashita.learningmaterial.views.fragments.result.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.shokiterashita.learningmaterial.R
import org.w3c.dom.Text

/**
 * Created by shokiterashita on 2017/12/26.
 */

class TestResultWordViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {


    lateinit var judgementImageView: ImageView
    lateinit var answerdWordEnTextview: TextView
    lateinit var answerTimeTextView: TextView
    lateinit var answerdWordJaTextview: TextView
    lateinit var testResultPronounceIconImageView: ImageView

    init {
        judgementImageView = mView.findViewById(R.id.judgement_imageview)
        answerdWordEnTextview = mView.findViewById(R.id.answerd_word_en_textview)
        answerTimeTextView = mView.findViewById(R.id.answer_time_textvew)
        answerdWordJaTextview = mView.findViewById(R.id.answerd_word_ja_textview)
        testResultPronounceIconImageView = mView.findViewById(R.id.test_result_pronounce_icon_image_view)


        testResultPronounceIconImageView.setOnClickListener {
            tapPronounceButton()
        }
    }



    fun tapPronounceButton(){
        //メソッドの名前の付け方として、①メソッドが起こすアクション自体の言語化②その関数が発火する行為の言語化。今回は、②になる。汎用的ではないと思う。


    }

}