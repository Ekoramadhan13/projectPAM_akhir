package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.ProductUserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    productId: Int,
    viewModel: ProductUserViewModel,
    onBack: () -> Unit
) {
    var product by remember { mutableStateOf<Product?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        coroutineScope.launch {
            product = viewModel.getProductDetail(productId)
        }
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
        if (product == null) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                Text("Produk tidak ditemukan", modifier = Modifier.padding(16.dp))
            }
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nama: ${product!!.name}", style = MaterialTheme.typography.titleLarge)
                Text("Harga: Rp ${product!!.price}")
                Text("Stok: ${product!!.stock}")
                Text("Deskripsi: ${product!!.description}")
            }
        }
    }
}
