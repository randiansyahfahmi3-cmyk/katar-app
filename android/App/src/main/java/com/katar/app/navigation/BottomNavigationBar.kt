package com.katar.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(items: List<String>, navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val icon = when (item) {
                "Beranda" -> Icons.Filled.Home
                "Anggota" -> Icons.Filled.Person
                "Kas" -> Icons.Filled.ShoppingCart
                "Agenda" -> Icons.Filled.DateRange
                else -> Icons.Filled.Home
            }
            NavigationBarItem(
                selected = currentRoute == item,
                onClick = {
                    if (currentRoute != item) {
                        navController.navigate(item) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(icon, contentDescription = item) },
                label = { Text(item) }
            )
        }
    }
}