package com.example.shokiterashita.learningmaterial.views.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
//import com.beardedhen.androidbootstrap.TypefaceProvider
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.example.shokiterashita.learningmaterial.views.fragments.TestListFragment
import com.example.shokiterashita.learningmaterial.views.fragments.word.WordListFragment
import com.example.shokiterashita.learningmaterial.views.lib.manager.extention.replaceFragment

class MainActivity : AppCompatActivity() {

    internal var learningMaterial: LearningMaterialTestFragment = LearningMaterialTestFragment()
    internal var testList: TestListFragment = TestListFragment()
    internal var wordList: WordListFragment = WordListFragment()

    val fragmentManager = supportFragmentManager

    lateinit var testButton:Button
    lateinit var testListButton:Button
    lateinit var wordListButton:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        TypefaceProvider.registerDefaultIconSets()

        setContentView(R.layout.activity_main)
        testButton = findViewById(R.id.toFragmentButton) as Button
        testListButton = findViewById(R.id.toTestListButton) as Button
        wordListButton = findViewById(R.id.toWordListButton) as Button


        testButton.setOnClickListener {
            this.showFragment()
        }
        testListButton.setOnClickListener {
            this.showTestList()
        }
        wordListButton.setOnClickListener {
            this.showWordList()
        }
    }

    override fun onStart() {
        super.onStart()
    }


    fun showFragment() {

        this.replaceFragment(R.id.content_layout, learningMaterial)
    }

    fun showTestList(){

        this.replaceFragment(R.id.content_layout, testList)
    }
    fun showWordList(){

        this.replaceFragment(R.id.content_layout, wordList)
    }

}
