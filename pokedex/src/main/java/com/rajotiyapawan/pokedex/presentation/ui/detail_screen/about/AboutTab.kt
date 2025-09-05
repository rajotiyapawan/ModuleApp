package com.rajotiyapawan.pokedex.presentation.ui.detail_screen.about

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel

fun LazyListScope.aboutTabUI(
    modifier: Modifier = Modifier,
    viewModel: PokeViewModel,
    data: PokemonData,
    typeColors: List<Color>
) {
    item {
        AboutSpecies(modifier = modifier, viewModel = viewModel, data = data, color = typeColors)
    }
    item {
        data.abilities?.let {
            AboutAbilities(
                modifier = modifier
                    .padding(top = 16.dp),
                color = typeColors[0],
                viewModel = viewModel,
                abilities = it
            )
        }
    }
    item {
        AboutTraining(modifier.padding(top = 16.dp), typeColors[0], data, viewModel)
    }
    item {
        AboutBreeding(modifier.padding(top = 16.dp), color = typeColors[0], viewModel)
    }
    item {
        AboutEvolution(modifier.padding(top = 16.dp), color = typeColors[0], viewModel)
    }
}