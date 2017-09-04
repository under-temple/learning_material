/*
package com.ex.expandingcollection.examples.simple
*/
package com.example.shokiterashita.learningmaterial.viewModel

import android.app.Application
import com.ramotion.expandingcollection.ECCardData
import android.content.Context
import java.util.Arrays
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Test
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word
import kotlin.collections.ArrayList

class TestListViewModel(val cardTitle: String,
                   private val mainBackgroundResource: Int?,
                   private val headBackgroundResource: Int?,
                   private val listItems: List<String>) : ECCardData<String> {

    companion object {

        fun generateTestCardList(testListPosition: Int): List<ECCardData<*>> {
            val list = ArrayList<ECCardData<*>>()

            val firstTestListId = testListPosition + 1 + (testListPosition * 10)
            val lastTestListId = firstTestListId + 10
            val testListRange = firstTestListId..lastTestListId

            for (i in testListRange) {
                list.add(TestListViewModel("", R.drawable.white, R.drawable.blackborder, createItemsList("Card 1")))
            }
            return list
        }

        fun fetchTestCardContents(testListPosition: Int,context: Context): TOEICFlash600Test{
            LessonMaterialManager.setup(context)
            return LessonMaterialManager.fetchTestList(testListPosition)
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