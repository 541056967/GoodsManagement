package com.example.myaiapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Interests
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myaiapplication.ui.navigation.Screen
import com.example.myaiapplication.ui.screens.analytics.AnalyticsScreen
import com.example.myaiapplication.ui.screens.config.ConfigScreen
import com.example.myaiapplication.ui.screens.goods.detail.GoodsDetailScreen
import com.example.myaiapplication.ui.screens.goods.edit.GoodsEditScreen
import com.example.myaiapplication.ui.screens.goods.list.GoodsListScreen
import com.example.myaiapplication.ui.theme.MyAIApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAIApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun MyApp() {
    val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.GoodsList.route,
        ) {
            composable(Screen.GoodsList.route) {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    GoodsListScreen(
                        onGoodsClick = { goodsId ->
                            navController.navigate(Screen.GoodsDetail.createRoute(goodsId))
                        },
                        onAddClick = {
                            navController.navigate(Screen.GoodsEdit.route)
                        }
                    )
                }

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
            composable("config") {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    ConfigScreen()
                }
            }
            composable("analytics") {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    AnalyticsScreen()
                }
            }
        }

}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.GoodsList.route, "config", "analytics")
    val icons = listOf(Icons.Default.List, Icons.Default.Interests,Icons.Default.Analytics)
    val titles = listOf("Goods", "Config", "Analytics")

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = titles[index]) },
                label = { Text(titles[index]) },
                selected = currentRoute == screen,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
} 