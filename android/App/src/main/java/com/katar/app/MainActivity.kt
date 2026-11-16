package com.katar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.katar.app.data.KatarViewModelFactory
import com.katar.app.navigation.BottomNavigationBar
import com.katar.app.ui.AgendaScreen
import com.katar.app.ui.AnggotaScreen
import com.katar.app.ui.BerandaScreen
import com.katar.app.ui.CashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil database dari Application class
        val app = application as KatarApplication
        val database = app.database

        // Buat factory ViewModel
        val viewModelFactory = KatarViewModelFactory(database)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(viewModelFactory = viewModelFactory)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModelFactory: KatarViewModelFactory) {
    val navController = rememberNavController()
    val items = listOf("Beranda", "Anggota", "Kas", "Agenda")

    Scaffold(
        bottomBar = {
            BottomNavigationBar(items = items, navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Beranda",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Beranda") { BerandaScreen() }
            composable("Anggota") { AnggotaScreen() }
            composable("Kas") { CashScreen() }
            composable("Agenda") { AgendaScreen() }
        }
    }
}