package com.example.shokiterashita.learningmaterial.views.lib.manager.extention

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by shoki.terashita on 2017/07/21.
 */

fun AppCompatActivity.addFragment(id:Int, fragment: Fragment){
    if(supportFragmentManager.backStackEntryCount > 0 && supportFragmentManager.fragments.contains(fragment)){
        this.replaceFragment(id, fragment)
        return
    }
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(id, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}

fun AppCompatActivity.replaceFragmentWithStackId(id:Int, fragment: Fragment, stackId:String){
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.addToBackStack(stackId)
    transaction.commit()
}


fun AppCompatActivity.replaceFragment(id:Int, fragment: Fragment){
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}

fun AppCompatActivity.replaceFragmentNotStack(id:Int, fragment: Fragment){
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.commit()
}


fun AppCompatActivity.captureScreen(): Bitmap {
    val rootView = window.decorView.rootView
    val screenView = rootView.getRootView()
    screenView.setDrawingCacheEnabled(true)
    val bitmap = Bitmap.createBitmap(screenView.getDrawingCache())
    screenView.setDrawingCacheEnabled(false)
    return bitmap
}

fun AppCompatActivity.isOnline():Boolean{
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let{
        it.activeNetworkInfo?.let{ info ->
            return info.isConnected
        }
    }
    return false
}