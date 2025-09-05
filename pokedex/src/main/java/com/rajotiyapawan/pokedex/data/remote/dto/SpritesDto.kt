package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class SpritesDto(
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("back_female") val backFemale: String?,
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("back_shiny_female") val backShinyFemale: String?,
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("front_shiny") val frontShiny: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String?,
    val other: OtherDto?,
//    val versions: PokeVersions?
) {
    data class OtherDto(
        @SerializedName("dream_world") val dreamWorld: DreamWorld?,
        val home: Home?,
        @SerializedName("official-artwork") val officialArtwork: OfficialArtwork?,
        val showDown: SpritesDto?
    )

    data class OfficialArtwork(
        @SerializedName("front_default") val frontDefault: String?,
        @SerializedName("front_shiny") val frontShiny: String?,
    )

    data class DreamWorld(
        @SerializedName("front_default") var frontDefault: String? = null,
        @SerializedName("front_female") var frontFemale: String? = null
    )

    data class Home(
        @SerializedName("front_default") var frontDefault: String? = null,
        @SerializedName("front_female") var frontFemale: String? = null,
        @SerializedName("front_shiny") var frontShiny: String? = null,
        @SerializedName("front_shiny_female") var frontShinyFemale: String? = null
    )
}
