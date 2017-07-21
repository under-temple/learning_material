package com.example.shokiterashita.learningmaterial.views.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.views.fragments.LearningMaterialTestFragment
import com.example.shokiterashita.learningmaterial.views.lib.manager.extention.replaceFragment

class MainActivity : AppCompatActivity() {

    internal var learningMaterial: LearningMaterialTestFragment = LearningMaterialTestFragment()

    val fragmentManager = supportFragmentManager

    lateinit var button:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.toFragmentButton) as Button
        button.setOnClickListener {
            this.showFragment()
        }
    }

    override fun onStart() {
        super.onStart()
    }


    fun showFragment() {

        this.replaceFragment(R.id.content_layout, learningMaterial)

    }

}
