package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDataDto(
    @SerializedName("abilities") var abilities: ArrayList<AbilityDto>? = null,
    @SerializedName("base_experience") var baseExperience: Int? = null,
    @SerializedName("cries") var cries: CriesDto? = null,
    @SerializedName("forms") var forms: ArrayList<NameUrlDto>?,
    @SerializedName("game_indices") var gameIndices: ArrayList<GameIndexDto>?,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("held_items") var heldItems: ArrayList<HeldItemDto>? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_default") var isDefault: Boolean? = null,
    @SerializedName("location_area_encounters") var locationAreaEncounters: String? = null,
    @SerializedName("moves") var moves: ArrayList<MovesDto>? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("order") var order: Int? = null,
    @SerializedName("past_abilities") var pastAbilities: ArrayList<PastAbilitiesDto>? = null,
    @SerializedName("past_types") var pastTypes: ArrayList<PastTypesDto>? = null,
    @SerializedName("species") var species: NameUrlDto? = null,
    @SerializedName("sprites") var sprites: SpritesDto? = null,
    @SerializedName("stats") var stats: ArrayList<StatsDto>? = null,
    @SerializedName("types") var types: ArrayList<PokeTypesDto>? = null,
    @SerializedName("weight") var weight: Int? = null
)
