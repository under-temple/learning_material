package com.example.shokiterashita.learningmaterial.views.fragments.word

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
//import com.beardedhen.androidbootstrap.TypefaceProvider
import com.example.shokiterashita.learningmaterial.R
import com.ramotion.expandingcollection.*
import com.ex.expandingcollection.examples.simple.WordListViewModel

import com.example.shokiterashita.learningmaterial.lib.manager.TOEICFlash600Word

/**
 * Created by shokiterashita on 2017/08/17.
 */

class WordListFragment: Fragment() {

    //ECPagerCardのサブクラスのインスタンス
    private var ecPagerView: ECPagerView? = null
    private var wordCardArr: MutableList<TOEICFlash600Word>? = null
    private var ecPagerCardCount:Int = 111
    private var dataset: MutableList<ECCardData<*>>? = null
    private var cardRange: IntRange? = null
    lateinit var learningWordButton: RadioButton
    lateinit var allWordButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_word_list, container, false)

        wordCardArr = WordListViewModel.fetchLearningWordCardArr(context, 1)
        ecPagerCardCount = wordCardArr?.size ?: 0

        ecPagerView = view.findViewById(R.id.ec_word_pager_element)
        learningWordButton = view.findViewById(R.id.learning_word_button)
        allWordButton = view.findViewById(R.id.all_word_button)

        cardRange = wordCardArr?.let { it.indices }
        dataset = WordListViewModel.generateWordCardList(cardRange!!)

        val fragment = LearningWordListFragment()
        val fragmentManager = childFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.ec_word_pager_element,fragment)
        fragmentManager.commit()

        learningWordButton.setOnClickListener {
            val fragment = LearningWordListFragment()
            val fragmentManager = childFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.ec_word_pager_element,fragment)
            fragmentManager.commit()
        }

        allWordButton.setOnClickListener {
            val fragment = AllWordListFragment()
            val fragmentManager = childFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.ec_word_pager_element,fragment)
            fragmentManager.commit()
        }
        return view
    }
}