package com.rajotiyapawan.pokedex.domain.repository

import com.rajotiyapawan.network.ApiResponse
import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.model.PokemonListData
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(params: RequestModel): Flow<ApiResponse<PokemonListData>>
}