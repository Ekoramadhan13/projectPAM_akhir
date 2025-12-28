package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projectpam.uicontroller.viewmodel.AuthViewModel
import com.example.projectpam.uicontroller.viewmodel.AuthState

@Composable
fun HalamanLogin(
    viewModel: AuthViewModel,
    onLoginUserSuccess: () -> Unit,    // Navigasi ke HOME
    onLoginAdminSuccess: () -> Unit,   // Navigasi ke MANAGE PRODUCT
    onRegisterClick: (() -> Unit)? = null
) {
    val authState by viewModel.authState.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Handle login success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            if (isAdmin) onLoginAdminSuccess() else onLoginUserSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Contoh: email admin dianggap email khusus
                if (email.contains("admin")) viewModel.loginAdmin(email, password)
                else viewModel.loginUser(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Show error message
        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Optional register link (hanya user)
        onRegisterClick?.let {
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = it) {
                Text("Belum punya akun? Daftar")
            }
        }
    }
}
