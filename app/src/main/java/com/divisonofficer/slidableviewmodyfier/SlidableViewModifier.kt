package com.divisonofficer.slidableviewmodyfier

import android.animation.ObjectAnimator
import android.animation.TimeAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View

class SlidableViewModifier {
    val TAG = "Modifier"
    private var onMoving = false




    private var minHeight = 0
    private var maxHeight = 1000

    private var touchYpositionGap = 0

    private lateinit var view : View

    private var topTouchableRatio = 0.2f

    /**
     * 얼마만큼 열면 완전히 열리는지
     */
    private var gestureOpenHeightRatio = 0.25f

    fun setView(view : View) : SlidableViewModifier
    {
        this.view = view
        this.minHeight = view.height
        return this
    }

    fun setMaxHeight(height : Int) : SlidableViewModifier
    {
        maxHeight = height
        return this
    }
    fun setMinHeight(height : Int) : SlidableViewModifier{
        minHeight = height
        return this
    }
    fun setHeadRatio(ratio: Double) : SlidableViewModifier{
        topTouchableRatio = ratio.toFloat()
        return this
    }
    fun setOnSlideRatioChangeListener(listener : (Float) -> Unit) : SlidableViewModifier
    {
        onSlideChangeListener = listener
        return this
    }
    fun activate() : SlidableViewModifier
    {

        val param = view.layoutParams
        param.height = minHeight
        view.layoutParams = param
        setThouchListener()

        return this

    }
    private fun Int.cy() : Int{
        return this - view.y.toInt()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setThouchListener(){
        Log.d(TAG,"Attach Listener")
        Log.d(TAG,"Max height : ${maxHeight}, Min height : ${minHeight}")
        view.setOnTouchListener { view, motionEvent ->

            Log.d(TAG,"ontouch : ${motionEvent.action}")
            when(motionEvent.action)
            {
                MotionEvent.ACTION_DOWN ->{
                    Log.d(TAG,"rawY : ${motionEvent.y} thisViewheihgt : ${this.view.height} ${this.view.height * topTouchableRatio} ")
                    if(motionEvent.y < this.view.height * topTouchableRatio)
                    {
                        onMoving = true
                        touchYpositionGap = motionEvent.y.toInt()

                        return@setOnTouchListener true
                    }
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE ->
                {
                    if (onMoving) {
                        var toHeight = this.view.height + touchYpositionGap - motionEvent.y.toInt()
                        if (toHeight > maxHeight) toHeight = maxHeight
                        if (toHeight < minHeight) toHeight = minHeight
                        Log.d(
                            TAG,
                            "rawY : ${motionEvent.y}, currentHeihgt: ${this.view.height}newHeight : ${toHeight}"
                        )
                        val param = this.view.layoutParams
                        param.height = toHeight
                        setRatio(toHeight.toFloat())
                        this.view.layoutParams = param
                        return@setOnTouchListener false
                    }
                }
                MotionEvent.ACTION_UP->{
                    onMoving = false
                    animateHeight(this.view.height)
                    return@setOnTouchListener false
                }


            }


            true
        }

    }
    var slideRatio = 0f
    private fun setRatio(currentHeight: Float)
    {
        slideRatio = ((currentHeight.toFloat() - minHeight) / (maxHeight - minHeight))
        onSlideChangeListener(slideRatio)
    }
    var onSlideChangeListener : (Float)->Unit = {_ ->}

    fun openView()
    {
        animatePreHeight = view.height.toFloat()
        openAnimator.start()
        isOpened = true
    }
    fun closeView()
    {
        animatePreHeight = view.height.toFloat()
        closeAnimator.start()
        isOpened = false
    }

    private fun animateHeight(currentHeight: Int)
    {
        val ratio = slideRatio


        if (isOpened)
        {
            if(ratio < 1f-gestureOpenHeightRatio)
            {
                closeView()
            }
            else{
                openView()
            }
        }
        else{
            if( ratio > gestureOpenHeightRatio)
            {
                openView()
            }
            else
            {
                closeView()
            }

        }

    }
    var isOpened = false

    var animatePreHeight = 0f
    private val openAnimator = TimeAnimator.ofFloat(0f,1f).apply{
        addUpdateListener {
            val newHeight =
               animatePreHeight + (maxHeight - animatePreHeight) * it.animatedFraction
            val param = view.layoutParams
            param.height = newHeight.toInt()
            view.layoutParams = param
            setRatio(newHeight)
        }
        duration = 300
    }
    private val closeAnimator = TimeAnimator.ofFloat(0f,1f).apply{
        addUpdateListener {
            val newHeight =
            animatePreHeight + (minHeight - animatePreHeight) * it.animatedFraction
            val param = view.layoutParams
            param.height = newHeight.toInt()
            view.layoutParams = param
            setRatio(newHeight)
        }
        duration = 300
    }
}