package com.katar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import com.katar.app.ui.theme.KatarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KatarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "beranda",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Beranda Destination
                            composable("beranda") {
                                BerandaScreen()
                            }

                            // Anggota Destination
                            composable("anggota") {
                                AnggotaScreen()
                            }

                            // Kas Destination
                            composable("kas") {
                                KasScreen()
                            }

                            // Agenda Destination
                            composable("agenda") {
                                AgendaScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}