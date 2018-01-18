//package com.example.shokiterashita.learningmaterial.viewmodel
package com.ex.expandingcollection.examples.simple


import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.TextView
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.manager.LessonMaterialManager
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Test
import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word
import com.ramotion.expandingcollection.ECCardData
import java.util.*

/**
 * Created by shoki.terashita on 2017/08/17.
 */

class WordListViewModel(val cardTitle: String,
                   private val mainBackgroundResource: Int?,
                   private val headBackgroundResource: Int?,
                   private val listItems: List<String>) : ECCardData<String> {

    companion object {

        fun generateWordCardList(cardRange: IntRange): MutableList<ECCardData<*>> {
            val list = ArrayList<ECCardData<*>>()
            for (i in cardRange) {
                list.add(WordListViewModel("$i", R.drawable.white, R.drawable.white, createItemsList("")))
            }
            return list
        }


        fun fetchAllWordCardArr(context: Context, wordListId: Int) : MutableList<TOEICFlash600Word>{
            LessonMaterialManager.setup(context)
            val realm = LessonMaterialManager.getLessonMaterial()
            var allWordCardArr: MutableList<TOEICFlash600Word> = when (wordListId) {
                0 -> realm.where(TOEICFlash600Word::class.java).lessThanOrEqualTo("id", 100).findAll()

                1 -> realm.where(TOEICFlash600Word::class.java).between("id", 101, 200).findAll()

                2 -> realm.where(TOEICFlash600Word::class.java).greaterThanOrEqualTo("id", 201).findAll()

                else -> realm.where(TOEICFlash600Word::class.java).greaterThanOrEqualTo("id", 201).findAll()
            }
            return allWordCardArr
        }
        fun fetchLearningWordCardArr(context: Context, wordListId: Int) : MutableList<TOEICFlash600Word>{
            LessonMaterialManager.setup(context)
            val realm = LessonMaterialManager.getLessonMaterial()

            val query = realm.where(TOEICFlash600Word::class.java).isNull("answerTimeSeconds").or().greaterThanOrEqualTo("answerTimeSeconds", 1.50).or().equalTo("isCorrect", false)
            var learningWordCardArr: MutableList<TOEICFlash600Word> = when (wordListId) {

                0 -> query.lessThanOrEqualTo("id", 100).findAll()
                1 -> realm.where(TOEICFlash600Word::class.java).between("id", 101, 200).beginGroup().isNull("answerTimeSeconds").or().greaterThanOrEqualTo("answerTimeSeconds", 1.50).or().equalTo("isCorrect", false).endGroup().findAll()
                2 -> realm.where(TOEICFlash600Word::class.java).greaterThanOrEqualTo("id", 201).beginGroup().isNull("answerTimeSeconds").or().greaterThanOrEqualTo("answerTimeSeconds", 1.50).equalTo("isCorrect", false).endGroup().findAll()
                else -> realm.where(TOEICFlash600Word::class.java).greaterThanOrEqualTo("id", 201).findAll()
            }
            return learningWordCardArr
        }

        fun fetchTestCardArr(context: Context, testFirstWordId: Int, testLastWordId:Int) :TOEICFlash600Test{

            LessonMaterialManager.setup(context)
            val realm = LessonMaterialManager.getLessonMaterial()
            return realm.where(TOEICFlash600Test::class.java).equalTo("idx_start",testFirstWordId).equalTo("totalCount",testLastWordId).findFirst()
        }


        //使わないけど、消せない。
        fun createItemsList(cardName: String): List<String> {
            return listOf("No","use")
        }

        fun pronounceWord(context: Context, wordCardId: Int){

            var pronounceId: Int = context.resources.getIdentifier("pronounce_${wordCardId}","raw", context.packageName)
//            var mp: MediaPlayer = MediaPlayer.create(context, pronounceId)
            var mp: MediaPlayer = MediaPlayer.create(context,pronounceId)
//            mp.prepareAsync()

            try {
                mp.start()
            }catch (e:Exception){
               Log.d("Error", e.toString())
            }

        }


        fun showOrHiddenJapaneseWord(wordCardData:TOEICFlash600Word, isClicked:Boolean): String = if(isClicked) wordCardData.wordjp.toString() else "?"
        fun showOrHiddenJapaneseSentense(wordCardData:TOEICFlash600Word, isClicked:Boolean): String = if(isClicked) wordCardData.examplejp.toString() else "? ? ?"

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