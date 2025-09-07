package com.rajotiyapawan.pokedex.domain.model

data class EggGroupDetail(
    val id: Int,
    val name: String,
    val pokemonSpecies: List<NameUrlItem>,
)
