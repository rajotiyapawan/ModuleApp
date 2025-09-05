package com.rajotiyapawan.pokedex

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rajotiyapawan.pokedex.model.NameItem
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.ui.PokedexMainScreen
import com.rajotiyapawan.pokedex.presentation.ui.PokemonDetailScreen
import com.rajotiyapawan.pokedex.presentation.ui.theme.ModuleActivityTheme
import com.rajotiyapawan.pokedex.presentation.viewmodel.PokeViewModel
import kotlinx.coroutines.flow.collectLatest

class PokedexMainActivity : ComponentActivity() {

    private val viewModel: PokeViewModel by viewModels { PokeViewModel.factory }

    companion object {
        fun launchPokedex(context: Context) {
            context.startActivity(Intent(context, PokedexMainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ModuleActivityTheme {
                MainViews(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Composable
    private fun MainViews(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        // collect user events
        LaunchedEffect(Unit) {
            viewModel.userEvent.collectLatest {
                handleUserEvents(navController, it)
            }
        }

        val startDestination = "main"
        PrepareNavGraph(modifier, navController, startDestination)
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun PrepareNavGraph(modifier: Modifier = Modifier, navController: NavHostController, startDestination: String) {
        NavHost(
            navController = navController, startDestination = startDestination,
            modifier = modifier
        ) {
            composable(
                "main",
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                }) {
                PokedexMainScreen(Modifier.fillMaxSize(), viewModel)
            }

            composable(
                "detail/{pokemon}",
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                }) { backStackEntry ->
                val item = backStackEntry.arguments?.getString("pokemon") ?: return@composable
                PokemonDetailScreen(Modifier.fillMaxSize(), NameItem(item, ""), viewModel)
            }

        }
    }

    private fun handleUserEvents(navController: NavHostController, event: PokedexUserEvent) {
        when (event) {
            PokedexUserEvent.BackBtnClicked -> navController.popBackStack()
            is PokedexUserEvent.OpenDetail -> navController.navigate("detail/${event.nameItem.name}")
        }
    }
}