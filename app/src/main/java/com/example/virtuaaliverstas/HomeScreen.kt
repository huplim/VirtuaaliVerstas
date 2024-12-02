package com.example.virtuaaliverstas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current

    val items = listOf(
        NavigationItem(label = context.getString(R.string.weather), route = "weather"),
        NavigationItem(label = context.getString(R.string.settings), route = "settings"),
        NavigationItem(label = context.getString(R.string.calculator), route = "home"),
        NavigationItem(label = context.getString(R.string.notes), route = "home"),
        NavigationItem(label = context.getString(R.string.shopping_list), route = "home")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.welcome_text),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items.size) { index ->
                val item = items[index]
                Button(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.aspectRatio(1f).defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
                ) {
                    Text(text = item.label)
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val route: String
)