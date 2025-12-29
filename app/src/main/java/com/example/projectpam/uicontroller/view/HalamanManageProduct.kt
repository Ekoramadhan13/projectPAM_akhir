package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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

    // Load data sekali saja
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Produk",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateToForm(null) }
        ) {
            Text("Tambah Produk")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ================= LIST PRODUK (SCROLL) =================
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.product_id ?: it.hashCode() }) { product ->
                ProductCard(
                    product = product,
                    onEdit = { onNavigateToForm(product) },
                    onDelete = {
                        product.product_id?.let { id ->
                            viewModel.deleteProduct(id)
                        }
                    }
                )
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

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            if (product.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = product.description)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Harga: Rp ${product.price}")
            Text("Stok: ${product.stock}")
            Text("Kategori: ${product.category}")

            // ================= GAMBAR =================
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
                OutlinedButton(onClick = onEdit) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = onDelete
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}
