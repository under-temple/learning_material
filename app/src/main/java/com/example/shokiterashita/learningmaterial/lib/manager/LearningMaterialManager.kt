package com.example.shokiterashita.learningmaterial.lib.manager
/**
 * Created by shoki.terashita on 2017/07/21.
 */
import android.content.Context
import android.util.Log
import io.realm.annotations.PrimaryKey

import com.example.shokiterashita.learningmaterial.R
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

//    fun nextQuestion(testId : Int):TOEICFlash600Word{
//        val realm = getLessonMaterial()
//        testId = testId + 1
//        return testContent = realm.where(TOEICFlash600Word::class.java).equalTo("id", testId).findFirst()
//    }
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

    open var fastestAnsewrTimeSeconds:Double? = null
    open var averageAnsewrTimeSeconds:Double? = null
    open var correctAnswerCount:Int? = null
}

