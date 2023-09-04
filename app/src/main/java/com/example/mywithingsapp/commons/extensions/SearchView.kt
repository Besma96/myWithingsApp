package com.example.mywithingsapp.commons.extensions

import android.animation.Animator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import com.example.mywithingsapp.R
import kotlinx.android.synthetic.main.view_search.view.*

class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val duration: Long = 300
    init {
        LayoutInflater.from(context).inflate(R.layout.view_search, this, true)

        open_search_button.setOnClickListener { openSearch() }
        close_search_button.setOnClickListener { closeSearch() }
    }

    private fun openSearch() {
        search_input_text.setText("")
        search_open_view.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val circularReveal = ViewAnimationUtils.createCircularReveal(
                search_open_view,
                (open_search_button.right + open_search_button.left) / 2,
                (open_search_button.top + open_search_button.bottom) / 2,
                0f, width.toFloat()
            )
            circularReveal.duration = duration
            circularReveal.start()
        }
    }

    private fun closeSearch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val circularConceal = ViewAnimationUtils.createCircularReveal(
                search_open_view,
                (open_search_button.right + open_search_button.left) / 2,
                (open_search_button.top + open_search_button.bottom) / 2,
                width.toFloat(), 0f
            )

            circularConceal.duration = duration
            circularConceal.start()
            circularConceal.addListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(p0: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(p0: Animator) {
                    search_open_view.visibility = View.GONE
                    search_input_text.setText("")
                    circularConceal.removeAllListeners()
                }

                override fun onAnimationCancel(p0: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(p0: Animator) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            search_open_view.visibility = View.GONE
            search_input_text.setText("")
        }
    }
}
