package com.rajotiyapawan.pokedex.data.remote.dto

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class PokemonListDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NameUrlDto>?
)
