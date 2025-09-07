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
        val gender: String?,

        @SerializedName("held_item")
        val heldItem: NameUrlDto?,

        @SerializedName("item")
        val item: NameUrlDto?,

        @SerializedName("known_move")
        val knownMove: NameUrlDto?,

        @SerializedName("known_move_type")
        val knownMoveType: NameUrlDto?,

        @SerializedName("location")
        val location: NameUrlDto?,

        @SerializedName("min_affection")
        val minAffection: Int?,

        @SerializedName("min_beauty")
        val minBeauty: Int?,

        @SerializedName("min_happiness")
        val minHappiness: Int?,

        @SerializedName("min_level")
        val minLevel: Int?,

        @SerializedName("needs_overworld_rain")
        val needsOverworldRain: Boolean?,

        @SerializedName("party_species")
        val partySpecies: NameUrlDto?,

        @SerializedName("party_type")
        val partyType: NameUrlDto?,

        @SerializedName("relative_physical_stats")
        val relativePhysicalStats: Int?,

        @SerializedName("time_of_day")
        val timeOfDay: String?,

        @SerializedName("trade_species")
        val tradeSpecies: NameUrlDto?,

        @SerializedName("trigger")
        val trigger: NameUrlDto,

        @SerializedName("turn_upside_down")
        val turnUpsideDown: Boolean?
    )
}
