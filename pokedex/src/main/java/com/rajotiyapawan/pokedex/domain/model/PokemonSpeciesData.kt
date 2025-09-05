package com.rajotiyapawan.pokedex.domain.model

data class PokemonSpeciesData(
    val flavourText: String,
    val genus: String,
    val growthRate: String,
    val femalePercentage: Double,
    val malePercentage: Double,
    val baseFriendship: Int,
    val hatchCounter: Int,
    val eggGroups: List<NameUrlItem>?,
    val evolutionChain: NameUrlItem?,
    val varieties: List<Variety>
) {
    data class Variety(
        val is_default: Boolean,
        val pokemon: NameUrlItem
    )

    companion object {
        fun init(): PokemonSpeciesData {
            return PokemonSpeciesData("", "", "", 0.0, 0.0, 0, 0, listOf(), NameUrlItem(), listOf())
        }
    }
}