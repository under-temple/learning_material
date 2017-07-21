//package com.example.shokiterashita.learningmaterial.views.lib.manager
///**
// * Created by shoki.terashita on 2017/07/21.
// */
//import android.content.Context
//import android.util.Log
//import io.realm.Realm
//import io.realm.RealmConfiguration
//import io.realm.RealmList
//import io.realm.RealmObject
//import io.realm.annotations.PrimaryKey
//
//object LessonMaterialManager {
//    var lessonMaterialConfig:RealmConfiguration? = null
//
//    fun setup(context: Context){
//        try {
//            //教材データ用
//            lessonMaterialConfig = RealmConfiguration.Builder(context).name("lesson_material.realm").deleteRealmIfMigrationNeeded().build()
////            Realm.getInstance(lessonMaterialConfig)
//            loadFromJson(context)
//        }catch (e:Exception){
//            Log.d("error",e.message)
//        }
//    }
//
//    fun getLessonMaterial():Realm{
//        var realm = Realm.getInstance(lessonMaterialConfig)
//        return realm
//    }
//
//    fun loadFromJson(context: Context){
//        val testListJsonText = context.resources.openRawResource(R.raw.test_list).bufferedReader().use { it.readText() }
//        val wordListJsonText = context.resources.openRawResource(R.raw.words).bufferedReader().use { it.readText() }
//        val realm = getLessonMaterial()
//        realm.executeTransaction {
//            realm.createObjectFromJson(TOEICFlash600TestList::class.java, testListJsonText)
//            realm.createObjectFromJson(TOEICFlash600WordList::class.java, wordListJsonText)
//        }
//    }
//
//}
//
//open class TOEICFlash600TestList : RealmObject() {
//    open var list:RealmList<TOEICFlash600Test>? = null
//}
//
//open class TOEICFlash600Test:RealmObject(){
//    @PrimaryKey
//    open var id:Int? = null
//
//    open var idx_start:Int? = null
//    open var result:Int? = null
//    open var totalCount:Int? = null
//    open var time:Int? = null
//    open var quicktime:Int? = null
//    open var typetest:Int? = null
//    open var typeshow:Int? = null
//}
//
//open class TOEICFlash600WordList : RealmObject() {
//    open var list:RealmList<TOEICFlash600Word>? = null
//}
//open class TOEICFlash600Word:RealmObject(){
//    @PrimaryKey
//    open var id:Int? = null
//
//    open var worden:String? = null
//    open var wordjp:String? = null
//    open var examplejp:String? = null
//    open var option_1:String? = null
//    open var option_2:String? = null
//    open var exampleen:String? = null
//}
//
