package com.rajotiyapawan.pokedex.domain.model

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class Ability(
    val ability: NameUrlItem? = null,
    val isHidden: Boolean,
    val slot: Int? = null
)
