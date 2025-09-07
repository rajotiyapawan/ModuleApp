package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class AbilityDetailDto(
    @SerializedName("effect_entries") val effectEntries: List<AbilityEffectDto>,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavourEntryDto>,
    @SerializedName("is_main_series") val isMainSeries: Boolean,
    val id: Int,
    val name: String,
    val pokemon: List<AbilityHoldersDto>
) {
    data class AbilityEffectDto(
        val effect: String,
        @SerializedName("short_effect") val shortEffect: String,
        @SerializedName("flavor_text") val flavorText: String?,
        val language: NameUrlDto?
    )

    data class AbilityHoldersDto(
        @SerializedName("is_hidden") val isHidden: Boolean,
        val pokemon: NameUrlDto,
        val slot: Int
    )
}
