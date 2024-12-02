package com.example.virtuaaliverstas

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var inputLocation by remember { mutableStateOf("") }
    var savedLocation by remember { mutableStateOf("") }
    var useCurrentLocation by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            savedLocation = getPlace(context)
            useCurrentLocation = getUseLocation(context)
        }
    }

    fun storeLocation() {
        savedLocation = inputLocation
        inputLocation = ""
        CoroutineScope(Dispatchers.IO).launch {
            savePlace(context, savedLocation)
        }
        keyboardController?.hide()
        Toast.makeText(context, context.getString(R.string.location_saved), Toast.LENGTH_SHORT).show()
    }

    fun updateUseLocation() {
        useCurrentLocation = !useCurrentLocation
        CoroutineScope(Dispatchers.IO).launch {
            updateUseLocation(context, useCurrentLocation)
        }
        if (useCurrentLocation) {
            Toast.makeText(context, context.getString(R.string.use_location_updated), Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.save_location_info),
            style = MaterialTheme.typography.titleSmall,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = inputLocation,
                onValueChange = { inputLocation = it },
                label = { Text(text = "${stringResource(id = R.string.default_place)}: $savedLocation")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        storeLocation()
                    }
                )
            )
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    storeLocation()
                }
            ) {
                Text(text = stringResource(id = R.string.save_location))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.use_current_location),
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.width(16.dp))

            Checkbox(
                checked = useCurrentLocation,
                onCheckedChange = { updateUseLocation() }
            )
        }

    }
}