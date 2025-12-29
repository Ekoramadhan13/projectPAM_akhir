package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.AdminProductViewModel

@Composable
fun HalamanManageProduct(
    viewModel: AdminProductViewModel,
    onNavigateToForm: (Product?) -> Unit
) {
    val products by viewModel.products.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadProducts() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Manage Produk", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onNavigateToForm(null) }) { Text("Tambah Produk") }
        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(message, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nama: ${product.name}", style = MaterialTheme.typography.titleMedium)
                        Text("Harga: ${product.price}")
                        Text("Stok: ${product.stock}")
                        Text("Kategori: ${product.category}")

                        product.image_url.takeIf { it.isNotEmpty() }?.let { url ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = rememberAsyncImagePainter(url),
                                contentDescription = "Gambar Produk",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { onNavigateToForm(product) }) {
                                Text("Edit")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                product.product_id?.let { id ->
                                    if (id > 0) viewModel.deleteProduct(id)
                                }
                            }) {
                                Text("Hapus")
                            }
                        }
                    }
                }
            }
        }
    }
}
