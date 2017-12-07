package com.example.shokiterashita.learningmaterial.lib.extention

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Handler
import android.view.animation.OvershootInterpolator
import android.widget.TextView

/**
 * Created by shokiterashita on 2017/11/10.
 */


fun TextView.animateShake(){

    var handler = Handler()
    val alphaShow = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f)
    val alphaHide = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)

    val animX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 2.0f)
    val animY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 2.0f)

    val showAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaShow)
    val hideAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaHide)
    val expandingAnimator = ObjectAnimator.ofPropertyValuesHolder(this, animX, animY)
    val interpolator = OvershootInterpolator()

    showAnimator.duration = 300
    expandingAnimator.duration = 1000
    showAnimator.interpolator = interpolator
    expandingAnimator.interpolator = interpolator

    showAnimator.start()
    expandingAnimator.start()

    handler.postDelayed({
        hideAnimator.duration = 700
        hideAnimator.start()
    }, 300)
}

fun TextView.animateScaleAndAlpha(){

    var handler = Handler()
    val alphaShow = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f)
    val alphaHide = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)

    val animX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 2.0f)
    val animY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 2.0f)

    val showAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaShow)
    val hideAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaHide)
    val expandingAnimator = ObjectAnimator.ofPropertyValuesHolder(this, animX, animY)

    val interpolator = OvershootInterpolator()
    showAnimator.duration = 300
    expandingAnimator.duration = 1000
    showAnimator.interpolator =  interpolator
    expandingAnimator.interpolator =  interpolator

    showAnimator.start()
    expandingAnimator.start()

    handler.postDelayed({
        hideAnimator.duration = 700
        hideAnimator.start()
    }, 300)

}


fun TextView.animateVerticalShake(){

    var handler = Handler()

//    val translationUp800Y = PropertyValuesHolder.ofFloat("translationY", 0.0f, 800f)
//    val translationUp400Y = PropertyValuesHolder.ofFloat("translationY", 0.0f, 400f)
//    val translationUp200Y = PropertyValuesHolder.ofFloat("translationY", 200f, 400f)
//    val translationUp100Y = PropertyValuesHolder.ofFloat("translationY", 300f, 400f)
//
//    val translationDown400Y = PropertyValuesHolder.ofFloat("translationY", 400f, 0.0f)
//    val translationDown200Y = PropertyValuesHolder.ofFloat("translationY", 400f, 200f)
//    val translationDown100Y = PropertyValuesHolder.ofFloat("translationY", 400f, 300f)
//    val absolute300Y = PropertyValuesHolder.ofFloat("translationY", 300f)

    val translationUp800Y = PropertyValuesHolder.ofFloat("translationY", 0.0f, 800f)
    val translationUp400Y = PropertyValuesHolder.ofFloat("translationY", 0.0f, 600f)
    val translationUp200Y = PropertyValuesHolder.ofFloat("translationY", 400f, 600f)
    val translationUp100Y = PropertyValuesHolder.ofFloat("translationY", 500f, 600f)

    val translationDown400Y = PropertyValuesHolder.ofFloat("translationY", 600f, 0.0f)
    val translationDown200Y = PropertyValuesHolder.ofFloat("translationY", 600f, 400f)
    val translationDown100Y = PropertyValuesHolder.ofFloat("translationY", 600f, 500f)
    val absolute300Y = PropertyValuesHolder.ofFloat("translationY", 500f)


    //拡大しつつ薄まる系
    val alphaShow = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f)
    val alphaHide = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)
    val animX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 2.0f)
    val animY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 2.0f)
    val showAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaShow)
    val hideAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaHide)
    val expandingAnimator = ObjectAnimator.ofPropertyValuesHolder(this, animX, animY)
    showAnimator.duration = 300
    hideAnimator.duration = 600
    expandingAnimator.duration = 600



    val translationUp800YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationUp800Y)
    val translationUp400YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationUp400Y)
    val translationUp200YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationUp200Y)
    val translationUp100YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationUp100Y)

    val translationDown400YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationDown400Y)
    val translationDown200YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationDown200Y)
    val translationDown100YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationDown100Y)

    val absolute300YAnimator = ObjectAnimator.ofPropertyValuesHolder(this, absolute300Y)


    translationUp400YAnimator.duration = 100
    translationUp400YAnimator.start()

    handler.postDelayed({
        translationDown200YAnimator.duration = 100
        translationDown200YAnimator.start()
    }, 100)
    handler.postDelayed({
        translationUp200YAnimator.duration = 100
        translationUp200YAnimator.start()
    }, 200)
    handler.postDelayed({
        translationDown100YAnimator.duration = 100
        translationDown100YAnimator.start()
    }, 300)

    handler.postDelayed({
        absolute300YAnimator.duration = 200
        absolute300YAnimator.start()
    }, 400)


    handler.postDelayed({
        hideAnimator.start()
        expandingAnimator.start()
    }, 1000)



}

