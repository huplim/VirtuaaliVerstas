package com.example.virtuaaliverstas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (isConnected) {
            Toast.makeText(
                context,
                context.getString(R.string.connection_found),
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.connection_lost),
                Toast.LENGTH_SHORT).show()
        }
    }
}