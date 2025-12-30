package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    onBack: () -> Unit,
    onGoToCart: () -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Load data sekali saat screen pertama muncul
    LaunchedEffect(Unit) {
        productUserViewModel.loadProducts()
    }

    val products by productUserViewModel.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Produk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /* ===== SEARCH ===== */
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    productUserViewModel.searchProducts(it)
                },
                label = { Text("Cari Produk") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                singleLine = true
            )

            /* ===== LIST ===== */
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Produk tidak ditemukan")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(products, key = { it.product_id ?: it.hashCode() }) { product ->
                        ProductItem(
                            product = product,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            /* ===== IMAGE ===== */
            if (!product.image_url.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(product.image_url),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 12.dp),
                    contentScale = ContentScale.Crop
                )
            }

            /* ===== INFO ===== */
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Harga: Rp ${product.price}")
                Text("Stok: ${product.stock}")
                Text("Kategori: ${product.category}")
            }
        }
    }
}
