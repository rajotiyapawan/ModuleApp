package com.rajotiyapawan.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
data class MovesDto(
    @SerializedName("move") var move: NameUrlDto? = null,
    @SerializedName("version_group_details") var versionGroupDetails: ArrayList<VersionGroupDetails>? = null
) {
    data class VersionGroupDetails(
        @SerializedName("level_learned_at") var levelLearnedAt: Int? = null,
        @SerializedName("move_learn_method") var moveLearnMethod: NameUrlDto? = null,
        @SerializedName("order") var order: Int? = null,
        @SerializedName("version_group") var versionGroup: NameUrlDto? = null
    )
}
