package com.rajotiyapawan.pokedex.presentation.navigation

import com.rajotiyapawan.pokedex.domain.model.NameUrlItem

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Detail : Routes("detail/{pokemon}") {
        fun createRoute(pokemon: NameUrlItem) =
            "detail/${pokemon.name}"
    }
    object AbilityList : Routes("ability_list/{abilityName}") {
        fun createRoute(abilityName: String) = "ability_list/$abilityName"
    }

    object EggGroupList : Routes("egg_group_list/{eggGroupName}") {
        fun createRoute(eggGroup: String) = "egg_group_list/$eggGroup"
    }
}