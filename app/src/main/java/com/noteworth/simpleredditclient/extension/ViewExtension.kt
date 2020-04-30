package com.noteworth.simpleredditclient.extension

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible

fun View.fadeIn() {
    val animation = AnimationUtils.loadAnimation(this.context, android.R.anim.fade_in)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationStart(animation: Animation?) {
            if (!this@fadeIn.isVisible) this@fadeIn.visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animation?) {
        }
    })

    this.startAnimation(animation)
}

fun View.fadeOut() {
    val animation = AnimationUtils.loadAnimation(this.context, android.R.anim.fade_out)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationStart(animation: Animation?) {
            if (!this@fadeOut.isVisible) this@fadeOut.visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animation?) {
            this@fadeOut.visibility = View.GONE
        }
    })

    this.startAnimation(animation)
}