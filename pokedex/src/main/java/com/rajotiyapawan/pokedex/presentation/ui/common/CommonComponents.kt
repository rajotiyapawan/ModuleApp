package com.rajotiyapawan.pokedex.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.domain.model.PokemonBasicInfo
import com.rajotiyapawan.pokedex.utility.TypeIcon
import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar
import com.rajotiyapawan.pokedex.utility.getFontFamily
import com.rajotiyapawan.pokedex.utility.getTypeIconColor
import com.rajotiyapawan.pokedex.utility.noRippleClick
import java.util.Locale

@Composable
fun PokemonListItemUI(
    modifier: Modifier = Modifier,
    item: NameUrlItem,
    detail: PokemonBasicInfo,
    itemSelected: () -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current
    val typeColors = detail?.types?.map { getTypeIconColor(it) } ?: listOf()
    val gradientBrush = remember(detail?.types) {
        when (typeColors.size) {
            1 -> {
                val base = typeColors[0]
                Brush.linearGradient(
                    colors = listOf(base.copy(alpha = 0.4f), base),
                    start = Offset.Zero,
                    end = Offset(width * 3.6f, 0f)
                )
            }

            2 -> Brush.linearGradient(
                colors = listOf(
                    typeColors[0].copy(alpha = 0.9f),
                    typeColors[0].copy(alpha = 0.6f),
                    typeColors[1].copy(alpha = 0.6f),
                    typeColors[1].copy(alpha = 0.9f)
                ),
//                colorStops = floatArrayOf(0.0f, 0.4f, 0.55f, 1.0f), // skewed to right
                start = Offset.Zero,
                end = Offset(width * 3.6f, 0f)
            )

            else -> Brush.verticalGradient(colors = listOf(Color.LightGray, Color.DarkGray))
        }
    }

    Row(
        modifier
            .background(
                brush = gradientBrush,
                shape = RoundedCornerShape(12.dp)
            )
            .noRippleClick {
                itemSelected()
            }
            .padding(vertical = 5.dp, horizontal = 8.dp)
    ) {
        Box(Modifier.size(80.dp)) {
            val request = ImageRequest.Builder(context)
                .data(detail.imageUrl) // must be the high-res artwork URL
                .crossfade(true)
                .size(Size.ORIGINAL) // do not scale down
                .build()
            AsyncImage(
                model = request, contentDescription = null, contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Column(Modifier.padding(start = 8.dp)) {
            Text(
                (item.name ?: "").capitalizeFirstChar(),
                fontFamily = getFontFamily(weight = FontWeight.Bold), fontSize = 24.sp, color = Color.White
            )
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                detail?.types?.forEach {
                    TypeIcon(type = it, withText = false)
                }
            }
        }
        val id = detail?.id ?: 0
        val formatted = if (id < 1000) {
            String.format(Locale.US, "%03d", id)
        } else {
            id.toString()
        }
        Spacer(Modifier.weight(1f))
        Column(modifier = Modifier.height(80.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
            Text("#$formatted", fontFamily = getFontFamily(weight = FontWeight.SemiBold), fontSize = 30.sp, color = Color.White)
        }
    }
}