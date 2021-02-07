package com.stenleone.stanleysfilm.ui.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.BASE_IMAGE_URL
import com.stenleone.stanleysfilm.util.glide.GlideApp
import com.stenleone.stanleysfilm.util.glide.GlideModule

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {

    url?.let {
        GlideApp
            .with(view)
            .load("$BASE_IMAGE_URL$url")
            .transition(DrawableTransitionOptions.withCrossFade(GlideModule.IMAGE_ANIM_DURUTATION))
            .into(view)
    }
}
