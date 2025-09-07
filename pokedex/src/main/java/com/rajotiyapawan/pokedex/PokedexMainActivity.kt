package com.rajotiyapawan.pokedex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.model.PokedexUserEvent
import com.rajotiyapawan.pokedex.presentation.navigation.Routes
import com.rajotiyapawan.pokedex.presentation.ui.AbilityListScreen
import com.rajotiyapawan.pokedex.presentation.ui.EggGroupListScreen
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

        PrepareNavGraph(modifier, navController, Routes.Home.route)
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun PrepareNavGraph(modifier: Modifier = Modifier, navController: NavHostController, startDestination: String) {
        NavHost(
            navController = navController, startDestination = startDestination,
            modifier = modifier
        ) {
            composable(
                route = Routes.Home.route,
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
                route = Routes.Detail.route,
                arguments = listOf(
                    navArgument("pokemon") { type = NavType.StringType },
                ),
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
                val pokemon = backStackEntry.arguments?.getString("pokemon") ?: return@composable
                PokemonDetailScreen(Modifier.fillMaxSize(), NameUrlItem(name = pokemon), viewModel)
            }

            composable(
                route = Routes.AbilityList.route,
                arguments = listOf(
                    navArgument("abilityName") { type = NavType.StringType },
                ),
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
                val abilityName = backStackEntry.arguments?.getString("abilityName") ?: return@composable
                AbilityListScreen(Modifier.fillMaxSize(), abilityName, viewModel)
            }

            composable(
                route = Routes.EggGroupList.route,
                arguments = listOf(
                    navArgument("eggGroupName") { type = NavType.StringType },
                ),
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
                val eggGroup = backStackEntry.arguments?.getString("eggGroupName") ?: return@composable
                EggGroupListScreen(Modifier.fillMaxSize(), eggGroup, viewModel)
            }

        }
    }

    private fun handleUserEvents(navController: NavHostController, event: PokedexUserEvent) {
        when (event) {
            PokedexUserEvent.BackBtnClicked -> navController.popBackStack()
            is PokedexUserEvent.OpenDetail -> navController.navigate(Routes.Detail.createRoute(event.pokemon))
            is PokedexUserEvent.OpenAbilityList -> navController.navigate(Routes.AbilityList.createRoute(event.abilityName))
            is PokedexUserEvent.OpenEggGroupList -> navController.navigate(Routes.EggGroupList.createRoute(event.eggGroup))
        }
    }
}