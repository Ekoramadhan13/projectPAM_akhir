package com.example.projectpam.uicontroller.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectpam.uicontroller.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanCheckout(
    viewModel: CheckoutViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("COD") }
    var selectedProvider by remember { mutableStateOf("DANA") }

    val success by viewModel.success.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(success) {
        if (success) onSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali ke Keranjang"
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
                .padding(16.dp)
        ) {

            /* ===== ALAMAT ===== */
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Alamat Pengiriman") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            /* ===== METODE PEMBAYARAN ===== */
            Text(
                "Metode Pembayaran",
                style = MaterialTheme.typography.titleMedium
            )

            Row {
                RadioButton(
                    selected = paymentMethod == "COD",
                    onClick = { paymentMethod = "COD" }
                )
                Text("COD")
            }

            Row {
                RadioButton(
                    selected = paymentMethod == "EWALLET",
                    onClick = { paymentMethod = "EWALLET" }
                )
                Text("E-Wallet")
            }

            /* ===== PILIH E-WALLET ===== */
            if (paymentMethod == "EWALLET") {

                Spacer(Modifier.height(12.dp))

                Text(
                    "Pilih E-Wallet",
                    style = MaterialTheme.typography.titleMedium
                )

                Row {
                    RadioButton(
                        selected = selectedProvider == "DANA",
                        onClick = { selectedProvider = "DANA" }
                    )
                    Text("DANA")
                }

                Row {
                    RadioButton(
                        selected = selectedProvider == "OVO",
                        onClick = { selectedProvider = "OVO" }
                    )
                    Text("OVO")
                }
            }

            Spacer(Modifier.height(24.dp))

            /* ===== CHECKOUT ===== */
            Button(
                onClick = {
                    viewModel.checkout(
                        address = address,
                        method = paymentMethod,
                        provider = if (paymentMethod == "EWALLET") selectedProvider else null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buat Pesanan")
            }

            /* ===== ERROR ===== */
            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
