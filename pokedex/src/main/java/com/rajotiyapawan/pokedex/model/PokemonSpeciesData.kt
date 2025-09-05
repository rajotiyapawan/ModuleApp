package com.rajotiyapawan.pokedex.model

import com.rajotiyapawan.pokedex.domain.model.NameUrlItem

data class PokemonSpeciesData(
    val flavourText: String,
    val genus: String,
    val growthRate: String,
    val femalePercentage: Double,
    val malePercentage: Double,
    val baseFriendship: Int,
    val hatchCounter: Int,
    val eggGroups: List<NameUrlItem>?,
    val evolutionChain: NameUrlItem?
) {
    companion object {
        fun init(): PokemonSpeciesData {
            return PokemonSpeciesData("", "", "", 0.0, 0.0, 0, 0, listOf(), NameUrlItem())
        }
    }
}
