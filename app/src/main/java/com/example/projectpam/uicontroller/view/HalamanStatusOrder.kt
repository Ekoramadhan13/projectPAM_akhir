package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectpam.uicontroller.viewmodel.OrderStatusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanStatusOrder(
    viewModel: OrderStatusViewModel,
    onBackToHome: () -> Unit
) {
    val orders by viewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Status Pesanan") },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Beranda"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada pesanan")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {

                            Text("Order ID: ${order.order_id}")
                            Text("Tanggal: ${order.created_at}")
                            Text("Status: ${order.status}")
                            Text("Total: Rp ${order.total_price}")

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "Item Pesanan:",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(6.dp))

                            order.items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    // 🔥 GAMBAR PRODUK
                                    AsyncImage(
                                        model = item.product?.image_url,
                                        contentDescription = item.product?.name,
                                        modifier = Modifier
                                            .size(60.dp)
                                    )

                                    Spacer(Modifier.width(12.dp))

                                    Column {
                                        Text(item.product?.name ?: "Produk")
                                        Text("Qty: ${item.quantity}")
                                        Text("Harga: Rp ${item.price}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
