@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview

/* ----------------------- LOGIN ----------------------- */

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Meditation App", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Begin Journey") }
    }
}

/* ----------------------- HOME ------------------------ */

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meditate") },
                actions = {
                    // keep it simple: text action instead of icon dependency
                    TextButton(onClick = { navController.navigate("settings") }) {
                        Text("Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Welcome to your daily calm.", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { /* TODO: start session */ }) { Text("Start Session") }
        }
    }
}

/* ---------------------- SETTINGS --------------------- */

@Composable
fun SettingsScreen(navController: NavController) {
    var sounds by remember { mutableStateOf(true) }
    var reminders by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Enable sounds")
            Switch(checked = sounds, onCheckedChange = { sounds = it })
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Daily reminders")
            Switch(checked = reminders, onCheckedChange = { reminders = it })
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) { Text("Save & Back") }
    }
}

/* ------------------------ PREVIEWS ------------------- */

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val nav = rememberNavController()
    LoginScreen(nav)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val nav = rememberNavController()
    HomeScreen(nav)
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val nav = rememberNavController()
    SettingsScreen(nav)
}
