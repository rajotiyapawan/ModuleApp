package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EggGroupDetailDto(
    val id: Int,
    val name: String,
    @SerializedName("pokemon_species") val pokemonSpecies: List<NameUrlDto>,
)
