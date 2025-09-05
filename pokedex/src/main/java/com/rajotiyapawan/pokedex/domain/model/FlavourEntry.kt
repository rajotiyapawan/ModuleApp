package com.rajotiyapawan.pokedex.domain.model

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class FlavourEntry(
    val flavor_text: String,
    val language: NameUrlItem,
    val version: NameUrlItem?,
    val version_group: NameUrlItem,
)
