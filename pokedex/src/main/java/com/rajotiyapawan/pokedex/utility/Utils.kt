package com.rajotiyapawan.pokedex.utility

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rajotiyapawan.pokedex.R
import com.rajotiyapawan.pokedex.domain.model.PokeType

val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val montserratFont = GoogleFont("Lato")

fun getFontFamily(
    weight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    font: GoogleFont = montserratFont
): FontFamily {
    return FontFamily(Font(googleFont = font, fontProvider, weight = weight, style = fontStyle))
}

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

@Composable
fun Modifier.noRippleClick(delayMillis: Long = 500L, onClick: () -> Unit): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= delayMillis) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

@Composable
fun ImageFromUrl(modifier: Modifier = Modifier, imageUrl: String, contentScale: ContentScale = ContentScale.Fit) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl) // Your full TMDB image URL
            .crossfade(true)
//                .placeholder(R.mipmap.ic_placeholder_foreground) // Placeholder drawable
//                .error(R.mipmap.ic_placeholder_foreground)       // Show this if loading fails
            .build(),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier,
        imageLoader = ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
}

fun getTypeColor(type: PokeType): Color {
    return when (type) {
        PokeType.FIRE -> Color(0xFFEE8130)
        PokeType.WATER -> Color(0xFF6390F0)
        PokeType.GRASS -> Color(0xFF7AC74C)
        PokeType.ELECTRIC -> Color(0xFFF7D02C)
        PokeType.FIGHTING -> Color(0xFFC22E28)
        PokeType.PSYCHIC -> Color(0xFFF95587)
        PokeType.DRAGON -> Color(0xFF6F35FC)
        PokeType.DARK -> Color(0xFF705746)
        PokeType.FAIRY -> Color(0xFFD685AD)
        PokeType.GHOST -> Color(0xFF735797)
        PokeType.POISON -> Color(0xFFA33EA1)
        PokeType.ROCK -> Color(0xFFB6A136)
        PokeType.GROUND -> Color(0xFFE2BF65)
        PokeType.ICE -> Color(0xFF96D9D6)
        PokeType.BUG -> Color(0xFFA6B91A)
        PokeType.STEEL -> Color(0xFFB7B7CE)
        PokeType.NORMAL -> Color(0xFFA8A77A)
        PokeType.FLYING -> Color.LightGray
    }
}

fun convertHeightToFeetInches(heightDm: Int): Pair<Int, Int> {
    val totalInches = heightDm * 3.93701
    val feet = (totalInches / 12).toInt()
    val inches = (totalInches % 12).toInt()
    return Pair(feet, inches)
}

fun convertWeightToKg(weightHg: Int): Double {
    return weightHg * 0.1
}

fun convertWeightToLbs(weightHg: Int): Double {
    val kg = convertWeightToKg(weightHg)
    return kg * 2.20462
}

@DrawableRes
fun getTypeIconRes(type: PokeType): Int {
    return when (type) {
        PokeType.NORMAL -> R.drawable.normal
        PokeType.FIRE -> R.drawable.fire
        PokeType.WATER -> R.drawable.water
        PokeType.GRASS -> R.drawable.grass
        PokeType.ELECTRIC -> R.drawable.electric
        PokeType.ICE -> R.drawable.ice
        PokeType.FIGHTING -> R.drawable.fighting
        PokeType.POISON -> R.drawable.poison
        PokeType.GROUND -> R.drawable.ground
        PokeType.FLYING -> R.drawable.flying
        PokeType.PSYCHIC -> R.drawable.psychic
        PokeType.BUG -> R.drawable.bug
        PokeType.ROCK -> R.drawable.rock
        PokeType.GHOST -> R.drawable.ghost
        PokeType.DRAGON -> R.drawable.dragon
        PokeType.DARK -> R.drawable.dark
        PokeType.STEEL -> R.drawable.steel
        PokeType.FAIRY -> R.drawable.fairy
    }
}

@Composable
fun TypeIcon(type: PokeType, modifier: Modifier = Modifier) {
    val iconRes = getTypeIconRes(type)
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "$type type icon",
        modifier = modifier
    )
}

