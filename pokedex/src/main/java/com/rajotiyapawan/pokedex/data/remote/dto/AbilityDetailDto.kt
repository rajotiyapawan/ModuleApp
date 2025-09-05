package com.rajotiyapawan.pokedex.data.remote.dto

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class AbilityDetailDto(
    val effect_entries: List<AbilityEffectDto>,
    val flavor_text_entries: List<FlavourEntryDto>,
    val is_main_series: Boolean,
    val id: Int,
    val name: String,
    val pokemon: List<AbilityHolders>
) {
    data class AbilityEffectDto(
        val effect: String,
        val short_effect: String,
        val flavor_text: String?,
        val language: NameUrlDto?
    )

    data class AbilityHolders(
        val is_hiddern: Boolean,
        val pokemon: NameUrlDto,
        val slot: Int
    )
}
