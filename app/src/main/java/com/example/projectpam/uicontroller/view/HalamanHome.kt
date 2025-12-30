package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onNavigateToProducts: () -> Unit, // navigasi ke halaman produk
    onBackToLogin: () -> Unit         // navigasi kembali ke login
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Beranda") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Selamat datang di E-Commerce Buah & Sayur",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToProducts,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lihat Produk")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol kembali ke login
            OutlinedButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Keluar / Kembali ke Login")
            }
        }
    }
}
