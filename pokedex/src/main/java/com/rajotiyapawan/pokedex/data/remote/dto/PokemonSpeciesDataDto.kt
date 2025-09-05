package com.rajotiyapawan.pokedex.data.remote.dto

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class PokemonSpeciesDataDto(
    val flavor_text_entries: List<FlavourEntryDto>?,
    val genera: List<Genus>,
    val gender_rate: Int,
    val base_happiness: Int,
    val order: Int,
    val capture_rate: Int,
    val hatch_counter: Int,
    val is_baby: Boolean,
    val is_legendary: Boolean,
    val is_mythical: Boolean,
    val egg_groups: List<NameUrlDto>?,
    val evolution_chain: NameUrlDto?,
    val growth_rate: NameUrlDto,
    val habitat: NameUrlDto,
    val shape: NameUrlDto,
    val pokedex_numbers: List<PokemonNumber>,
    val varieties: List<VarietyDto>
) {
    data class Genus(
        val genus: String,
        val language: NameUrlDto
    )

    data class PokemonNumber(
        val entry_number: Int,
        val pokedex: NameUrlDto,
    )

    data class VarietyDto(
        val is_default: Boolean,
        val pokemon: NameUrlDto
    )

}
