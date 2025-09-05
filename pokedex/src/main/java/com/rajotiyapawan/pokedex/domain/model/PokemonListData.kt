package com.rajotiyapawan.pokedex.domain.model

data class PokemonListData(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NameUrlItem>?
)