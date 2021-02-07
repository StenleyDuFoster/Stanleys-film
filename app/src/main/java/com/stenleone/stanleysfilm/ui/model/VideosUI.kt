package com.stenleone.stanleysfilm.ui.model

import com.stenleone.stanleysfilm.interfaces.model.UI

data class VideosUI(
    val id: Int,
    val results: ArrayList<VideosResultUI>
): UI