package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onNavigateToProducts: () -> Unit // tambahkan ini
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Beranda") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            Text("Selamat datang di E-Commerce Buah & Sayur", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNavigateToProducts) { // panggil callback navigasi
                Text("Lihat Produk")
            }
        }
    }
}
