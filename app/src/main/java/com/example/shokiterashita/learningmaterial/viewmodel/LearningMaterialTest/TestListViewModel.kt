package com.ramotion.expandingcollection.examples.simple

import android.app.Application
import com.ramotion.expandingcollection.ECCardData
import android.content.Context
import java.util.Arrays
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.views.lib.manager.TOEICFlash600Test
import kotlin.collections.ArrayList

class CardDataImpl(val cardTitle: String,
                   private val mainBackgroundResource: Int?,
                   private val headBackgroundResource: Int?,
                   private val listItems: List<String>) : ECCardData<String> {

    override fun getMainBackgroundResource(): Int? {//親クラスが抽象クラスで、宣言しているため、このメソッドは消せない
        return mainBackgroundResource
    }

    override fun getHeadBackgroundResource(): Int? {
        return headBackgroundResource
    }

    override fun getListItems(): List<String> {
        return listItems
    }


    companion object {

        fun generateTestCardList(testListPosition: Int, context: Context): List<ECCardData<*>> {
            val list = ArrayList<ECCardData<*>>()
            LessonMaterialManager.setup(context)
            //カード枚数が決まる。

            val firstTestListId = testListPosition + 1 + (testListPosition * 10)
            val lastTestListId = firstTestListId + 10
            val testListRange = firstTestListId..lastTestListId


            for (i in testListRange) {
                var testListData = LessonMaterialManager.fetchTestList(i)

                list.add(CardDataImpl("", R.drawable.white, R.drawable.blackborder, createItemsList("Card 1")))
            }
            return list
        }

        fun fetchTestCardContents(testListPosition: Int,context: Context): TOEICFlash600Test{
            LessonMaterialManager.setup(context)
            var testCardContents = TOEICFlash600Test()

//            val firstTestListId = testListPosition + 1 + (testListPosition * 10)
//            val lastTestListId = firstTestListId + 10
//            val testListRange = firstTestListId..lastTestListId

//            for (i in testListRange) {
            testCardContents = LessonMaterialManager.fetchTestList(testListPosition)
//                testCardContentsArray.add(testCardContents)
//            }

            return testCardContents

        }


        private fun createItemsList(cardName: String): List<String> {//使わないけど、消せない。
            val list = ArrayList<String>()
            list.addAll(Arrays.asList(
                    cardName + " - Item 1",
                    cardName + " - Item 2"
            ))
            return list
        }
    }

}