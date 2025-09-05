package com.rajotiyapawan.pokedex.domain.model

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class PokemonData(
    val id: Int?,
    val name: String?,
    val imageUrl: String?,
    val types: List<PokeType>,
    val weight: Int?,
    val height: Int?,
    val baseExperience: Int?,
    val abilities: List<Ability>?,
    val species: NameUrlItem?,
    val stats: List<Stats>?
)
