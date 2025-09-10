@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.AuthResult
import com.example.myapplication.repository.ClassLocation
import com.example.myapplication.viewmodel.AuthViewModel

/* ----------------------- LOGIN ----------------------- */

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var firstName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf(ClassLocation.FOOTSCRAY) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val authResult by authViewModel.authResult.collectAsState()
    
    // Handle authentication result
    LaunchedEffect(authResult) {
        when (val result = authResult) {
            is AuthResult.Success -> {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthResult.Error -> {
                showError = true
                errorMessage = result.message
            }
            is AuthResult.Loading -> {
                // Loading state handled in UI
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Meditation App", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        // Class Location Selection
        Text("Select your class location:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClassLocation.values().forEach { location ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = selectedLocation == location,
                            onClick = { selectedLocation = location }
                        )
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = selectedLocation == location,
                        onClick = { selectedLocation = location }
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = location.name,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
        
        Spacer(Modifier.height(24.dp))

        // First Name Input
        OutlinedTextField(
            value = firstName,
            onValueChange = { 
                firstName = it
                showError = false
            },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError
        )
        Spacer(Modifier.height(8.dp))
        
        // Student ID Input
        OutlinedTextField(
            value = studentId,
            onValueChange = { 
                studentId = it
                showError = false
            },
            label = { Text("Student ID (8 digits)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = showError
        )
        
        // Error Message
        if (showError) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Login Button
        Button(
            onClick = { 
                if (firstName.isBlank() || studentId.isBlank()) {
                    showError = true
                    errorMessage = "Please enter both first name and student ID"
                } else if (studentId.length != 8 || !studentId.all { it.isDigit() }) {
                    showError = true
                    errorMessage = "Student ID must be exactly 8 digits"
                } else {
                    authViewModel.login(firstName, studentId, selectedLocation)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authResult !is AuthResult.Loading
        ) { 
            if (authResult is AuthResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
            }
            Text("Login") 
        }
    }
}

/* --------------------- DASHBOARD -------------------- */

@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    TextButton(onClick = { navController.navigate("settings") }) {
                        Text("Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Welcome to your meditation journey!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Text("You have successfully logged in.", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(24.dp))
            Button(onClick = { navController.navigate("home") }) { Text("Continue to Home") }
        }
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
fun DashboardScreenPreview() {
    val nav = rememberNavController()
    DashboardScreen(nav)
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
