package com.rajotiyapawan.pokedex.data.repository

import com.rajotiyapawan.network.ApiResponse
import com.rajotiyapawan.network.NetworkRepository
import com.rajotiyapawan.network.POKE_BaseUrl
import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.repository.PokemonRepository
import com.rajotiyapawan.pokedex.model.PokemonListData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepositoryImpl : PokemonRepository {
    override fun getPokemonList(params: RequestModel): Flow<ApiResponse<PokemonListData>> = flow {
        val response = NetworkRepository.get<PokemonListData>("${POKE_BaseUrl}pokemon?offset=${params.offset}&limit=${params.limit}")
        emit(response)
    }

}