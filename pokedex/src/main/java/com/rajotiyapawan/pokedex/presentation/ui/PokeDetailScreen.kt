package com.rajotiyapawan.pokedex.presentation.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.detail_screen.about.aboutTabUI
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import com.rajotiyapawan.pokedex.utility.UiState
import com.rajotiyapawan.pokedex.utility.capitalizeFirstChar
import com.rajotiyapawan.pokedex.utility.getFontFamily
import com.rajotiyapawan.pokedex.utility.getTypeIconColor
import com.rajotiyapawan.pokedex.utility.noRippleClick
import kotlinx.coroutines.launch
import java.util.Locale

enum class PokemonDataTabs {
    About, Stats, Moves, Other
}

@Composable
fun PokemonDetailScreen(modifier: Modifier = Modifier, nameItem: NameUrlItem, viewModel: PokeViewModel) {
    // Trigger fetch only when name changes
    LaunchedEffect(nameItem.name) {
        viewModel.getPokemonData(nameItem)
    }
    val pokeData = viewModel.pokemonData.collectAsState()
    when (val response = pokeData.value) {
        UiState.Loading -> {
            CircularProgressIndicator(modifier)
        }

        is UiState.Success -> {
            DetailMainUI(modifier, viewModel, response.data)
        }

        else -> {
            Log.e("Fetch Error", "No data.")
        }
    }
}

private fun getStatusBarColor(index: Int): Color =
    if (index == 0) Color.Transparent else Color(0xfff5f5f5)

private fun getTintColorBasedOnStatusBarColor(statusBarColor: Color): Color = if (statusBarColor == Color.Transparent) Color.White else Color.Black

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailMainUI(modifier: Modifier = Modifier, viewModel: PokeViewModel, data: PokemonData) {
    val listState = rememberLazyListState()
    val statusBarColor = remember { mutableStateOf(Color.Transparent) }
    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                statusBarColor.value = getStatusBarColor(index)
            }
    }
    val width = remember { mutableStateOf(0) }
    val height = LocalConfiguration.current.screenHeightDp

    val typeColors = data.types.map { getTypeIconColor(it) }

    Scaffold { padding ->
        Box(
            modifier
                .fillMaxSize()
                .background(Color(0xfff5f5f5))
        ) {
            val selectedTab = remember { mutableStateOf(PokemonDataTabs.About) }
            Box(Modifier.padding(bottom = padding.calculateBottomPadding())) {
                LazyColumn(
                    modifier = Modifier
                        .onGloballyPositioned {
                            width.value = it.size.width
                        }, contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    state = listState
                ) {
                    item {
                        DetailTopSection(
                            Modifier
                                .fillMaxWidth()
                                .height((height * 0.5f).dp), typeColors, padding, data
                        )
                    }
                    stickyHeader {
                        TabBarRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(statusBarColor.value)
                                .padding(top = 36.dp + padding.calculateTopPadding()),
                            selectedTab.value.ordinal
                        ) { selectedTab.value = it }
                    }
                    when (selectedTab.value) {
                        PokemonDataTabs.About -> {
                            aboutTabUI(modifier = Modifier.fillMaxWidth(), viewModel, data, typeColors)
                        }

                        PokemonDataTabs.Stats -> {
                            item { Text("Stats") }
                        }

                        PokemonDataTabs.Moves -> {
                            item { Text("Moves") }
                        }

                        PokemonDataTabs.Other -> {
                            item { Text("Other") }
                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = padding.calculateTopPadding())
                        .background(statusBarColor.value)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .noRippleClick { viewModel.sendUserEvent(PokedexUserEvent.BackBtnClicked) },
                        tint = getTintColorBasedOnStatusBarColor(statusBarColor.value)
                    )
                    Icon(
                        Icons.Default.FavoriteBorder, contentDescription = null, modifier = Modifier
                            .padding(8.dp)
                            .noRippleClick {
                                viewModel.toggleFavourites(NameUrlItem(data.name, ""))
                            },
                        tint = getTintColorBasedOnStatusBarColor(statusBarColor.value)
                    )
                }
            }
        }
    }

}

@Composable
private fun DetailTopSection(
    modifier: Modifier = Modifier,
    typeColors: List<Color>,
    paddingValues: PaddingValues,
    data: PokemonData
) {
    val context = LocalContext.current
    val id = remember(data) {
        val id = data.id ?: 0
        if (id < 1000) {
            String.format(Locale.US, "%03d", id)
        } else {
            id.toString()
        }
    }
    val scaleInX = remember { Animatable(1f) }
    val startWidth = with(LocalDensity.current) { (56.dp).toPx() }

    val targetSizePx = remember { mutableStateOf(IntSize.Zero) }
    LaunchedEffect(targetSizePx.value) {
        val scaleFactorX = targetSizePx.value.width / startWidth
        launch {
            scaleInX.animateTo(scaleFactorX, tween(500))
        }
    }
    Box(modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .drawBehind {
                    val canvasWidth = size.width
                    val arcHeight = 510f // height of the curved area
                    val path = Path().apply {
                        // Start at top-left corner
                        moveTo(-2f, -40f)
                        arcTo(
                            rect = Rect(
                                left = -canvasWidth / 2f,
                                top = 0f,
                                right = canvasWidth * 1.5f,
                                bottom = arcHeight * 2.5f
                            ),
                            startAngleDegrees = -180f,
                            sweepAngleDegrees = -180f,
                            forceMoveTo = false
                        )
                        lineTo(canvasWidth, -40f)
                        close()
                    }

                    // === Draw the upper arc gradient ===
                    val gradientBrush =
                        when (typeColors.size) {
                            1 -> {
                                val base = typeColors[0]
                                val darker =
                                    base.copy(alpha = 1f).compositeOver(Color.Black.copy(alpha = 0.2f)) // Slight dark blend
                                Brush.linearGradient(
                                    colors = listOf(base.copy(alpha = 0.8f), darker),
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, 0f)
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
                                start = Offset(0f, size.height),
                                end = Offset(size.width * 1.8f, -size.height * 0.2f)
                            )

                            else -> Brush.verticalGradient(colors = listOf(Color.LightGray, Color.DarkGray))
                        }
                    drawPath(
                        path = path,
                        brush = gradientBrush
                    )
                }
                .padding(top = paddingValues.calculateTopPadding() + 40.dp)
        ) {
            Text("#$id", fontFamily = getFontFamily(weight = FontWeight.SemiBold), color = Color.White)
            Text(
                (data.name ?: "").capitalizeFirstChar(),
                fontFamily = getFontFamily(weight = FontWeight.SemiBold),
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(Modifier.height(50.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { layoutCoordinates ->
                        targetSizePx.value = layoutCoordinates.size
                    }, contentAlignment = Alignment.Center
            ) {
                val request = ImageRequest.Builder(context)
                    .data(data.imageUrl) // must be the high-res artwork URL
                    .crossfade(true)
                    .size(Size.ORIGINAL) // do not scale down
                    .build()
                AsyncImage(
                    request,
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            scaleX = scaleInX.value
                            scaleY = scaleInX.value
                        },
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                AsyncImage(
                    request,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0f),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
private fun TabBarRow(modifier: Modifier, selectedTabIndex: Int, onTabSelected: (PokemonDataTabs) -> Unit) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        divider = {},
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        }
    ) {
        PokemonDataTabs.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        tab.name,
                        color = Color.Black,
                        fontFamily = getFontFamily(FontWeight.SemiBold),
                        fontSize = 14.sp,
                        lineHeight = 15.sp
                    )
                }
            )
        }
    }
}