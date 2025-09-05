package com.rajotiyapawan.pokedex.domain.usecase

import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.repository.PokemonRepository

/**
 * Created by Pawan Rajotiya on 05-09-2025.
 */
class GetPokemonBasicInfoUseCase(private val repository: PokemonRepository) {
    operator fun invoke(params: RequestModel) = repository.getPokemonBasicDetails(params)
}
