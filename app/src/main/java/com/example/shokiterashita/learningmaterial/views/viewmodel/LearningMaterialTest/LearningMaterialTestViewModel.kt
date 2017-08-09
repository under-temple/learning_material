package com.example.shokiterashita.learningmaterial.views.viewmodel.LearningMaterialTest

import android.widget.Toast
import javax.xml.datatype.DatatypeConstants.SECONDS
import android.widget.EditText
import android.os.Bundle
import android.widget.Button

import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.Observable
import rx.subjects.BehaviorSubject
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.example.shokiterashita.learningmaterial.R
import rx.functions.Func1





/**
 * Created by shoki.terashita on 2017/08/09.
 */
//class LearningMaterialTestViewModel {
//
//    companion object {
//        private val TAG = "MyActivity"
//    }

//    fun countTime(): Observable<Button>{
//
//
//
//    }

//
//    //イベントを、Voidに変換。
//    val signalizer: Func1<Any, Void> = Func1 { null }
//


//    fun loadMoreArticle(): Observable<List<Article>> {
//        // Don't try and load if we're already loading
//        if (isLoadingSubject.value) {
//            return Observable.empty<List<Article>>()
//        }
//
//        isLoadingSubject.onNext(true)
//
//        return LearningAPIService()
//                .api
//                .request("", PER_PAGE, page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnError { throwable ->
//
//                }.doOnNext { list ->
//            val fullList = ArrayList(articlesSubject.value)
//            fullList.addAll(list)
//            articlesSubject.onNext(fullList)
//            page += 1
//
//            isLast = PER_PAGE > list.size
//        }.doOnTerminate { isLoadingSubject.onNext(false) }
//    }


//}


