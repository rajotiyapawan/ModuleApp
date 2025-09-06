package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class EvolutionChainDto(
    val chain: ChainDto?,
    val id: Int?
) {
    data class ChainDto(
        @SerializedName("evolution_details")
        val evolutionDetails: List<EvolutionDetailDto>,

        @SerializedName("evolves_to")
        val evolvesTo: List<ChainDto>?,

        @SerializedName("is_baby")
        val isBaby: Boolean,

        @SerializedName("species")
        val species: NameUrlDto
    )


    data class EvolutionDetailDto(
        @SerializedName("gender")
        val gender: Any?,

        @SerializedName("held_item")
        val heldItem: Any?,

        @SerializedName("item")
        val item: NameUrlDto?,

        @SerializedName("known_move")
        val knownMove: Any?,

        @SerializedName("known_move_type")
        val knownMoveType: Any?,

        @SerializedName("location")
        val location: Any?,

        @SerializedName("min_affection")
        val minAffection: Any?,

        @SerializedName("min_beauty")
        val minBeauty: Any?,

        @SerializedName("min_happiness")
        val minHappiness: Any?,

        @SerializedName("min_level")
        val minLevel: Int?,

        @SerializedName("needs_overworld_rain")
        val needsOverworldRain: Boolean?,

        @SerializedName("party_species")
        val partySpecies: Any?,

        @SerializedName("party_type")
        val partyType: Any?,

        @SerializedName("relative_physical_stats")
        val relativePhysicalStats: Any?,

        @SerializedName("time_of_day")
        val timeOfDay: String?,

        @SerializedName("trade_species")
        val tradeSpecies: Any?,

        @SerializedName("trigger")
        val trigger: NameUrlDto,

        @SerializedName("turn_upside_down")
        val turnUpsideDown: Boolean?
    )
}
