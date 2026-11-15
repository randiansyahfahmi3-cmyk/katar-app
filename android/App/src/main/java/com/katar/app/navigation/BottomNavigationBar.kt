package com.katar.app.navigation

import androidx.compose.foundation BottomNavigationItem
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarIcon
import androidx.compose.material3.NavigationBarTab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.katar.app.R
import com.katar.app.navigation.route

/**
 * Bottom Navigation Bar with 4 main menus for KATAR application.
 * Integrates with NavController for navigation between screens.
 */
@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    val selectedIndex = remember { mutableIntStateOf(0) }

    NavigationBar(
        modifier = Modifier.fillMaxSize(),
        content = {
            // Home / Beranda
            NavigationBarTab(
                label = { Text(text = "Beranda") },
                icon = {
                    Icon(
                        painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_home),
                        contentDescription = "Beranda"
                    )
                },
                selected = selectedIndex.value == 0,
                onClick = {
                    selectedIndex.value = 0
                    navController.navigate("beranda")
                }
            )

            // Anggota
            NavigationBarTab(
                label = { Text(text = "Anggota") },
                icon = {
                    Icon(
                        painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_people),
                        contentDescription = "Anggota"
                    )
                },
                selected = selectedIndex.value == 1,
                onClick = {
                    selectedIndex.value = 1
                    navController.navigate("anggota")
                }
            )

            // Kas
            NavigationBarTab(
                label = { Text(text = "Kas") },
                icon = {
                    Icon(
                        painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_account_balance),
                        contentDescription = "Kas"
                    )
                },
                selected = selectedIndex.value == 2,
                onClick = {
                    selectedIndex.value = 2
                    navController.navigate("kas")
                }
            )

            // Agenda
            NavigationBarTab(
                label = { Text(text = "Agenda") },
                icon = {
                    Icon(
                        painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_calendar_today),
                        contentDescription = "Agenda"
                    )
                },
                selected = selectedIndex.value == 3,
                onClick = {
                    selectedIndex.value = 3
                    navController.navigate("agenda")
                }
            )
        }
    )
}