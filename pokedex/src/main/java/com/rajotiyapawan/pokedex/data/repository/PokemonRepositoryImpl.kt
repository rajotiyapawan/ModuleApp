package com.rajotiyapawan.pokedex.data.repository

import com.rajotiyapawan.network.ApiResponse
import com.rajotiyapawan.network.NetworkRepository
import com.rajotiyapawan.network.POKE_BaseUrl
import com.rajotiyapawan.pokedex.data.mapper.toBasicInfo
import com.rajotiyapawan.pokedex.data.mapper.toDomain
import com.rajotiyapawan.pokedex.data.remote.dto.AbilityDetailDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonDataDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonListDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonSpeciesDataDto
import com.rajotiyapawan.pokedex.domain.model.AbilityDetails
import com.rajotiyapawan.pokedex.domain.model.PokemonBasicInfo
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.repository.PokemonRepository
import com.rajotiyapawan.pokedex.model.PokemonListData
import com.rajotiyapawan.pokedex.model.PokemonSpeciesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepositoryImpl : PokemonRepository {
    override fun getPokemonList(params: RequestModel): Flow<ApiResponse<PokemonListData>> = flow {
        val response =
            NetworkRepository.get<PokemonListDto>("${POKE_BaseUrl}pokemon?offset=${params.offset}&limit=${params.limit}")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success<PokemonListDto> -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getPokemonDetails(params: RequestModel): Flow<ApiResponse<PokemonData>> = flow {
        val url = if (params.url?.isNotEmpty() == true) {
            params.url
        } else {
            "${POKE_BaseUrl}pokemon/${params.name}"
        }
        val response = NetworkRepository.get<PokemonDataDto>(url)
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success<PokemonDataDto> -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getPokemonSpeciesData(params: RequestModel): Flow<ApiResponse<PokemonSpeciesData>> = flow {
        val response = NetworkRepository.get<PokemonSpeciesDataDto>(params.url ?: "")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success<PokemonSpeciesDataDto> -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getPokemonBasicDetails(params: RequestModel): Flow<ApiResponse<PokemonBasicInfo>> = flow {
        val url = if (params.url?.isNotEmpty() == true) {
            params.url
        } else {
            "${POKE_BaseUrl}pokemon/${params.name}"
        }
        val response = NetworkRepository.get<PokemonDataDto>(url)
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success<PokemonDataDto> -> emit(ApiResponse.Success(response.data.toBasicInfo()))
        }
    }

    override fun getAbilityDetails(params: RequestModel): Flow<ApiResponse<AbilityDetails>> = flow {
        val response = NetworkRepository.get<AbilityDetailDto>(params.url ?: "")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success<AbilityDetailDto> -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

}