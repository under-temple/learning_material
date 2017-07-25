package com.example.shokiterashita.learningmaterial.views.lib.manager
/**
 * Created by shoki.terashita on 2017/07/21.
 */
import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

import com.example.shokiterashita.learningmaterial.R



object LessonMaterialManager {
    var lessonMaterialConfig:RealmConfiguration? = null

    fun setup(context: Context){
        try {

            //"lesson_material.realm"という名前でContext.getFilesDir()の場所にファイルを作成している。
            lessonMaterialConfig = RealmConfiguration.Builder(context)
                    .name("lesson_material.realm")
                    .deleteRealmIfMigrationNeeded()
                    .build()
//            Realm.getInstance(lessonMaterialConfig)

            loadFromJson(context)

        }catch (e:Exception){
            Log.d("error",e.message)
        }
    }

    //なぜ、ここの処理を、setup()の中でやらないのであろうか。
    fun getLessonMaterial():Realm{
        //設定に従ったRealmを取得
        var realm = Realm.getInstance(lessonMaterialConfig)
        return realm
    }

    fun loadFromJson(context: Context){
        //openRawResource -- rawディレクトリから、ファイルを取得する。
        val testListJsonText = context.resources.openRawResource(R.raw.test_list).bufferedReader().use { it.readText() }
        val wordListJsonText = context.resources.openRawResource(R.raw.words).bufferedReader().use { it.readText() }

        val realm = getLessonMaterial()

        //トランザクション開始：書き込み準備
        realm.executeTransaction { //Realm Doc オブジェクトの自動更新にて、「executeTransaction」が使われている。 https://realm.io/jp/docs/java/1.1.0/#section-8

            //なぜ、createAllFromJsonではないのであろうか。
            //この::class.java の意味が判明　https://kotlinlang.org/docs/reference/java-interop.html
            realm.createObjectFromJson(TOEICFlash600TestList::class.java, testListJsonText)
            realm.createObjectFromJson(TOEICFlash600WordList::class.java, wordListJsonText)
        }
    }

    fun findAllTest():TOEICFlash600TestList{
        val realm = getLessonMaterial()
        var testListFinal = realm.where(TOEICFlash600TestList::class.java).findAll()
        return testListFinal.first()
    }
}


// RealmObjectを継承することで、モデルクラスを定義する。
open class TOEICFlash600TestList : RealmObject() {
    open var list:RealmList<TOEICFlash600Test>? = null //1 対 多
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
    open var list:RealmList<TOEICFlash600Word>? = null //1 対 多
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
}

