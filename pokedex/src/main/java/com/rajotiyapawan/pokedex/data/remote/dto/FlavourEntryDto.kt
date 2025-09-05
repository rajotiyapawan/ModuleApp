package com.rajotiyapawan.pokedex.data.remote.dto

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class FlavourEntryDto(
    val flavor_text: String,
    val language: NameUrlDto,
    val version: NameUrlDto?,
    val version_group: NameUrlDto,
)
