package com.rajotiyapawan.pokedex.presentation.ui.detail_screen.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajotiyapawan.pokedex.R
import com.rajotiyapawan.pokedex.domain.model.Ability
import com.rajotiyapawan.pokedex.domain.model.AbilityDetails.AbilityEffect
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.detail_screen.DetailCardWithTitle
import com.rajotiyapawan.pokedex.presentation.ui.detail_screen.ShowListButton
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar
import com.rajotiyapawan.pokedex.utility.getFontFamily
import com.rajotiyapawan.pokedex.utility.noRippleClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAbilities(
    modifier: Modifier = Modifier,
    color: Color,
    abilities: List<Ability>,
    viewModel: PokeViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showAbilityDescription by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    DetailCardWithTitle(
        modifier = modifier, title = "Abilities", color
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.ability_intro),
                color = Color.Black,
                fontFamily = getFontFamily(),
                fontSize = 14.sp,
                lineHeight = 16.sp,
            )
            Spacer(Modifier.height(18.dp))
            abilities.forEach { ability ->
                val detail = viewModel.abilityDetails[ability.ability?.name ?: ""]
                LaunchedEffect(ability.ability?.name) {
                    if (detail == null) viewModel.getAbilityEffect(ability.ability)
                }
                AbilityItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    abilityName = ability.ability?.name ?: "",
                    isHidden = ability.isHidden,
                    description = detail?.flavorText ?: "",
                    color = color,
                    onInfoClick = {
                        showAbilityDescription = ability.ability?.name ?: ""
                    }
                )
            }
        }
    }
    if (showAbilityDescription.isNotEmpty()) {
        ModalBottomSheet(
            onDismissRequest = { showAbilityDescription = "" },
            sheetState = sheetState,
            containerColor = Color(0xfff5f5f5)
        ) {
            val detail = viewModel.abilityDetails[showAbilityDescription]
            Box(
                modifier = Modifier.heightIn(max = screenHeight * 0.7f)
            ) {
                AbilitiesDialogUI(modifier = Modifier, title = showAbilityDescription, detail = detail, color = color, viewModel)
            }
        }
    }
}

@Composable
private fun AbilityItem(
    modifier: Modifier = Modifier,
    abilityName: String,
    isHidden: Boolean,
    description: String,
    color: Color,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                abilityName.capitalizeFirstChar(),
                color = color,
                fontFamily = getFontFamily(weight = FontWeight.Bold),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            if (isHidden) {
                Text(
                    " - Hidden",
                    color = color.copy(alpha = 0.7f),
                    fontFamily = getFontFamily(weight = FontWeight.Bold),
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )

            }
        }
        Icon(
            Icons.Outlined.Info, contentDescription = null, tint = color, modifier = Modifier
                .size(16.dp)
                .noRippleClick { onInfoClick() })
    }
    Spacer(Modifier.height(8.dp))
    Text(description, modifier = Modifier.fillMaxWidth(), fontSize = 14.sp, lineHeight = 16.sp)
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun AbilitiesDialogUI(modifier: Modifier = Modifier, title: String, detail: AbilityEffect?, color: Color, viewModel: PokeViewModel) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    "ABILITY",
                    color = Color(0xff909090),
                    fontFamily = getFontFamily(weight = FontWeight.SemiBold),
                    fontSize = 10.sp,
                    lineHeight = 11.sp
                )
            }
            item {
                Text(
                    title.capitalizeFirstChar(), fontFamily = getFontFamily(weight = FontWeight.Bold),
                    fontSize = 24.sp
                )
            }
            item {
                DetailCardWithTitle(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp), title = "Details", color = color
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
                    ) {
                        Text("Description", fontFamily = getFontFamily(weight = FontWeight.SemiBold), fontSize = 20.sp, lineHeight = 24.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(detail?.flavorText ?: "", fontSize = 14.sp, lineHeight = 16.sp)
                        Spacer(Modifier.height(16.dp))
                        Text("Effect", fontFamily = getFontFamily(weight = FontWeight.SemiBold), fontSize = 20.sp, lineHeight = 24.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(detail?.shortEffect ?: "", fontSize = 14.sp, lineHeight = 16.sp)
                        Spacer(Modifier.height(16.dp))
                        Text("Details", fontFamily = getFontFamily(weight = FontWeight.SemiBold), fontSize = 20.sp, lineHeight = 24.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(detail?.effect ?: "", fontSize = 14.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
        ShowListButton(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        ) {
            viewModel.sendUserEvent(PokedexUserEvent.OpenAbilityList(abilityName = title))
        }
    }
}
