package com.ramotion.expandingcollection.examples.simple

import com.ramotion.expandingcollection.ECCardData

import java.util.ArrayList
import java.util.Arrays
import com.example.shokiterashita.learningmaterial.R

 open class CardDataImpl(val cardTitle: String,
                   private val mainBackgroundResource: Int?,
                   private val headBackgroundResource: Int?,
                   private val listItems: List<String>) : ECCardData<String> {

    override fun getMainBackgroundResource(): Int? {//親クラスが抽象クラスで、宣言しているため、getMainBackgroundResourceメソッドは消せない
        return mainBackgroundResource
    }

    override fun getHeadBackgroundResource(): Int? {
        return headBackgroundResource
    }

    override fun getListItems(): List<String> {
        return listItems
    }

     fun fetchCardData(){
         //ここで、RealmManagerから、100枚分のデータを取得するメソッドを考える。
     }

    companion object {

        fun generateExampleData(): List<ECCardData<*>> {
            val list = ArrayList<ECCardData<*>>()
            list.add(CardDataImpl("Card 1", R.drawable.white, R.drawable.blackborder, createItemsList("Card 1")))
            list.add(CardDataImpl("Card 2", R.drawable.white, R.drawable.blackborder, createItemsList("Card 2")))
            list.add(CardDataImpl("Card 3", R.drawable.white, R.drawable.blackborder, createItemsList("Card 3")))
            return list
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