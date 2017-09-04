package com.example.shokiterashita.learningmaterial.views.lib.extention

import android.util.AttributeSet
import android.content.Context
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import com.ramotion.expandingcollection.ECPagerCard
import com.ramotion.expandingcollection.ECPagerCardContentList

/**
 * Created by shokiterashita on 2017/08/25.
 */

class WordListCard(context: Context) : ECPagerCard(context) {

    var englishWord: TextView? = null
    var japaneseWord: TextView? = null
    var japaneseSentence: TextView? = null
    var englishSentence: TextView? = null
    var previousCorrectCount: TextView? = null
    var testNumberLabel: TextView? = null

    var startTestButton: Button? = null
    var showJpButton: ToggleButton? = null
    var pronounceButton: ImageButton? = null


}