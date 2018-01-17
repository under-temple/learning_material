package com.example.shokiterashita.learningmaterial.views.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shokiterashita.learningmaterial.R
import com.example.shokiterashita.learningmaterial.lib.extention.animateVerticalBounce

/**
 * Created by shokiterashita on 2017/11/10.
 */
class BlackSheetFragment : Fragment() {

    lateinit var countDownTimerTextView: TextView
    lateinit var finishTextView: TextView
    lateinit var roundView: View

    interface CountDownListner{
        fun onFinish()
    }

    var listner:CountDownListner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_black_sheet, container, false)
        var isStart = arguments.getBoolean("isStart")


        countDownTimerTextView = view.findViewById(R.id.count_down_timer_text_view)
        finishTextView = view.findViewById(R.id.finish_text_view)
        roundView = view.findViewById(R.id.round_view)

        if (isStart){
            finishTextView.visibility = View.INVISIBLE

            object : CountDownTimer(3800, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countDownTimerTextView.text = (millisUntilFinished / 1000).toString()
                }
                override fun onFinish() {
                    countDownTimerTextView.visibility = View.INVISIBLE
                    fragmentManager.popBackStack()
                    if (isStart){
                        listner?.onFinish()
                    }
                }
            }.start()

        } else {
            finishTextView.animateVerticalBounce()
            countDownTimerTextView.visibility = View.INVISIBLE
            roundView.visibility = View.INVISIBLE

            object : CountDownTimer(2500, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countDownTimerTextView.text = (millisUntilFinished / 1000).toString()
                }
                override fun onFinish() {
                    countDownTimerTextView.visibility = View.INVISIBLE
                    fragmentManager.popBackStack()

                    if (isStart){
                        listner?.onFinish()
                    }

                }
            }.start()
        }
        return view
    }

}
