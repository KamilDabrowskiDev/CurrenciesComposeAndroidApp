package com.currencies.compose.app.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currencies.compose.app.ui.screen.currencies.CurrenciesScreen

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Composable
fun AppGraph() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CurrenciesRoute) {

        composable<CurrenciesRoute> {
            CurrenciesScreen(navController)
        }

//        composable<CurrencyDetailRoute> {
//            CurrencyDetailScreen(navController)
//        }
    }
}
