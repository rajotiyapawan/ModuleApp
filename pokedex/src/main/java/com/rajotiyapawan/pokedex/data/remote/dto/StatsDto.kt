package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class StatsDto(
    @SerializedName("base_stat") val baseStat: Int?,
    @SerializedName("effort") val effort: Int?,
    @SerializedName("stat") val stat: NameUrlDto?
)
