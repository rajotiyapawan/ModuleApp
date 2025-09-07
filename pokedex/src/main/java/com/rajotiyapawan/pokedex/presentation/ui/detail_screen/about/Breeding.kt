package com.rajotiyapawan.pokedex.presentation.ui.detail_screen.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.detail_screen.DetailCardWithTitle
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar
import com.rajotiyapawan.pokedex.utility.getEggGroupColor
import com.rajotiyapawan.pokedex.utility.getFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBreeding(modifier: Modifier = Modifier, color: Color, viewModel: PokeViewModel) {
    val aboutData by viewModel.aboutData.collectAsState()
    DetailCardWithTitle(modifier, "Breeding", color) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .padding(12.dp)
        ) {
            Text(
                text = "Egg groups:",
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Start,
                fontFamily = getFontFamily(),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                aboutData.eggGroups?.forEach {
                    EggGroupItem(Modifier.weight(1f), it) {
                        viewModel.sendUserEvent(PokedexUserEvent.OpenEggGroupList(eggGroup = it.name ?: ""))
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Egg cycles:",
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Start,
                fontFamily = getFontFamily(),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)) {
                        append("${aboutData.hatchCounter} ")
                    }
                    append(" (${aboutData.hatchCounter * 256} Steps)")
                },
                textAlign = TextAlign.Start,
                fontFamily = getFontFamily(),
                fontSize = 14.sp,
                lineHeight = 16.sp
            )

        }
    }
}

@Composable
private fun EggGroupItem(modifier: Modifier = Modifier, eggGroup: NameUrlItem, onInfoClick: () -> Unit) {
    val color = getEggGroupColor(eggGroup.name ?: "")
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.2f),
        tonalElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
        ) {
            Text(
                text = (eggGroup.name ?: "").capitalizeFirstChar(),
                fontFamily = getFontFamily(weight = FontWeight.SemiBold),
                fontSize = 14.sp, lineHeight = 16.sp,
                color = color
            )
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info, modifier = Modifier.size(16.dp),
                    contentDescription = "Info",
                    tint = color
                )
            }
        }
    }
}
