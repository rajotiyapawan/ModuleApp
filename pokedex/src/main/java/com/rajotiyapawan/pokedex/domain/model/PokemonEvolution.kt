package com.rajotiyapawan.pokedex.domain.model

data class EvolutionChain(
    val chain: Chain?
)

data class Chain(
    val evolvesTo: List<Chain>,
    val species: NameUrlItem,
    val evolutionDetails: List<EvolutionDetail>
)

data class EvolutionDetail(
    val minLevel: Int,
    val minHappiness: Int,
    val minAffection: Int,
    val minBeauty: Int,
    val needsOverworldRain: Boolean,
    val timeOfDay: String,
    val gender: String,
    val trigger: NameUrlItem,
    val turnUpsideDown: Boolean,
    val item: NameUrlItem?,
    val location: NameUrlItem?,
    val knownMoveType: NameUrlItem?,
    val knownMove: NameUrlItem?,
    val tradeSpecies: NameUrlItem?,
    val heldItem: NameUrlItem?,
    val partySpecies: NameUrlItem?,
    val partyType: NameUrlItem?,
    val relativePhysicalStats: Int?,
)