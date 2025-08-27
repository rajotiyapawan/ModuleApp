package com.rajotiyapawan.pokedex.domain.usecase

import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    operator fun invoke(params: RequestModel) = repository.getPokemonList(params)
}