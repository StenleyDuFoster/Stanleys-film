package com.stenleone.stanleysfilm.ui.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.BASE_IMAGE_URL
import com.stenleone.stanleysfilm.util.glide.GlideApp

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {

    url?.let {
        GlideApp
            .with(view)
            .load("$BASE_IMAGE_URL$url")
            .into(view)
    }
}
