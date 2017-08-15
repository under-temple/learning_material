package com.ramotion.expandingcollection.examples.simple

import com.ramotion.expandingcollection.ECCardData

import java.util.ArrayList
import java.util.Arrays
import com.example.shokiterashita.learningmaterial.R

class CardDataImpl(val cardTitle: String, private val mainBackgroundResource: Int?, private val headBackgroundResource: Int?, private val listItems: List<String>) : ECCardData<String> {

    override fun getMainBackgroundResource(): Int? {
        return mainBackgroundResource
    }

    override fun getHeadBackgroundResource(): Int? {
        return headBackgroundResource
    }

    override fun getListItems(): List<String> {
        return listItems
    }

    companion object {

        fun generateExampleData(): List<ECCardData<*>> {
            val list = ArrayList<ECCardData<*>>()
            list.add(CardDataImpl("Card 1", R.drawable.test, R.drawable.test, createItemsList("Card 1")))
            list.add(CardDataImpl("Card 2", R.drawable.test, R.drawable.test, createItemsList("Card 2")))
            list.add(CardDataImpl("Card 3", R.drawable.test, R.drawable.test, createItemsList("Card 3")))
            return list
        }

        private fun createItemsList(cardName: String): List<String> {
            val list = ArrayList<String>()
            list.addAll(Arrays.asList(
                    cardName + " - Item 1",
                    cardName + " - Item 2",
                    cardName + " - Item 3",
                    cardName + " - Item 4",
                    cardName + " - Item 5",
                    cardName + " - Item 6",
                    cardName + " - Item 7"
            ))
            return list
        }
    }

}