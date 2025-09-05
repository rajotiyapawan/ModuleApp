package com.rajotiyapawan.pokedex.domain.repository

import com.rajotiyapawan.network.ApiResponse
import com.rajotiyapawan.pokedex.domain.model.AbilityDetails
import com.rajotiyapawan.pokedex.domain.model.EvolutionChain
import com.rajotiyapawan.pokedex.domain.model.PokemonBasicInfo
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.domain.model.PokemonListData
import com.rajotiyapawan.pokedex.domain.model.PokemonSpeciesData
import com.rajotiyapawan.pokedex.domain.model.RequestModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(params: RequestModel): Flow<ApiResponse<PokemonListData>>
    fun getPokemonDetails(params: RequestModel): Flow<ApiResponse<PokemonData>>
    fun getPokemonBasicDetails(params: RequestModel): Flow<ApiResponse<PokemonBasicInfo>>
    fun getPokemonSpeciesData(params: RequestModel): Flow<ApiResponse<PokemonSpeciesData>>
    fun getAbilityDetails(params: RequestModel): Flow<ApiResponse<AbilityDetails>>
    fun getEvolutionDetails(params: RequestModel): Flow<ApiResponse<EvolutionChain>>
}