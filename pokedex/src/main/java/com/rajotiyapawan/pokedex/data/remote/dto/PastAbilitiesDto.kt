package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class PastAbilitiesDto(
    @SerializedName("abilities") var abilities: ArrayList<AbilityDto>? = null,
    @SerializedName("generation") var generation: NameUrlDto? = null
)
