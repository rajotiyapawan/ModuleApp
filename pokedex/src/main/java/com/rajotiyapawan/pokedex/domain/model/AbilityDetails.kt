package com.rajotiyapawan.pokedex.domain.model

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class AbilityDetails(
    val effect_entries: List<AbilityEffect>,
    val flavor_text_entries: List<FlavourEntry>
) {
    data class AbilityEffect(
        val effect: String,
        val short_effect: String,
        val flavor_text: String?,
        val language: NameUrlItem?
    )
}
