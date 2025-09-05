package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class GameIndexDto(
    @SerializedName("game_index") var gameIndex: Int? = null,
    @SerializedName("version") var version: NameUrlDto? = null
)
