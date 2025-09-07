package com.rajotiyapawan.pokedex.utility

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rajotiyapawan.pokedex.R
import com.rajotiyapawan.pokedex.domain.model.EvolutionDetail
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

fun getTypeIconColor(type: PokeType): Color {
    return when (type) {
        PokeType.NORMAL -> Color(0xff9FA29F)
        PokeType.FIRE -> Color(0xffFBA54C)
        PokeType.WATER -> Color(0xff549DDF)
        PokeType.GRASS -> Color(0xff60BD58)
        PokeType.ELECTRIC -> Color(0xffF2D94E)
        PokeType.ICE -> Color(0xff76D0C1)
        PokeType.FIGHTING -> Color(0xffD3425F)
        PokeType.POISON -> Color(0xffB764CF)
        PokeType.GROUND -> Color(0xffD97C4C)
        PokeType.FLYING -> Color(0xffA1BBEC)
        PokeType.PSYCHIC -> Color(0xffFA8581)
        PokeType.BUG -> Color(0xff92BC2C)
        PokeType.ROCK -> Color(0xffC9BB8A)
        PokeType.GHOST -> Color(0xff5F6DBC)
        PokeType.DRAGON -> Color(0xff0C69C7)
        PokeType.DARK -> Color(0xff595762)
        PokeType.STEEL -> Color(0xff5694A3)
        PokeType.FAIRY -> Color(0xffEF90E5)
    }
}

@Composable
fun TypeIcon(modifier: Modifier = Modifier, type: PokeType, withText: Boolean = true) {
    val iconRes = getTypeIconRes(type)
    Row(
        modifier
            .background(color = getTypeIconColor(type), shape = RoundedCornerShape(50))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "$type type icon", modifier = Modifier.size(12.dp)
        )
        if (withText) {
            Spacer(Modifier.width(8.dp))
            Text(
                type.name.lowercase().capitalizeFirstChar(),
                color = Color.White, fontFamily = getFontFamily(weight = FontWeight.SemiBold),
                fontSize = 14.sp, lineHeight = 15.sp
            )
            Spacer(Modifier.width(16.dp))
        }
    }
}

fun getEggGroupColor(name: String): Color {
    return when (name.lowercase()) {
        "monster" -> Color(0xFF6B4226)
        "water1", "water2", "water3" -> Color(0xFF1E90FF)
        "bug" -> Color(0xFF7CFC00)
        "flying" -> Color(0xFF87CEEB)
        "fairy" -> Color(0xFFFFB6C1)
        "human-like" -> Color(0xFFDA70D6)
        "mineral" -> Color(0xFF808080)
        "ditto" -> Color(0xFFBA55D3)
        "dragon" -> Color(0xFF8B0000)
        "undiscovered" -> Color(0xFFA9A9A9)
        "plant" -> Color(0xFF7AC74C)
        else -> Color.Gray
    }
}

// requirements for evolutions
fun List<EvolutionDetail>.toRequirementText(): String {
    if (isEmpty()) return ""

    val grouped = mutableListOf<String>()

    val levelUps = filter { it.trigger.name == "level-up" }
    val useItem = filter { it.trigger.name == "use-item" }
    val trades = filter { it.trigger.name == "trade" }

    if (levelUps.isNotEmpty()) {
        grouped += buildLevelUpRequirement(levelUps)
    }

    grouped += useItem.mapNotNull { it.item?.let { item -> "Use ${item.name?.capitalizeFirstChar()}" } }
    grouped += trades.map { buildTradeRequirement(it) }

    return grouped.joinToString(" OR ")
}

// --- Helpers ---

private fun buildLevelUpRequirement(levelUps: List<EvolutionDetail>): String {
    val reqs = mutableListOf<String>()

    reqs += levelUps.minOfOrNull { it.minLevel }?.takeIf { it > 0 }?.let { "at level $it" } ?: ""

    if (levelUps.any { it.minHappiness > 0 }) reqs += "with high happiness"
    if (levelUps.any { it.minAffection > 0 }) reqs += "with high affection"
    if (levelUps.any { it.minBeauty > 0 }) reqs += "with high beauty"

    reqs += buildGenderRequirement(levelUps)
    reqs += buildTimeRequirement(levelUps)
    if (levelUps.any { it.needsOverworldRain }) reqs += "while raining"
    reqs += buildKnownMovesRequirement(levelUps)
    reqs += buildHeldItemsRequirement(levelUps)
    reqs += buildPartyRequirement(levelUps)
    reqs += buildRelativeStatsRequirement(levelUps)
    reqs += buildLocationRequirement(levelUps)

    return "Level up ${reqs.filter { it.isNotEmpty() }.joinToString(" ")}".trim()
}

private fun buildGenderRequirement(levelUps: List<EvolutionDetail>): String {
    return when {
        levelUps.any { it.gender == "female" } -> "if female"
        levelUps.any { it.gender == "male" } -> "if male"
        else -> ""
    }
}

private fun buildTimeRequirement(levelUps: List<EvolutionDetail>): String {
    val times = levelUps.mapNotNull { it.timeOfDay.takeIf { t -> t.isNotBlank() } }.toSet()
    return if (times.isNotEmpty()) "during the ${times.joinToString(" or ")}" else ""
}

private fun buildKnownMovesRequirement(levelUps: List<EvolutionDetail>): String {
    val moves = levelUps.mapNotNull { it.knownMove?.name }.toSet()
    val moveTypes = levelUps.mapNotNull { it.knownMoveType?.name }.toSet()

    return when {
        moves.isNotEmpty() -> "knowing ${moves.joinToString(" or ") { it.capitalizeFirstChar() }}"
        moveTypes.isNotEmpty() -> "knowing a ${moveTypes.joinToString(" or ")} type move"
        else -> ""
    }
}

private fun buildHeldItemsRequirement(levelUps: List<EvolutionDetail>): String {
    val items = levelUps.mapNotNull { it.heldItem?.name }.toSet()
    return if (items.isNotEmpty()) "while holding ${items.joinToString(" or ") { it.capitalizeFirstChar() }}" else ""
}

private fun buildPartyRequirement(levelUps: List<EvolutionDetail>): String {
    val species = levelUps.mapNotNull { it.partySpecies?.name }.toSet()
    val types = levelUps.mapNotNull { it.partyType?.name }.toSet()

    return when {
        species.isNotEmpty() -> "with ${species.joinToString(" or ") { it.capitalizeFirstChar() }} in party"
        types.isNotEmpty() -> "with a ${types.joinToString(" or ")} type Pokémon in party"
        else -> ""
    }
}

private fun buildRelativeStatsRequirement(levelUps: List<EvolutionDetail>): String {
    return when (levelUps.mapNotNull { it.relativePhysicalStats }.firstOrNull()) {
        -1 -> "if Defense > Attack"
        0 -> "if Attack = Defense"
        1 -> "if Attack > Defense"
        else -> ""
    }
}

private fun buildLocationRequirement(levelUps: List<EvolutionDetail>): String {
    val locations = levelUps.mapNotNull { it.location?.name }.toSet()
    return when {
        locations.any { it.contains("forest", true) || it.contains("moss", true) } -> "near a Mossy Rock"
        locations.any { it.contains("icy", true) || it.contains("ice", true) || it.contains("frost", true) } -> "near an Icy Rock"
        locations.any { it.contains("magnetic", true) || it.contains("cave", true) } -> "near a Magnetic Field"
        locations.isNotEmpty() -> "at ${locations.joinToString(" or ")}"
        else -> ""
    }
}

private fun buildTradeRequirement(detail: EvolutionDetail): String {
    var text = "Trade"
    detail.tradeSpecies?.let { text += " for ${it.name?.capitalizeFirstChar()}" }
    detail.heldItem?.let { text += " while holding ${it.name?.capitalizeFirstChar()}" }
    return text
}


