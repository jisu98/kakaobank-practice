package com.example.kakaobank.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kakaobank.R
import com.example.kakaobank.presentation.bookmark.BookmarkScreen
import com.example.kakaobank.presentation.detail.DetailScreen
import com.example.kakaobank.presentation.search.SearchScreen

private data class BottomNavItem(val screen: Screen, val label: String, val iconRes: Int)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Search, "검색", R.drawable.ic_search),
    BottomNavItem(Screen.Bookmark, "보관함", R.drawable.ic_bookmark_outline),
)

@Composable
fun KakaoBankNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = bottomNavItems.any { currentDestination?.hierarchy?.any { d -> d.route == it.screen.route } == true }
    val onItemClick: (String) -> Unit = { imageUrl ->
        navController.navigate(Screen.Detail.createRoute(imageUrl))
    }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(painter = painterResource(item.iconRes), contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Search.route) {
                SearchScreen(onItemClick = onItemClick)
            }
            composable(Screen.Bookmark.route) {
                BookmarkScreen(onItemClick = onItemClick)
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument(Screen.Detail.ARG_IMAGE_URL) { type = NavType.StringType }),
            ) { backStackEntry ->
                val imageUrl = backStackEntry.arguments?.getString(Screen.Detail.ARG_IMAGE_URL).orEmpty()
                DetailScreen(
                    imageUrl = imageUrl,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
