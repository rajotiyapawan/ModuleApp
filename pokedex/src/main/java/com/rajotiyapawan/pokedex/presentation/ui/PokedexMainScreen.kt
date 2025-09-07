package com.rajotiyapawan.pokedex.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.common.PokemonListItemUI
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import com.rajotiyapawan.pokedex.utility.UiState
import com.rajotiyapawan.pokedex.utility.getFontFamily
import com.rajotiyapawan.pokedex.utility.noRippleClick

@Composable
fun PokedexMainScreen(modifier: Modifier = Modifier, viewModel: PokeViewModel) {
    val pokeData = viewModel.pokemonList.collectAsState()
    when (val response = pokeData.value) {
        UiState.Loading -> {
            Box(modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.size(56.dp))
            }
        }

        is UiState.Success -> {
            response.data.results?.let { MainScreenUI(modifier, viewModel) }
        }

        else -> {
            Log.e("Fetch Error", "No data.")
        }
    }
}

@Composable
private fun MainScreenUI(modifier: Modifier = Modifier, viewModel: PokeViewModel) {
    val focusManager = LocalFocusManager.current
    Scaffold(modifier) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .noRippleClick { focusManager.clearFocus() }
        ) {
            Text(
                "Pokedex", Modifier
                    .fillMaxWidth(),
                fontFamily = getFontFamily(weight = FontWeight.SemiBold),
                fontSize = 28.sp, color = Color.Red, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                SearchBar(
                    Modifier
                        .padding(end = 16.dp)
                        .weight(1f), viewModel
                )
                Icon(Icons.Outlined.Menu, contentDescription = null, tint = Color.Black)
            }
            PokemonListUI(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp), viewModel
            )
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier, viewModel: PokeViewModel) {
    val query by viewModel.query.collectAsState()
    BasicTextField(
        value = query,
        onValueChange = { viewModel.onQueryChanged(it) },
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, fontFamily = getFontFamily()),
        modifier = modifier
            .height(50.dp)
            .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(50)),
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(30.dp))
                Box(
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp), contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            "Search for a Pokemon ...",
                            style = TextStyle(color = Color.Black, fontSize = 16.sp, fontFamily = getFontFamily())
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(Color.Gray, shape = CircleShape)
                            .size(30.dp)
                            .noRippleClick { viewModel.onQueryChanged("") }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Clear,
                            contentDescription = null, tint = Color.White
                        )
                    }
                }
            }
        })
}

@Composable
private fun PokemonListUI(modifier: Modifier = Modifier, viewModel: PokeViewModel) {
    val list by viewModel.searchResults.collectAsState()

    LazyColumn(
        modifier
            .padding(top = 12.dp),
        contentPadding = PaddingValues(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(list) { item ->
            PokemonListItem(Modifier.fillMaxWidth(), item, viewModel, itemSelected = {
                viewModel.sendUserEvent(PokedexUserEvent.OpenDetail(item))
            })
        }
    }
}

@Composable
private fun PokemonListItem(
    modifier: Modifier = Modifier, item: NameUrlItem,
    viewModel: PokeViewModel,
    itemSelected: () -> Unit
) {
    val detail = viewModel.pokemonDetails[item.name]

    LaunchedEffect(item.name) {
        if (detail == null) viewModel.fetchBasicDetail(item)
    }
    Log.d("PokemonUI", "Composing ${item.name} | detail loaded = ${detail != null}")

    detail?.let { PokemonListItemUI(modifier, item, it) { itemSelected() } }
}
