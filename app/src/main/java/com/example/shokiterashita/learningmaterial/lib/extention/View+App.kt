package com.example.shokiterashita.learningmaterial.lib.extention

import android.view.KeyEvent
import android.view.View

/**
 * Created by shokiterashita on 2017/12/07.
 */


fun View.disableBackKey(){
    this.setFocusableInTouchMode(true)
    this.requestFocus()
    this.setOnKeyListener { v, keyCode, event ->
        return@setOnKeyListener (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP)
    }

}