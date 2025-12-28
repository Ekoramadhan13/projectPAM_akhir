package com.example.projectpam.uicontroller.view

import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projectpam.modeldata.Product
import com.example.projectpam.uicontroller.viewmodel.AdminProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun HalamanFormProduct(
    viewModel: AdminProductViewModel,
    product: Product? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "Buah") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePreview by remember { mutableStateOf(product?.image_url ?: "") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imagePreview = it.toString() // Preview pakai Coil
        }
    }

    fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val fileName = uri.lastPathSegment ?: "temp_image"
            val tempFile = File(context.cacheDir, fileName)
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = if (product == null) "Tambah Produk" else "Edit Produk",
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

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

        // Tombol pilih gambar
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pilih Gambar (JPG/PNG)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (imagePreview.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imagePreview),
                contentDescription = "Preview Gambar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (name.isNotBlank() && description.isNotBlank() && price.isNotBlank() && stock.isNotBlank()) {
                val file = imageUri?.let { uriToFile(it) }

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (product == null) {
                            viewModel.addOrUpdateProductMultipart(
                                name, description, price.toDouble(), stock.toInt(),
                                category, file, isUpdate = false
                            )
                        } else {
                            viewModel.addOrUpdateProductMultipart(
                                name, description, price.toDouble(), stock.toInt(),
                                category, file, isUpdate = true, productId = product.product_id
                            )
                        }
                        onNavigateBack()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }) {
            Text(if (product == null) "Tambah Produk" else "Update Produk")
        }
    }
}
