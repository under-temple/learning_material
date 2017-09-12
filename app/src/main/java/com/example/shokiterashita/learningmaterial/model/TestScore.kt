package com.example.shokiterashita.learningmaterial.model

import android.content.Context
import android.content.SharedPreferences
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment

/**
 * Created by shokiterashita on 2017/09/03.
 */


object TestScore{

    //LearningMaterialTestFragmentから呼ばれる。
    fun updateTestScore(wordId: Int, newAnswerTimeMillis: Long, context: Context){

        var prefs = context.getSharedPreferences("TEST_SCORE",Context.MODE_PRIVATE)
        var editor = prefs.edit()

        //oldValueに値が入っていない場合、defaultが0になるため、newValueが格納されない。
        var existAnswerTimeMillis = prefs.getLong("${wordId}_answerTimeMillis",5000)
        if (newAnswerTimeMillis < existAnswerTimeMillis){
            editor.putLong("${wordId}_answerTimeMillis",newAnswerTimeMillis)
        }

        var correctAnswerCount = prefs.getInt("${wordId}_correctAnswerCount",0)
        //fun updateTestScoreが呼ばれるたびに、+1 する。but 値ごとに、管理すべし。
        editor.putInt("${wordId}_correctAnswerCount",correctAnswerCount+1)


        var averageAnswerTimeMillis = prefs.getLong("${wordId}_averageAnswerTimeMillis",0)
        averageAnswerTimeMillis = (averageAnswerTimeMillis + newAnswerTimeMillis)/(correctAnswerCount + 1)
        editor.putLong("${wordId}_averageAnswerTimeMillis",averageAnswerTimeMillis)

        editor.apply()
    }

    fun getAverageAnswerTimeSeconds(wordId: Int,context: Context): Double{

        var prefs = context.getSharedPreferences("TEST_SCORE",Context.MODE_PRIVATE)
        var averageAnswerTimeMillis = prefs.getLong("${wordId}_averageAnswerTimeMillis",0)
        return averageAnswerTimeMillis/1000.0
    }

    fun getFastestAnswerTimeSeconds(wordId: Int,context: Context): Double{

        var prefs = context.getSharedPreferences("TEST_SCORE",Context.MODE_PRIVATE)
        return prefs.getLong("${wordId}_answerTimeMillis",0)/1000.0
    }

    fun getCorrectAnswerCount(wordId: Int,context: Context): Int{

        var prefs = context.getSharedPreferences("TEST_SCORE",Context.MODE_PRIVATE)
        return prefs.getInt("${wordId}_correctAnswerCount",0)
    }
}