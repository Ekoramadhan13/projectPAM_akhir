package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.AdminProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanManageProduct(
    viewModel: AdminProductViewModel,
    onNavigateToForm: (Product?) -> Unit,
    onBackToLogin: () -> Unit // ganti parameter agar jelas ini back ke login
) {
    val products by viewModel.products.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadProducts() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Produk") },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Login")
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
            // Tombol Tambah Produk
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToForm(null) } // navigasi ke FormProduct
            ) {
                Text("Tambah Produk")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pesan jika ada
            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // List Produk, ambil sisa ruang dengan weight(1f)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products, key = { it.product_id ?: it.hashCode() }) { product ->
                    ProductCard(
                        product = product,
                        onEdit = { onNavigateToForm(product) },
                        onDelete = { product.product_id?.let { id -> viewModel.deleteProduct(id) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Spacer(modifier = Modifier.height(6.dp))
            Text("Nama: ${product.name}")
            Text("Deskripsi: ${product.description}")
            Text("Harga: Rp ${product.price}")
            Text("Stok: ${product.stock}")
            Text("Kategori: ${product.category}")

            // Gambar produk
            if (product.image_url.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(product.image_url),
                    contentDescription = "Gambar Produk",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onEdit) { Text("Edit") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    onClick = onDelete
                ) { Text("Hapus") }
            }
        }
    }
}
