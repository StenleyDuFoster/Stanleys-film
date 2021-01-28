package com.stenleone.stanleysfilm.network.entity.tmdb_auth

import com.google.gson.annotations.SerializedName
import com.stenleone.stanleysfilm.interfaces.model.Network

data class GuestSessionEntity(
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("guest_session_id")
    val guestSessionId: String,
    val success: Boolean
): Network