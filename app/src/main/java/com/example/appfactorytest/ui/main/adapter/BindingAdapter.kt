package com.example.appfactorytest.ui.main.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Track

@BindingAdapter("loadImageView")
fun loadImageView(imageView: ImageView, url: String) {

    Glide.with(imageView.context).load(url).error(R.drawable.ic_baseline_highlight_off_24)
        .placeholder(R.drawable.ic_baseline_highlight_off_24).into(imageView)

}

@BindingAdapter("loadText")
fun loadText(textView: TextView, track: Track) {

    var long = track.duration

    val mns = long / 60
    val scs = long % 60

    textView.text = "$mns minutes, $scs seconds"

}
