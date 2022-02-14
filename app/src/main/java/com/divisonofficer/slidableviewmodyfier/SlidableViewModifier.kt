package com.divisonofficer.slidableviewmodyfier

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeAnimator
import android.annotation.SuppressLint
import android.transition.Slide
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd

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
        this.minHeight = view.measuredHeight
        return this
    }

    /**
     * 뷰의 최대 높이
     */
    fun setMaxHeight(height : Int) : SlidableViewModifier
    {
        maxHeight = height
        return this
    }

    /**
     * 어느 정도 비율로 드래그 했을 때 자동으로 뷰가 열리고 닫치는지
     */
    fun setAutoSlideRatioLimit(ratio : Float) : SlidableViewModifier
    {
        gestureOpenHeightRatio = ratio
        return this
    }

    /**
     * 뷰의 최소 높이
     */
    fun setMinHeight(height : Int) : SlidableViewModifier{
        minHeight = height
        return this
    }

    /**
     * 슬라이드 제스쳐를 인식할 최상단 뷰의 범위
     */
    fun setHeadRatio(ratio: Double) : SlidableViewModifier{
        topTouchableRatio = ratio.toFloat()
        return this
    }

    /**
     * 슬라이드가 변할때 비율을 받아오는 콜백 함수
     */
    fun setOnSlideRatioChangeListener(listener : (Float) -> Unit) : SlidableViewModifier
    {
        onSlideChangeListener = listener
        return this
    }

    /**
     * 활성화
     */
    fun activate() : SlidableViewModifier
    {

        val param = view.layoutParams
        param.height = minHeight
        view.layoutParams = param
        setThouchListener()

        return this

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
                    animateHeight()
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

    /**
     * 자동으로 열기
     */
    fun openView()
    {
        animatePreHeight = view.height.toFloat()
        openAnimator.start()
        isOpened = true
    }

    /**
     * 자동으로 닫기
     */
    fun closeView()
    {
        animatePreHeight = view.height.toFloat()
        closeAnimator.start()
        isOpened = false
    }

    /**
     * 터치 제스처 손을 땔 경우, 뷰를 어느 한 방향으로 이동시킵니다.
     */
    private fun animateHeight()
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
        doOnEnd {  onEndOfOpen(it)}
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
        doOnEnd {  onEndOfClose(it)}
    }

    private var onEndOfOpen : (Animator)->Unit = {_ ->}

    fun setOnEndOfOpenListener(listener : (Animator)->Unit) : SlidableViewModifier
    {
        onEndOfOpen = listener
        return this
    }
    private var onEndOfClose : (Animator)->Unit = {_ ->}
    fun setOnEndOfCloseListener(listener : (Animator)->Unit) : SlidableViewModifier
    {
        onEndOfClose = listener
        return this
    }


}