package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projectpam.uicontroller.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanCart(
    viewModel: CartViewModel,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val carts by viewModel.cartItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load cart saat komposisi pertama
    LaunchedEffect(Unit) { viewModel.loadCart() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keranjang") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            Column(Modifier.padding(16.dp)) {
                Text("Total: Rp ${viewModel.totalPrice()}")
                Button(
                    onClick = onCheckout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Checkout")
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Error: $errorMessage")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(carts) { cart ->
                    val product = cart.product
                    if (product == null) return@items

                    val productId = product.product_id ?: return@items
                    val productName = product.name
                    val productDesc = product.description
                    val productPrice = product.price
                    val quantity = cart.quantity
                    val imageUrl = product.image_url

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            if (imageUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = productName,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Text(productName, style = MaterialTheme.typography.titleMedium)
                            Text("Deskripsi: $productDesc")
                            Text("Harga: Rp $productPrice")
                            Text("Qty: $quantity")
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = { viewModel.decrease(productId) }) { Text("-") }
                                IconButton(onClick = { viewModel.increase(productId) }) { Text("+") }
                                Spacer(Modifier.weight(1f))
                                TextButton(onClick = { viewModel.remove(productId) }) { Text("Hapus") }
                            }
                        }
                    }
                }
            }
        }
    }
}
