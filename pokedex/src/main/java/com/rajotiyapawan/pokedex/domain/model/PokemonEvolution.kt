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
    val min_level: Int,
    val needs_overworld_rain: Boolean,
    val time_of_day: String,
    val trigger: NameUrlItem,
    val turn_upside_down: Boolean
)