package com.example.virtuaaliverstas

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

val WEATHER_KEY = stringPreferencesKey("weather_place")

suspend fun savePlace(context: Context, place: String) {
    context.dataStore.edit { prefs ->
        prefs[WEATHER_KEY] = place
    }
}

suspend fun getPlace(context: Context): String {
    val prefs = context.dataStore.data.first()
    return prefs[WEATHER_KEY] ?: "-"
}

/*
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

val FIRSTNAME_KEY = stringPreferencesKey("user_first_name")
val LASTNAME_KEY = stringPreferencesKey("user_last_name")

suspend fun saveName(context: Context, firstName: String, lastName: String) {
    context.dataStore.edit { prefs ->
        prefs[FIRSTNAME_KEY] = firstName
        prefs[LASTNAME_KEY] = lastName
    }
}

suspend fun getName(context: Context): Pair<String, String> {
    val prefs = context.dataStore.data.first()
    return Pair(
        prefs[FIRSTNAME_KEY] ?: "First name not defined.",
        prefs[LASTNAME_KEY] ?: "Last name not defined."
    )
}

@Preview (showBackground = true)
@Composable
fun DataStoreTest() {
    val context = LocalContext.current

    var inputFirstName by remember { mutableStateOf("") }
    var inputLastName by remember { mutableStateOf("") }

    var savedFirstName by remember { mutableStateOf("") }
    var savedLastName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = inputFirstName,
                onValueChange = { inputFirstName = it },
                label = { Text("Insert first name") }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            TextField(
                value = inputLastName,
                onValueChange = { inputLastName = it },
                label = { Text("Insert last name") }
            )
            Spacer(modifier = Modifier.padding(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        saveName(context, inputFirstName, inputLastName)
                    }
                }) {
                    Text("Save name")
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val name = getName(context)
                        withContext(Dispatchers.Main) {
                            if (name.first.isNotEmpty()) savedFirstName = name.first
                            if (name.second.isNotEmpty()) savedLastName = name.second
                        }
                    }
                }) {
                    Text("Get name")
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Text("Name:")
            Text(text = "$savedFirstName $savedLastName")
        }
    }
}
 */