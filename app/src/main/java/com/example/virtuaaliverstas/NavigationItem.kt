package com.example.virtuaaliverstas

import android.content.Context

data class NavigationItem(
    val label: String,
    val route: String,
    val description: String,
)

fun getNavigationItems(context: Context): List<NavigationItem> {
    return listOf(
        NavigationItem(
            label = context.getString(R.string.weather),
            route = "weather",
            description = ""),
        NavigationItem(
            label = context.getString(R.string.calculator),
            route = "home",
            description = context.getString(R.string.coming_soon)),
        NavigationItem(
            label = context.getString(R.string.notes),
            route = "home",
            description = context.getString(R.string.coming_soon)),
        NavigationItem(
            label = context.getString(R.string.shopping_list),
            route = "home",
            description = context.getString(R.string.coming_soon)),
        NavigationItem(
            label = context.getString(R.string.qr_reader),
            route = "qr_reader",
            description = ""),
        NavigationItem(
            label = context.getString(R.string.level_tool),
            route = "home",
            description = context.getString(R.string.coming_soon)),
        NavigationItem(
            label = context.getString(R.string.fun_corner),
            route = "home",
            description = context.getString(R.string.coming_soon)),
    )
}