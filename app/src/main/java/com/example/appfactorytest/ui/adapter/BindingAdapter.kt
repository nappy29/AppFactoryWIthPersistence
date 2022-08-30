package com.example.appfactorytest.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.appfactorytest.R
import java.math.RoundingMode
import java.text.DecimalFormat

@BindingAdapter("loadImageView")
fun loadImageView(imageView: ImageView, url: String){

        Glide.with(imageView.context).load(url).override(200, 200).
            placeholder(R.drawable.ic_launcher_background).into(imageView)

}