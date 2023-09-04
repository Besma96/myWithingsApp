package com.example.mywithingsapp.commons.extensions

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import com.example.mywithingsapp.R

@BindingAdapter(value = ["previewURL"])
fun previewURL(view: ImageView, imageUrl: String?){

    if(!TextUtils.isEmpty(imageUrl)) {
        view.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.place_holder)
            error(R.drawable.place_holder)
            memoryCachePolicy(CachePolicy.DISABLED)
        }
    }
}

@BindingAdapter(value = ["largeImageURL"])
fun largeImageURL(view: ImageView, imageUrl: String?){

    if(!TextUtils.isEmpty(imageUrl)) {
        view.load(imageUrl) {
            crossfade(true)
            scale(Scale.FILL)
            placeholder(R.drawable.place_holder)
            error(R.drawable.place_holder)
            memoryCachePolicy(CachePolicy.DISABLED)
        }
    }
}

