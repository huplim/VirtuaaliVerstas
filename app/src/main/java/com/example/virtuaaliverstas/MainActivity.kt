package com.example.virtuaaliverstas

import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.virtuaaliverstas.ui.theme.VirtuaaliVerstasTheme

class MainActivity : ComponentActivity() {
    private lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize and register the receiver
        connectivityReceiver = ConnectivityReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)

        // Check for location permissions, ask for them if not granted already
        if( checkSelfPermission( android.Manifest.permission.ACCESS_FINE_LOCATION )
            != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION )
            != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions( arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION ), 0 )
            requestPermissions( arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION ), 0 )
        }

        enableEdgeToEdge()
        setContent {
            VirtuaaliVerstasTheme {
                MainApplication()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApplication() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    HomeScreen(navController)
                }
                composable("weather") {
                    WeatherAppHomeScreen(navController)
                }
                composable("settings") {
                    SettingsScreen(navController)
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            label = { Text(text = stringResource(id = R.string.home))},
            icon = { Text(text = "🏠")},
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            label = { Text(text = stringResource(id = R.string.settings))},
            icon = { Text(text = "⚙️")},
            selected = navController.currentDestination?.route == "settings",
            onClick = { navController.navigate("settings") }
        )
    }
}