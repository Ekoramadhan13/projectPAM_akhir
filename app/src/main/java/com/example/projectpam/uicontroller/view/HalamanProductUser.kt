package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.ProductUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanProductUser(
    productUserViewModel: ProductUserViewModel,
    onProductClick: (Product) -> Unit,
    onBack: () -> Unit
) {
    // State untuk query search
    var searchQuery by remember { mutableStateOf("") }

    // Load produk awal saat halaman muncul
    LaunchedEffect(Unit) {
        productUserViewModel.loadProducts()
    }

    val products = productUserViewModel.products.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Produk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // ===== Input Search =====
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    productUserViewModel.searchProducts(query) // panggil search
                },
                label = { Text("Search Produk") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== List Produk =====
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada produk")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onProductClick(product) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val imageUrl =
                                    "http://10.0.2.2:3000/uploads/${product.image_url}" // pastikan path benar
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(text = "Harga: Rp ${product.price}")
                                    Text(text = "Stok: ${product.stock}")
                                    Text(text = "Kategori: ${product.category}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
