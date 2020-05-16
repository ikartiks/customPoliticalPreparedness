package com.yrdtechnologies.adapters

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by kartikshah on 11/02/18.
 */
class CustomSimpleItemAnimator(var adapter: RecyclerView.Adapter<*>) : SimpleItemAnimator(), Animator.AnimatorListener {
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val anim = ObjectAnimator.ofFloat(holder.itemView, "translationX", 0f, 400f)
        anim.duration = 100
        anim.target = holder.itemView
        anim.interpolator = LinearInterpolator()
        anim.start()
        anim.addListener(this)
        return false
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val anim = ObjectAnimator.ofFloat(holder.itemView, "translationY", -holder.itemView.height.toFloat(), 0f)
        anim.duration = 500
        anim.interpolator = LinearInterpolator()
        anim.start()
        return false
    }

    override fun animateMove(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        if (fromY == toY) return false
        val animStart = -(toY - fromY)
        val anim = ObjectAnimator.ofFloat(holder.itemView, "translationY", animStart.toFloat(), 0f)
        anim.duration = 500
        anim.interpolator = LinearInterpolator()
        anim.start()
        dispatchMoveFinished(holder)
        return true
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, fromLeft: Int, fromTop: Int, toLeft: Int, toTop: Int): Boolean {
        dispatchChangeFinished(oldHolder, true)
        dispatchChangeFinished(newHolder, false)
        return false
    }

    override fun runPendingAnimations() {}
    override fun endAnimation(item: RecyclerView.ViewHolder) {}
    override fun endAnimations() {}
    override fun isRunning(): Boolean {
        return false
    }

    override fun onAnimationStart(animation: Animator) {}
    override fun onAnimationEnd(animation: Animator) {
        val objectAnimator = animation as ObjectAnimator
        val view = objectAnimator.target as View?
        if (view != null) view.visibility = View.GONE
    }

    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}

}