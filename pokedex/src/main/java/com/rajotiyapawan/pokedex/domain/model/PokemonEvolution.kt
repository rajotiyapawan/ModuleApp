package com.rajotiyapawan.pokedex.domain.model

import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar

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
    val needsOverworldRain: Boolean,
    val timeOfDay: String,
    val trigger: NameUrlItem,
    val turnUpsideDown: Boolean,
    val item: NameUrlItem?
) {
    fun getRequirementText(): String {
        return when {
            minLevel != 0 -> "Lvl $minLevel"
            item != null -> "Use ${item.name?.capitalizeFirstChar()}"
            trigger.name == "trade" -> "Trade"
            trigger.name == "level-up" && timeOfDay == "day" -> "Lvl up (Day)"
            trigger.name == "level-up" && timeOfDay == "night" -> "Lvl up (Night)"
            else -> trigger.name?.capitalizeFirstChar() ?: "Special"
        }
    }

}