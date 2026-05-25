package com.sycho.lookup.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.sycho.lookup.data.remote.NetworkResult
import com.sycho.lookup.ui.screens.HistoryScreen
import com.sycho.lookup.ui.screens.HomeScreen
import com.sycho.lookup.ui.screens.ResultScreen
import com.sycho.lookup.ui.viewmodel.LookupViewModel

object Routes {
    const val HOME    = "home"
    const val RESULT  = "result"
    const val HISTORY = "history"
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    val viewModel: LookupViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToResult  = { navController.navigate(Routes.RESULT) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) }
            )
        }
        composable(Routes.RESULT) {
            ResultScreen(viewModel = viewModel, onBack = {
                viewModel.resetState()
                navController.popBackStack()
            })
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSearchFromHistory = { query ->
                    navController.popBackStack()
                    viewModel.searchFromHistory(query)
                    navController.navigate(Routes.RESULT)
                }
            )
        }
    }

    val lookupState by viewModel.lookupState.collectAsState()
    LaunchedEffect(lookupState) {
        if (lookupState is NetworkResult.Success &&
            navController.currentDestination?.route == Routes.HOME) {
            navController.navigate(Routes.RESULT)
        }
    }
}
