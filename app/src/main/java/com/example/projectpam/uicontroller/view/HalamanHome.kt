package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onNavigateToProducts: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToOrders: () -> Unit,   // 🔥 BARU
    onBackToLogin: () -> Unit
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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Keranjang Saya")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 TOMBOL BARU
            Button(
                onClick = onNavigateToOrders,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lihat Pesanan Saya")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Keluar / Kembali ke Login")
            }
        }
    }
}
