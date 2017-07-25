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
import io.realm.*
import io.realm.annotations.PrimaryKey
import org.json.JSONArray
import java.util.HashSet


class LearningMaterialTestFragment : Fragment() {

    lateinit var testTitleTextView: TextView
    lateinit var currentTestNumberTextView: TextView
    lateinit var overallTestNumberTextView: TextView
    lateinit var testWordTextView: TextView

    lateinit var choiceAButton: Button
    lateinit var choiceBButton: Button
    lateinit var choiceCButton: Button
    lateinit var realm: Realm
    open var sampleDate : String = ""


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        realm = Realm.getDefaultInstance()
//        val results = RealmList<TOEICFlash600TestList>()
//        val results = RealmList<JSONArray>()

        LessonMaterialManager.setup(context)
        var testListFinal = LessonMaterialManager.findAllTest()

//        results.addAll(testListFinal.subList(0,testListFinal.size))
        Log.d("toeicWordList", "${testListFinal}")
//        sampleDate = results.size.toString()

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_learning_material_test, container, false)


        testTitleTextView = view.findViewById(R.id.material_test_title)
        currentTestNumberTextView = view.findViewById(R.id.currentTestNumber)
        overallTestNumberTextView = view.findViewById(R.id.overallTestNumber)
        testWordTextView = view.findViewById(R.id.testWord)

        choiceAButton = view.findViewById(R.id.choice_a)
        choiceBButton = view.findViewById(R.id.choice_b)
        choiceCButton = view.findViewById(R.id.choice_c)


        testTitleTextView.text = "test phrase"
        testTitleTextView.text = sampleDate


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
