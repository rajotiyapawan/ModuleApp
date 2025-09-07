package com.rajotiyapawan.pokedex.data.repository

import com.rajotiyapawan.network.ApiResponse
import com.rajotiyapawan.network.NetworkRepository
import com.rajotiyapawan.network.POKE_BaseUrl
import com.rajotiyapawan.pokedex.data.mapper.toBasicInfo
import com.rajotiyapawan.pokedex.data.mapper.toDomain
import com.rajotiyapawan.pokedex.data.remote.dto.AbilityDetailDto
import com.rajotiyapawan.pokedex.data.remote.dto.EggGroupDetailDto
import com.rajotiyapawan.pokedex.data.remote.dto.EvolutionChainDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonDataDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonListDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonSpeciesDataDto
import com.rajotiyapawan.pokedex.domain.model.AbilityDetails
import com.rajotiyapawan.pokedex.domain.model.EggGroupDetail
import com.rajotiyapawan.pokedex.domain.model.EvolutionChain
import com.rajotiyapawan.pokedex.domain.model.PokemonBasicInfo
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.domain.model.PokemonListData
import com.rajotiyapawan.pokedex.domain.model.PokemonSpeciesData
import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepositoryImpl : PokemonRepository {
    override fun getPokemonList(params: RequestModel): Flow<ApiResponse<PokemonListData>> = flow {
        val response =
            NetworkRepository.getCall<PokemonListDto>("${POKE_BaseUrl}pokemon?offset=${params.offset}&limit=${params.limit}")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getPokemonDetails(params: RequestModel): Flow<ApiResponse<PokemonData>> = flow {
        val url = if (params.url?.isNotEmpty() == true) {
            params.url
        } else {
            "${POKE_BaseUrl}pokemon/${params.name}"
        }
        val response = NetworkRepository.getCall<PokemonDataDto>(url)
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getPokemonSpeciesData(params: RequestModel): Flow<ApiResponse<PokemonSpeciesData>> = flow {
        val response = NetworkRepository.getCall<PokemonSpeciesDataDto>(params.url ?: "")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getPokemonBasicDetails(params: RequestModel): Flow<ApiResponse<PokemonBasicInfo>> = flow {
        val url = if (params.url?.isNotEmpty() == true) {
            params.url
        } else {
            "${POKE_BaseUrl}pokemon/${params.name}"
        }
        val response = NetworkRepository.getCall<PokemonDataDto>(url)
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> emit(ApiResponse.Success(response.data.toBasicInfo()))
        }
    }

    override fun getAbilityDetails(params: RequestModel): Flow<ApiResponse<AbilityDetails>> = flow {
        val response = NetworkRepository.getCall<AbilityDetailDto>(params.url ?: "")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

    override fun getEvolutionDetails(params: RequestModel): Flow<ApiResponse<EvolutionChain>> = flow {
        val response = NetworkRepository.getCall<EvolutionChainDto>(params.url ?: "")
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> {
                // Collect every distinct species (name + url) in the chain
                val speciesPairs = collect(response.data.chain)
                val unique = speciesPairs.distinctBy { it.first } // distinct by base name
                // For each species node, fetch its species DTO (to get its varieties) in parallel
                val speciesVarietiesMap = mutableMapOf<String, List<String>>() // baseName -> varieties
                coroutineScope {
                    val deferred = unique.map { (baseName, url) ->
                        async {
                            val spResp = NetworkRepository.getCall<PokemonSpeciesDataDto>(url)
                            val varieties = if (spResp is ApiResponse.Success) {
                                spResp.data.varieties.mapNotNull { it.pokemon.name }
                            } else emptyList()
                            baseName to varieties
                        }
                    }
                    deferred.awaitAll().forEach { (k, v) -> speciesVarietiesMap[k] = v }
                }

                // Determine suffix from requested name
                val suffix = extractSuffix(params.currentPokemon)

                // Map chain -> domain using speciesVarietiesMap + suffix
                emit(ApiResponse.Success(response.data.toDomain(speciesVarietiesMap, suffix)))
            }
        }
    }

    fun extractSuffix(name: String): String? {
        val parts = name.split("-")
        return if (parts.size > 1) {
            "-" + parts.last()//drop(1).joinToString("-") // preserve multi-part suffix like "-hisui" or "-galar"
        } else null
    }

    fun collect(chain: EvolutionChainDto.ChainDto?): MutableList<Pair<String, String>> {
        val speciesPairs = mutableListOf<Pair<String, String>>()
        if (chain == null) return speciesPairs
        chain.species.url?.let { url ->
            speciesPairs.add(chain.species.name!! to url)
        }
        chain.evolvesTo?.forEach {
            speciesPairs.addAll(collect(it))
        }
        return speciesPairs
    }

    override fun getEggGroupPokemonList(params: RequestModel): Flow<ApiResponse<EggGroupDetail>> = flow {
        val response = NetworkRepository.getCall<EggGroupDetailDto>(POKE_BaseUrl + "egg-group/" + params.name)
        when (response) {
            is ApiResponse.Error -> emit(response)
            is ApiResponse.Success -> emit(ApiResponse.Success(response.data.toDomain()))
        }
    }

}