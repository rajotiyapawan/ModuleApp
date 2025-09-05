package com.rajotiyapawan.pokedex.data.remote.dto

import com.rajotiyapawan.pokedex.domain.model.NameUrlItem

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class EvolutionChainDto(
    val baby_trigger_item: Any?,
    val chain: ChainDto?,
    val id: Int?
) {
    data class ChainDto(
        val evolution_details: List<EvolutionDetailDto>,
        val evolves_to: List<ChainDto>?,
        val is_baby: Boolean,
        val species: NameUrlItem
    )


    data class EvolutionDetailDto(
        val gender: Any,
        val held_item: Any,
        val item: Any,
        val known_move: Any,
        val known_move_type: Any,
        val location: Any,
        val min_affection: Any,
        val min_beauty: Any,
        val min_happiness: Any,
        val min_level: Int,
        val needs_overworld_rain: Boolean,
        val party_species: Any,
        val party_type: Any,
        val relative_physical_stats: Any,
        val time_of_day: String,
        val trade_species: Any,
        val trigger: NameUrlDto,
        val turn_upside_down: Boolean
    )
}
