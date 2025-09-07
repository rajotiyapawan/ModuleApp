package com.rajotiyapawan.pokedex.domain.model

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class AbilityDetails(
    val effectEntries: List<AbilityEffect>,
    val flavorTextEntries: List<FlavourEntry>,
    val pokemon: List<AbilityHolders>
) {
    data class AbilityEffect(
        val effect: String,
        val shortEffect: String,
        val flavorText: String?,
        val language: NameUrlItem?,
        val pokemon: List<AbilityHolders>?
    )

    data class AbilityHolders(
        val isHidden: Boolean,
        val pokemon: NameUrlItem,
        val slot: Int
    )
}
