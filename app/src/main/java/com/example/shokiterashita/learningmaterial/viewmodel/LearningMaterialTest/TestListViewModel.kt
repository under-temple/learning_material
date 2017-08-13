package com.example.shokiterashita.learningmaterial.viewmodel.LearningMaterialTest

import com.ramotion.expandingcollection.ECCardData
import com.example.shokiterashita.learningmaterial.R
import java.util.ArrayList



/**
 * Created by shokiterashita on 2017/08/13.
 */
class CardDataImpl : ECCardData<String> {

    private val cardTitle: String? = null
    private val mainBackgroundResource: Int? = null
    private val headBackgroundResource: Int? = null

    override fun getMainBackgroundResource(): Int? {
        return mainBackgroundResource
    }


    override fun getHeadBackgroundResource(): Int? {
        return headBackgroundResource
    }

    override fun getListItems(): List<String> {
        return listItems
    }

    fun getCardTitle(): String? {
        return cardTitle
    }


    fun generateExampleData(): List<ECCardData<*>> {
        val list = ArrayList<ECCardData<*>>()
        list.add(CardDataImpl(R.drawable.test, R.drawable.test))
        list.add(CardDataImpl("Card 2", R.drawable.test, R.drawable.test))
        list.add(CardDataImpl("Card 3", R.drawable.test, R.drawable.test))
        return list
    }
}