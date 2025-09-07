package com.rajotiyapawan.pokedex.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.common.PokemonListItemUI
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar
import com.rajotiyapawan.pokedex.utility.getEggGroupColor
import com.rajotiyapawan.pokedex.utility.getFontFamily
import com.rajotiyapawan.pokedex.utility.noRippleClick

@Composable
fun EggGroupListScreen(modifier: Modifier = Modifier, eggGroup: String, viewModel: PokeViewModel) {
    val pokemonList = viewModel.eggGroupPokemonList.collectAsState()
    LaunchedEffect(eggGroup) {
        viewModel.getEggGroupPokemonList(NameUrlItem(name = eggGroup, url = ""))
    }
    Scaffold(modifier, containerColor = Color(0xfff5f5f5)) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .noRippleClick { viewModel.sendUserEvent(PokedexUserEvent.BackBtnClicked) }
                )
                Text(
                    eggGroup.capitalizeFirstChar(),
                    fontFamily = getFontFamily(weight = FontWeight.SemiBold), fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Box(
                    Modifier
                        .padding(end = 16.dp)
                        .background(color = getEggGroupColor(eggGroup).copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                        .padding(3.dp)
                ) {
                    Text(
                        "Egg group",
                        fontFamily = getFontFamily(weight = FontWeight.SemiBold), fontSize = 12.sp, lineHeight = 13.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center, color = getEggGroupColor(eggGroup)
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(brush = Brush.verticalGradient(listOf(Color.Gray.copy(alpha = 0.3f), Color.Transparent)))
            )
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(top = 12.dp)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pokemonList.value) { item ->
                    PokemonEggGroupListItem(Modifier.fillMaxWidth(), item, viewModel, itemSelected = {
                        viewModel.sendUserEvent(PokedexUserEvent.OpenDetail(item))
                    })
                }
            }
        }
    }
}

@Composable
private fun PokemonEggGroupListItem(
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