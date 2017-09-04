package com.ramotion.expandingcollection.examples.simple

import android.content.Context
import android.media.MediaPlayer
import android.widget.TextView
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word
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

            var firstTestListId = wordListPosition + (wordListPosition * 100)
            if (firstTestListId == 0){
                firstTestListId = 1
            }

            var lastTestListId = firstTestListId + 99
            val testListRange = firstTestListId..lastTestListId

            for (i in testListRange) {
                list.add(CardWordDataImpl("i", R.drawable.white, R.drawable.white, createItemsList("")))
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

        fun pronounceWord(context: Context, wordCardId: Int){

            var pronounceId: Int = context.resources.getIdentifier("pronounce_${wordCardId}","raw", context.packageName)
            var mp: MediaPlayer = MediaPlayer.create(context, pronounceId)
            mp.start()

        }


        fun showOrHiddenJapaneseWord(wordCardData:TOEICFlash600Word, isClicked:Boolean): String = if(isClicked) wordCardData.wordjp.toString() else "?"

        fun showOrHiddenJapaneseSentense(wordCardData:TOEICFlash600Word, isClicked:Boolean): String = if(isClicked) wordCardData.examplejp.toString() else "? ? ?"




        fun showJapanese(id:Int):String {
            return LessonMaterialManager.fetchWordList(id).wordjp.toString()
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