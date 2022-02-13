package com.divisonofficer.slidableviewmodyfier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.divisonofficer.slidableviewmodyfier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind : ActivityMainBinding
    lateinit var modifier: SlidableViewModifier
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bind  = DataBindingUtil.setContentView(this,R.layout.activity_main)


        bind.apply{

            modifier = SlidableViewModifier().setView(myItem).setMaxHeight(1800).setMinHeight(1000).setHeadRatio(0.35).setOnSlideRatioChangeListener {
                imageView.alpha = (1-it)
                Log.d("MainActivity","${it} is alpha")
            }.activate()

            buttonbutton.setOnClickListener {
                if(modifier.isOpened) modifier.closeView()
                else modifier.openView()
            }
        }
    }
}