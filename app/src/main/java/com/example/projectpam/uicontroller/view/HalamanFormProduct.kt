package com.example.projectpam.uicontroller.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.AdminProductViewModel
import com.example.projectpam.utils.uriToMultipart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanFormProduct(
    viewModel: AdminProductViewModel,
    product: Product? = null,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "Buah") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (product == null) "Tambah Produk" else "Edit Produk") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Produk") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi") })
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Harga") })
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stok") })

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                listOf("Buah", "Sayur").forEach { cat ->
                    Row(modifier = Modifier.padding(end = 8.dp)) {
                        RadioButton(selected = category == cat, onClick = { category = cat })
                        Text(cat, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = if (imageUri == null) "Pilih Gambar" else "Ganti Gambar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (name.isNotBlank() && description.isNotBlank() && price.isNotBlank() && stock.isNotBlank()) {
                    try {
                        val imagePart = imageUri?.let { uriToMultipart(it, context.contentResolver, "image") }
                        if (product == null) {
                            viewModel.addOrUpdateProductMultipart(
                                name, description, price.toDouble(), stock.toInt(),
                                category, imagePart, isUpdate = false
                            )
                        } else {
                            val productId = product.product_id ?: 0
                            viewModel.addOrUpdateProductMultipart(
                                name, description, price.toDouble(), stock.toInt(),
                                category, imagePart, isUpdate = true, productId = productId
                            )
                        }
                        onNavigateBack() // kembali ke ManageProduct
                    } catch (e: Exception) {
                        Log.e("HalamanFormProduct", "Error simpan produk: ${e.message}")
                    }
                }
            }) {
                Text(if (product == null) "Tambah Produk" else "Update Produk")
            }
        }
    }
}
