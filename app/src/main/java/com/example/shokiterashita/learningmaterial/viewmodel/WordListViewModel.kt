package com.ramotion.expandingcollection.examples.simple

import android.content.Context
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.views.lib.manager.TOEICFlash600Word
import com.ramotion.expandingcollection.ECCardData
import java.util.*

/**
 * Created by shoki.terashita on 2017/08/17.
 */

class CardWordDataImpl(val cardTitle: String,
                   private val mainBackgroundResource: Int?,
                   private val headBackgroundResource: Int?,
                   private val listItems: List<String>) : ECCardData<String> {

    companion object {

        //単語カードの発行枚数を算出するメソッド
        fun generateWordCardList(wordListPosition: Int): List<ECCardData<*>> {
            val list = ArrayList<ECCardData<*>>()

            //0 -> 1  1-> 101 2-> 201 スマートな算出方法を、後で考える。
            var firstTestListId = wordListPosition + (wordListPosition * 100)
            if (firstTestListId == 0){
                firstTestListId = 1
            }

            var lastTestListId = firstTestListId + 99
            val testListRange = firstTestListId..lastTestListId

            for (i in testListRange) {
                list.add(CardWordDataImpl("", R.drawable.white, R.drawable.blackborder, createItemsList("")))
            }
            return list
        }

        fun fetchWordCardContents(wordListPosition: Int,context: Context): TOEICFlash600Word{
            LessonMaterialManager.setup(context)
            return LessonMaterialManager.fetchWordList(wordListPosition)
        }

        private fun createItemsList(cardName: String): List<String> {//使わないけど、消せない。
            return listOf("No","use")
        }
    }
















    override fun getMainBackgroundResource(): Int? {//親クラスが抽象クラスで、宣言しているため、このメソッドは消せない
        return mainBackgroundResource
    }

    override fun getHeadBackgroundResource(): Int? {
        return headBackgroundResource
    }

    override fun getListItems(): List<String> {
        return listItems
    }

}