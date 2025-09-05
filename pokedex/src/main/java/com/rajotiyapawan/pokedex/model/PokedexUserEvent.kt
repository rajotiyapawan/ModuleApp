package com.rajotiyapawan.pokedex.model

import com.rajotiyapawan.pokedex.domain.model.NameUrlItem

sealed class PokedexUserEvent {
    data object BackBtnClicked : PokedexUserEvent()
    data class OpenDetail(val poekmon: NameUrlItem) : PokedexUserEvent()
}