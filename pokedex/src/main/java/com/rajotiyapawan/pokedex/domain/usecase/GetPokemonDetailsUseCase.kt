package com.rajotiyapawan.pokedex.domain.usecase

import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.repository.PokemonRepository

class GetPokemonDetailsUseCase(private val repository: PokemonRepository) {
    operator fun invoke(params: RequestModel) = repository.getPokemonDetails(params)
}