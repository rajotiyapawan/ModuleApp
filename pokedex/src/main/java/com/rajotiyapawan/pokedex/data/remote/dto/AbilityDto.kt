package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class AbilityDto(
    @SerializedName("ability") var ability: NameUrlDto? = null,
    @SerializedName("is_hidden") var isHidden: Boolean? = null,
    @SerializedName("slot") var slot: Int? = null
)
