package com.example.appfactorytest.ui.adapter

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.model.AlbumWithTracks
import com.example.appfactorytest.data.model.Track
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

@BindingAdapter("loadImageView")
fun loadImageView(imageView: ImageView, url: String){

        Glide.with(imageView.context).load(url).
            error(R.drawable.ic_launcher_background).
            placeholder(R.drawable.ic_launcher_background).into(imageView)

}

@BindingAdapter("loadImageViewSmall")
fun loadImageViewSmall(imageView: ImageView, url: String){

    Glide.with(imageView.context).load(url).override(200, 200).
    placeholder(R.drawable.ic_launcher_background).into(imageView)

}

@BindingAdapter("loadText")
fun loadText(textView: TextView, track: Track){

    var long = track.duration

    val mns = long / 60
    val scs = long % 60

    textView.text = "$mns minutes, $scs seconds"

}
