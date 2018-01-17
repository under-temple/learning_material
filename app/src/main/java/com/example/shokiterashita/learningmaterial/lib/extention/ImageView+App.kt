package com.example.shokiterashita.learningmaterial.lib.extention

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.media.Image
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

/**
 * Created by shokiterashita on 2017/12/07.
 */


fun ImageView.animateAlpha(){

    val accelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
    var handler = Handler()
    val alphaShow = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f)
    val alphaHide = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)

    val showAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaShow)
    val hideAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alphaHide)

    showAnimator.duration = 300
    showAnimator.interpolator = accelerateDecelerateInterpolator
    showAnimator.start()
//    isImageViewAnimated = true
    handler.postDelayed({
        hideAnimator.duration = 50
        hideAnimator.start()
//        isImageViewAnimated = false
    }, 950)
}


