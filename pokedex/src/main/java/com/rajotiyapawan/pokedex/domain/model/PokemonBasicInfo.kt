package com.rajotiyapawan.pokedex.domain.model

data class PokemonBasicInfo(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokeType>
)
