package com.example.shokiterashita.learningmaterial.lib.manager
/**
 * Created by shoki.terashita on 2017/07/21.
 */
import android.content.Context
import android.util.Log
import io.realm.annotations.PrimaryKey

import com.example.shokiterashita.learningmaterial.R
import com.ramotion.expandingcollection.ECCardData
import io.realm.*


object LessonMaterialManager {
    var lessonMaterialConfig:RealmConfiguration? = null
    fun setup(context: Context){
        try {
            lessonMaterialConfig = RealmConfiguration.Builder(context)
                    .name("lesson_material.realm")
                    .deleteRealmIfMigrationNeeded()
                    .build()

            loadFromJson(context)

        }catch (e:Exception){
            Log.d("error",e.message)
        }
    }
    fun getLessonMaterial():Realm{
        var realm = Realm.getInstance(lessonMaterialConfig)
        return realm
    }

    fun loadFromJson(context: Context){
        val testListJsonText = context.resources.openRawResource(R.raw.test_list).bufferedReader().use { it.readText() }
        val wordListJsonText = context.resources.openRawResource(R.raw.words).bufferedReader().use { it.readText() }

        val realm = getLessonMaterial()

        realm.executeTransaction {
            realm.createObjectFromJson(TOEICFlash600TestList::class.java, testListJsonText)
            realm.createObjectFromJson(TOEICFlash600WordList::class.java, wordListJsonText)
        }
    }
    fun generateEcPagerCardArr(context: Context): MutableList<TOEICFlash600Word>{
        setup(context)
        var realm = Realm.getInstance(lessonMaterialConfig)
        return realm.where(TOEICFlash600Word::class.java).findAll()

    }

    fun convertTestIDtoWordID(testId:Int): Int{

        val realm = getLessonMaterial()
        val TOEIC600TestId = realm.where(TOEICFlash600Test::class.java).equalTo("id", testId).findFirst().idx_start
        return realm.where(TOEICFlash600Word::class.java).equalTo("id", TOEIC600TestId).findFirst().id!!
    }

    fun fetchTestList(testListId:Int): TOEICFlash600Test{
        val realm = getLessonMaterial()
        return realm.where(TOEICFlash600Test::class.java).equalTo("id", testListId).findFirst()
    }

    fun fetchWordList(testListId:Int): TOEICFlash600Word{
        val realm = getLessonMaterial()
        return realm.where(TOEICFlash600Word::class.java).equalTo("id", testListId).findFirst()
    }

    fun updateCorrectAnswerData(context: Context, wordId: Int, answerTimeSeconds:Double){
        setup(context)
        val realm = getLessonMaterial()
        realm.executeTransaction {
            val wordData = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst()

            //isCorrectの更新
            wordData.isCorrect = true
            // fastestAnsewrTimeSeconds の更新
            var existingAnswerTimeSeconds = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst().fastestAnswerTimeSeconds
            if (existingAnswerTimeSeconds == null){
                wordData.fastestAnswerTimeSeconds = answerTimeSeconds
            }else if (existingAnswerTimeSeconds > answerTimeSeconds ) {
                wordData.fastestAnswerTimeSeconds = answerTimeSeconds
            }

            // correctAnswerCount の更新
            var correctCount = wordData.correctAnswerCount
            wordData.correctAnswerCount++

            // averageAnsewrTimeSeconds の更新
            var averageAnswerTime = wordData.averageAnswerTimeSeconds
            if (averageAnswerTime == null){
                wordData.averageAnswerTimeSeconds = answerTimeSeconds
            }else{
                wordData.averageAnswerTimeSeconds = (averageAnswerTime + answerTimeSeconds)/(correctCount + 1)
            }
        }
    }

    fun updateInCorrectAnswerData(context: Context, wordId: Int){
        setup(context)
        val realm = getLessonMaterial()
        realm.executeTransaction {
            val wordData = realm.where(TOEICFlash600Word::class.java).equalTo("id", wordId).findFirst()
            wordData.isCorrect = false
        }

    }

    fun generateTestWordArray(context: Context, testId: Int) :ArrayList<TOEICFlash600Word>{
        setup(context)
        val realm = getLessonMaterial()
        var TOEIC600WordArray = ArrayList<TOEICFlash600Word>()

        var query = realm.where(TOEICFlash600Test::class.java).equalTo("id", testId).findFirst()
        query.idx_start

        for (i in query.idx_start!! .. query.totalCount!!){

            var TOEIC600Word = realm.where(TOEICFlash600Word::class.java).equalTo("id", i).findFirst()
            TOEIC600WordArray.add(TOEIC600Word)

        }




        return TOEIC600WordArray
    }


}


open class TOEICFlash600TestList : RealmObject() {
    open var list:RealmList<TOEICFlash600Test>? = null
}

open class TOEICFlash600Test:RealmObject(){
    @PrimaryKey
    open var id:Int? = null

    open var idx_start:Int? = null
    open var result:Int? = null
    open var totalCount:Int? = null
    open var time:Int? = null
    open var quicktime:Int? = null
    open var typetest:Int? = null
    open var typeshow:Int? = null
}

open class TOEICFlash600WordList : RealmObject() {
    open var list:RealmList<TOEICFlash600Word>? = null
}



open class TOEICFlash600Word:RealmObject(){
    @PrimaryKey
    open var id:Int? = null

    open var worden:String? = null
    open var wordjp:String? = null
    open var examplejp:String? = null
    open var option_1:String? = null
    open var option_2:String? = null
    open var exampleen:String? = null
    open var isCorrect:Boolean? = null

    open var isTestData:Boolean = false
    open var testFirstWordId:Int? = null
    open var testLastWordId: Int? = null

    open var fastestAnswerTimeSeconds:Double? = null
    open var averageAnswerTimeSeconds:Double? = null
    open var correctAnswerCount:Int = 0


}

