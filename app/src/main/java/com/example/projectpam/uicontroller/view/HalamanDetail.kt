package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.CartViewModel
import com.example.projectpam.uicontroller.viewmodel.ProductUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    productId: Int,
    productViewModel: ProductUserViewModel,
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onGoToCart: () -> Unit // navigasi ke halaman cart
) {
    var product by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        product = productViewModel.getProductDetail(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val data = product!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(data.name, style = MaterialTheme.typography.titleLarge)
                    Text("Harga     : Rp ${data.price}")
                    Text("Stok      : ${data.stock}")
                    Text("Kategori  : ${data.category}")
                    Text("Deskripsi : ${data.description}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            data.product_id?.let {
                                cartViewModel.addToCart(it, 1)
                                onGoToCart() // langsung navigasi ke cart
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = data.stock > 0
                    ) {
                        Text("Tambah ke Keranjang")
                    }
                }
            }
        }
    }
}
