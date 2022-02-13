package com.divisonofficer.slidableviewmodyfier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.divisonofficer.slidableviewmodyfier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bind  = DataBindingUtil.setContentView(this,R.layout.activity_main)


        bind.apply{

            SlidableViewModifier().setView(myItem).setMaxHeight(1000).setMinHeight(300).activate()
        }
    }
}