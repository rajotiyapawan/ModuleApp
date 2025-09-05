package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class CriesDto(
    @SerializedName("latest") var latest: String? = null,
    @SerializedName("legacy") var legacy: String? = null
)
