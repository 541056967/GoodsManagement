package com.example.myaiapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myaiapplication.ui.screens.goods.detail.GoodsDetailScreen
import com.example.myaiapplication.ui.screens.goods.edit.GoodsEditScreen
import com.example.myaiapplication.ui.screens.goods.list.GoodsListScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.GoodsList.route
    ) {
        composable(Screen.GoodsList.route) {
            GoodsListScreen(
                onGoodsClick = { goodsId ->
                    navController.navigate(Screen.GoodsDetail.createRoute(goodsId))
                },
                onAddClick = {
                    navController.navigate(Screen.GoodsEdit.route)
                }
            )
        }

        composable(
            route = Screen.GoodsDetail.route,
            arguments = listOf(
                navArgument("goodsId") { type = NavType.LongType }
            )
        ) {
            GoodsDetailScreen(
                onEditClick = { goodsId ->
                    navController.navigate(Screen.GoodsEdit.createRoute(goodsId))
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.GoodsEdit.route,
            arguments = listOf(
                navArgument("goodsId") { 
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            GoodsEditScreen(
                onSaveSuccess = {
                    navController.navigateUp()
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
} 