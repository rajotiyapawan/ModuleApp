package com.rajotiyapawan.pokedex.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rajotiyapawan.network.ApiResponse
import com.rajotiyapawan.pokedex.data.repository.PokemonRepositoryImpl
import com.rajotiyapawan.pokedex.domain.model.AbilityDetails.AbilityEffect
import com.rajotiyapawan.pokedex.domain.model.EvolutionChain
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.domain.model.PokemonBasicInfo
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.domain.model.PokemonListData
import com.rajotiyapawan.pokedex.domain.model.PokemonSpeciesData
import com.rajotiyapawan.pokedex.domain.model.RequestModel
import com.rajotiyapawan.pokedex.domain.usecase.GetAbilityDetailsUseCase
import com.rajotiyapawan.pokedex.domain.usecase.GetEvolutionChainDetailsUseCase
import com.rajotiyapawan.pokedex.domain.usecase.GetPokemonBasicInfoUseCase
import com.rajotiyapawan.pokedex.domain.usecase.GetPokemonDetailsUseCase
import com.rajotiyapawan.pokedex.domain.usecase.GetPokemonListUseCase
import com.rajotiyapawan.pokedex.domain.usecase.GetPokemonSpeciesDataUseCase
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.utility.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PokeViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
    private val getPokemonSpeciesDataUseCase: GetPokemonSpeciesDataUseCase,
    private val getPokemonBasicInfoUseCase: GetPokemonBasicInfoUseCase,
    private val getAbilityDetailsUseCase: GetAbilityDetailsUseCase,
    private val getEvolutionChainDetailsUseCase: GetEvolutionChainDetailsUseCase
) : ViewModel() {

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val pokeRepo = PokemonRepositoryImpl()
                val getPokemonListUseCase = GetPokemonListUseCase(pokeRepo)
                val getPokemonDetailsUseCase = GetPokemonDetailsUseCase(pokeRepo)
                val getPokemonSpeciesDataUseCase = GetPokemonSpeciesDataUseCase(pokeRepo)
                val getPokemonBasicInfoUseCase = GetPokemonBasicInfoUseCase(pokeRepo)
                val getAbilityDetailsUseCase = GetAbilityDetailsUseCase(pokeRepo)
                val getEvolutionChainDetailsUseCase = GetEvolutionChainDetailsUseCase(pokeRepo)
                return@initializer PokeViewModel(
                    getPokemonListUseCase,
                    getPokemonDetailsUseCase,
                    getPokemonSpeciesDataUseCase,
                    getPokemonBasicInfoUseCase, getAbilityDetailsUseCase, getEvolutionChainDetailsUseCase
                )
            }
        }
    }

    private var _userEvent = MutableSharedFlow<PokedexUserEvent>()
    val userEvent = _userEvent.asSharedFlow()

    private var _pokemonList: MutableStateFlow<UiState<PokemonListData>> = MutableStateFlow(UiState.Idle)
    val pokemonList = _pokemonList.asStateFlow()

    // Cache detail per Pokémon name
    private val _pokemonDetails = mutableStateMapOf<String, PokemonBasicInfo>()
    val pokemonDetails = _pokemonDetails
    private val _pokemonCache = mutableMapOf<String, PokemonData>()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    private var _searchResults = MutableStateFlow<List<NameUrlItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    init {
        getPokemonList()
        initializeSearch()
//        getPokemonData()
    }

    fun sendUserEvent(event: PokedexUserEvent) {
        viewModelScope.launch {
            _userEvent.emit(event)
        }
    }

    @OptIn(FlowPreview::class)
    private fun initializeSearch() {
        viewModelScope.launch {
            _query.debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    when (val response = pokemonList.value) {
                        is UiState.Success -> {
                            response.data.results?.let { results ->
                                _searchResults.value = results.filter { it.name?.contains(query) == true || query.isEmpty() }
                            }
                        }

                        else -> {
                            Log.e("FetchError", "Pokemon list not available")
                        }
                    }
                }
        }
    }

    // fetch the list of pokemon with names and ids
    private fun getPokemonList() {
        viewModelScope.launch {
            _pokemonList.value = UiState.Loading
            getPokemonListUseCase.invoke(params = RequestModel()).collectLatest {
                when (it) {
                    is ApiResponse.Success -> {
                        _pokemonList.value = UiState.Success(it.data)
                        _searchResults.value = it.data.results ?: emptyList()
                    }

                    is ApiResponse.Error -> {
                        Log.e("FetchError", "Failed in getting the Pokemon list")
                    }
                }
            }
        }
    }

    // fetch the details of the selected Pokemon
    private var _pokemonData = MutableStateFlow<UiState<PokemonData>>(UiState.Idle)
    val pokemonData = _pokemonData.asStateFlow()
    fun getPokemonData(item: NameUrlItem) {
        // clear the stale about data and evolution data
        _aboutData.value = PokemonSpeciesData.init()
        _evolutionChain.value = EvolutionChain(null)
        val name = item.name?.lowercase()

        // Use cached data if available
        val cached = _pokemonCache[name]
        if (cached != null) {
            _pokemonData.value = UiState.Success(cached)
            return
        }
        viewModelScope.launch {
            getPokemonDetailsUseCase.invoke(params = RequestModel(name = item.name, url = item.url)).collectLatest {
                when (it) {
                    is ApiResponse.Error -> {
                        Log.e("FetchError", "Failed for ${item.name}: ${it.message}")
                    }

                    is ApiResponse.Success<PokemonData> -> {
                        _pokemonData.value = UiState.Success(it.data)
                        _pokemonCache[name ?: ""] = it.data
                    }
                }
            }
        }
    }

    fun fetchBasicDetail(item: NameUrlItem?) {
        if (item?.name.isNullOrEmpty() && item?.url.isNullOrEmpty()) return
        if (_pokemonDetails.containsKey(item.name)) return // already fetched

        viewModelScope.launch {
            delay(100L) // Give some time between requests
            getPokemonBasicInfoUseCase.invoke(RequestModel(name = item.name, url = item.url)).collectLatest {
                when (it) {
                    is ApiResponse.Error -> {
                        Log.e("FetchError", "Failed for ${item.name}: ${it.message}")
                    }

                    is ApiResponse.Success -> {
                        item.name?.let { name ->
                            _pokemonDetails[name] = it.data
                        }
                    }
                }
            }
        }
    }

    private var _aboutData = MutableStateFlow(PokemonSpeciesData.Companion.init())
    val aboutData get() = _aboutData
    fun fetchPokemonAbout(item: NameUrlItem?) {
        viewModelScope.launch {
            delay(100L) // Give some time between requests
            getPokemonSpeciesDataUseCase.invoke(RequestModel(name = item?.name, url = item?.url)).collectLatest {
                when (it) {
                    is ApiResponse.Error -> Log.e("FetchError", "Failed for ${item?.name}: ${it.message}")
                    is ApiResponse.Success<PokemonSpeciesData> -> {
                        _aboutData.value = it.data
                    }
                }
            }
        }
    }

    // Cache detail per Pokémon name
    private val _abilityDetails = mutableStateMapOf<String, AbilityEffect>()
    val abilityDetails: Map<String, AbilityEffect> get() = _abilityDetails

    fun getAbilityEffect(item: NameUrlItem?) {
        if (_abilityDetails.containsKey(item?.name)) return // already fetched

        viewModelScope.launch {
            delay(100L) // Give some time between requests
            getAbilityDetailsUseCase.invoke(RequestModel(name = item?.name, url = item?.url)).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> Log.e("FetchError", "Failed for ${item?.name}: ${response.message}")
                    is ApiResponse.Success -> {
                        val detail = response.data
                        item?.name?.let { name ->
                            _abilityDetails[name] = AbilityEffect(
                                effect = detail.effect_entries
                                    .firstOrNull { it.language?.name == "en" && it.effect.isNotBlank() }
                                    ?.effect ?: "",
                                short_effect = detail.effect_entries
                                    .firstOrNull { it.language?.name == "en" && it.short_effect.isNotBlank() }
                                    ?.short_effect ?: "",
                                flavor_text = detail.flavor_text_entries
                                    .lastOrNull { it.language.name == "en" }
                                    ?.flavor_text
                                    ?.replace("\n", " ") ?: "",
                                language = NameUrlItem("", "")
                            )
                        }
                    }
                }
            }
        }
    }

    private val _evolutionChain = MutableStateFlow(EvolutionChain(null))
    val evolutionChain = _evolutionChain.asStateFlow()

    fun getEvolutionChain(item: NameUrlItem?, varieties: List<String>, currentPokemon: String) {
        viewModelScope.launch {
            getEvolutionChainDetailsUseCase.invoke(
                RequestModel(
                    name = item?.name,
                    url = item?.url,
                    varieties = varieties,
                    currentPokemon = currentPokemon
                )
            ).collectLatest {
                when (it) {
                    is ApiResponse.Error -> Log.e("FetchError", "Failed for ${item?.name}: ${it.message}")
                    is ApiResponse.Success -> {
                        _evolutionChain.value = it.data
                    }
                }
            }
        }
    }


    fun toggleFavourites(item: NameUrlItem) {
        Log.e("Favourites", "Favourite icon clicked - ${item.name}")
    }
}