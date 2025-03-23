package com.example.virtuaaliverstas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
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
    var inputApiKey by remember { mutableStateOf("") }
    var savedApiKey by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            savedLocation = getPlace(context)
            useCurrentLocation = getUseLocation(context)
            savedApiKey = getApiKey(context)
        }
    }

    fun storeLocation() {
        if (inputLocation.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.empty_input_location), Toast.LENGTH_SHORT).show()
            return
        }
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

    fun storeApiKey() {
        if (inputApiKey.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.empty_api_key), Toast.LENGTH_SHORT).show()
            return
        }
        savedApiKey = inputApiKey
        inputApiKey = ""
        CoroutineScope(Dispatchers.IO).launch {
            saveApiKey(context, savedApiKey)
        }
        keyboardController?.hide()
        Toast.makeText(context, context.getString(R.string.api_key_saved), Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier.
            fillMaxSize().
            padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Use current location section
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.use_current_location),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(
                onClick = { updateUseLocation() },
                modifier = Modifier.weight(0.2f),
                shape = MaterialTheme.shapes.small,
                colors = if (useCurrentLocation) {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                },
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = if (useCurrentLocation) stringResource(id = R.string.checked)
                        else stringResource(id = R.string.unchecked),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Save location section
        Text(
            text = stringResource(id = R.string.save_location_info),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 0.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputLocation,
                onValueChange = { inputLocation = it },
                label = {
                    Text(
                        text = "${stringResource(id = R.string.default_place)}: $savedLocation",
                        style = MaterialTheme.typography.titleSmall
                    )},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        storeLocation()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = MaterialTheme.shapes.small
            )
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    storeLocation()
                },
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(id = R.string.checked),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Insert API key section
        Text(
            text = stringResource(id = R.string.insert_api_key),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 0.dp)
        )
        // API key text based on if it's found
        val apiDisplayText = if (savedApiKey.isNotEmpty()) {
            stringResource(id = R.string.api_key_saved)
        } else {
            stringResource(id = R.string.no_saved_api_key)
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputApiKey,
                onValueChange = { inputApiKey = it },
                label = {
                    Text(
                        text = apiDisplayText,
                        style = MaterialTheme.typography.titleSmall
                    )},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        storeApiKey()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = MaterialTheme.shapes.small
            )
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    storeApiKey()
                },
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(id = R.string.checked),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}