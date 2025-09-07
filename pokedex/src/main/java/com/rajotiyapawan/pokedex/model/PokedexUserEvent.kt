package com.rajotiyapawan.pokedex.model

import com.rajotiyapawan.pokedex.domain.model.NameUrlItem

sealed class PokedexUserEvent {
    data object BackBtnClicked : PokedexUserEvent()
    data class OpenDetail(val pokemon: NameUrlItem) : PokedexUserEvent()
    data class OpenAbilityList(val abilityName: String) : PokedexUserEvent()
    data class OpenEggGroupList(val eggGroup: String) : PokedexUserEvent()
}