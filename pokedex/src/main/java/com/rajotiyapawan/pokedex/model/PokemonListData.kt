package com.rajotiyapawan.pokedex.model

import com.rajotiyapawan.pokedex.domain.model.NameUrlItem

data class PokemonListData(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NameUrlItem>?
)

