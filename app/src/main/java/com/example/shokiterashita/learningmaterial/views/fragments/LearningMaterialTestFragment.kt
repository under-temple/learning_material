package com.example.shokiterashita.learningmaterial.views.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.lib.manager.LessonMaterialManager

import android.util.Log
import com.example.shokiterashita.learningmaterial.views.lib.manager.TOEICFlash600TestList
import com.example.shokiterashita.learningmaterial.views.lib.manager.TOEICFlash600WordList
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONArray
import io.realm.Sort
import io.realm.RealmResults




class LearningMaterialTestFragment : Fragment() {

    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var overallTestNumberTextView: TextView
    lateinit var testWordTextView: TextView

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button
    lateinit var realm: Realm


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        LessonMaterialManager.setup(context)
        var testListFinal = realm.where(TOEICFlash600TestList::class.java).findAll()

//        realm.where(TOEICFlash600TestList::class.java.findAll().forEach {
            Log.d("RealmWithKotlin", "${testListFinal} is  years old.")
//            // Tama is 3 years old.
//            // Mike is 2 years old.
//        }
//        val tweets = realm.allObjectsSorted(TOEICFlash600TestList::class.java, "createdAt", Sort.DESCENDING)
        var testList = TOEICFlash600TestList::class.java
        var testListWord = testList
//        var wordList = testWordsList.TOEICFlash600WordList
//        var wordList = TOEICFlash600TestList


        Log.d("toeicWordList", "${testList}")


//        val realmConfiguration = RealmConfiguration.Builder().build()
//        Realm.deleteRealm(realmConfiguration)
//        realm = Realm.getInstance(realmConfiguration)


        LessonMaterialManager.setup(context)




//        LessonMaterialManager.setup(context)
//        var wordList = LessonMaterialManager.setup(context)
//
//        print(wordList)
//        Log.d(wordList.toString(), "wordlist")




//        var wordList : JSONArray = testListJsonText



    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_learning_material_test, container, false)

        //UIコンポーネントをプログラムで扱えるようにする。
        testTitleTextView = view.findViewById(R.id.material_test_title)
        currentTestNumberTextView = view.findViewById(R.id.currentTestNumber)
        overallTestNumberTextView = view.findViewById(R.id.overallTestNumber)
        testWordTextView = view.findViewById(R.id.testWord)

        choiceAButton = view.findViewById(R.id.choice_a)
        choiceBButton = view.findViewById(R.id.choice_b)
        choiceCButton = view.findViewById(R.id.choice_c)


        testTitleTextView.text = "あと２時間で、進歩報告"


        return view
    }


    override fun onDetach() {
        super.onDetach()
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }





}
