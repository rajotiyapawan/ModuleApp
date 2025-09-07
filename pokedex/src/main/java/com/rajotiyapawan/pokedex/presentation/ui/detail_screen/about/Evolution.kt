package com.rajotiyapawan.pokedex.presentation.ui.detail_screen.about

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rajotiyapawan.pokedex.domain.model.Chain
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.detail_screen.DetailCardWithTitle
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import com.rajotiyapawan.pokedex.utility.TypeIcon
import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar
import com.rajotiyapawan.pokedex.utility.getFontFamily
import com.rajotiyapawan.pokedex.utility.noRippleClick
import com.rajotiyapawan.pokedex.utility.toRequirementText

@Composable
fun AboutEvolution(modifier: Modifier = Modifier, color: Color, currentPokemon: String, viewModel: PokeViewModel) {
    val aboutData by viewModel.aboutData.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getEvolutionChain(
            aboutData.evolutionChain,
            aboutData.varieties.map { it.pokemon.name ?: "" },
            currentPokemon
        )
    }
    val evolutionChain by viewModel.evolutionChain.collectAsState()

    evolutionChain.chain?.let { root ->
        DetailCardWithTitle(modifier, "Evolution chain", color) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(12.dp)
            ) {
                EvolutionNode(Modifier.fillMaxWidth(), root, "", viewModel)
            }
        }
    }
}

@Composable
private fun EvolutionNode(modifier: Modifier = Modifier, chain: Chain, requirementText: String, viewModel: PokeViewModel) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        EvolvePokemon(Modifier.weight(1f), chain.species, requirementText, viewModel)
        val innerWeight = if (chain.evolvesTo.any { it.evolvesTo.isNotEmpty() }) 2f else 1f
        if (chain.evolvesTo.isNotEmpty()) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, Modifier
                    .padding(horizontal = 8.dp)
                    .size(20.dp)
            )
            Column(
                Modifier.weight(innerWeight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                chain.evolvesTo.forEach { evo ->
                    EvolutionNode(Modifier, chain = evo, evo.evolutionDetails.toRequirementText(), viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun EvolvePokemon(modifier: Modifier = Modifier, pokemon: NameUrlItem?, requirementText: String, viewModel: PokeViewModel) {
    pokemon?.let {
        val detail = viewModel.pokemonDetails[it.name]
        Log.d("Evolution", "Pokemon name = ${it.name} and url = ${it.url}")
        LaunchedEffect(pokemon.name) {
            if (detail == null) {
                viewModel.fetchBasicDetail(NameUrlItem(name = it.name))
            }
        }
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = detail?.imageUrl, contentDescription = null, contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(56.dp)
                    .noRippleClick {
                        viewModel.sendUserEvent(PokedexUserEvent.OpenDetail(pokemon))
                    }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                detail?.types?.forEach { type ->
                    TypeIcon(type = type, withText = false)
                }
            }
            Text(
                (it.name ?: "").capitalizeFirstChar(),
                fontSize = 12.sp,
                lineHeight = 13.sp,
                fontFamily = getFontFamily(weight = FontWeight.SemiBold)
            )
            if (requirementText.isNotEmpty()) {
                Text(
                    requirementText,
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                    fontFamily = getFontFamily(), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(0.5f)
                )
            }
        }
    }
}